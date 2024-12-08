package wiki.parser.app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.apache.commons.compress.compressors.CompressorException;
import org.springframework.stereotype.Service;
import wiki.parser.core.WikiIndexParser;
import wiki.parser.core.WikiIndexTransformer;
import wiki.parser.core.WikiParser;
import wiki.parser.core.model.WikiIndex;
import wiki.parser.core.model.WikiPage;
import wiki.parser.core.reader.XmlMultipartReader;
import wiki.parser.core.reader.XmlReader;
import wiki.parser.database.model.WikiSourceEntity;
import wiki.parser.database.service.WikiService;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.stream.Collectors;

@Service
@Log
@RequiredArgsConstructor
public class WikiFileProcessor {

    private final WikiService wikiService;

    public void process(String wikiFileName, String indexFileName, boolean decompress) throws XMLStreamException, CompressorException, IOException {
        WikiIndexParser wikiIndexParser = new WikiIndexParser(indexFileName);
        log.info("Reading index");
        List<WikiIndex> indices = wikiIndexParser.readAll();
        log.info("Index entry count: " + indices.size());
        log.info("Storing source");
        WikiSourceEntity source = wikiService.storeSource(wikiFileName, List.of());
        log.info("Parser setup");
        XmlReader reader = new XmlMultipartReader(wikiFileName, decompress);
        WikiParser wikiParser = new WikiParser(reader);
        log.info("Starting to read pages");
        int i = 0;
        try {
            while (true) {
                WikiPage page = wikiParser.readNext();
                wikiService.storePages(source, Set.of(page));
                if (i % 1000 == 0) {
                    log.info("Processed pages: " + i);
                }
                i++;
            }
        } catch (Exception e) {
            log.info("Error while processing page " + i);
            throw new RuntimeException(e);
        } finally {
            wikiParser.close();
        }
    }

    public List<WikiPage> process(String wikiFileName, String indexFileName, boolean decompress, String title) throws XMLStreamException, CompressorException, IOException {
        WikiIndexParser wikiIndexParser = new WikiIndexParser(indexFileName);
        log.info("Reading index");
        List<WikiIndex> indices = wikiIndexParser.readAll();
        log.info("Index entry count: " + indices.size());
        List<WikiIndex> interestingPages = indices.stream().filter((it) -> it.getTitle().toString().contains(title)).toList();
        Set<String> interestingTitles = interestingPages.stream().map(WikiIndex::getTitle)
                .map((it) -> it.content().getFirst()).collect(Collectors.toSet());
        log.info("Found " + interestingPages.size() + " interesting pages with titles: " + interestingTitles);
        log.info("Parser setup");
        WikiIndexTransformer wikiIndexTransformer = new WikiIndexTransformer(indices);
        var ranges = wikiIndexTransformer.toRange(interestingPages);
        log.info("Pages are in ranges: " + ranges);
        XmlReader reader = new XmlMultipartReader(wikiFileName, decompress, ranges);
        WikiParser wikiParser = new WikiParser(reader);
        log.info("Starting to read pages");
        List<WikiPage> pages = new ArrayList<>();
        int readPageCount = 0;
        try {
            while (true) {
                var page = wikiParser.readNext();
                readPageCount++;
                //log.info("Title: " + page.getTitle());
                if (!page.getTitle().isEmpty() && interestingTitles.contains(page.getTitle())) {
                    pages.add(page);
                }
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, e.getMessage());
            log.log(Level.SEVERE, Arrays.toString(e.getStackTrace()));
        } finally {
            wikiParser.close();
            log.info("Read " + readPageCount + " total pages");
        }
        if (interestingPages.size() != pages.size()) {
            log.info("Read pages with titles: " + pages.stream().map(WikiPage::getTitle).toList());
            throw new IllegalStateException("Should have retrieved " + interestingPages.size() + " interesting pages but instead retrieved " + pages.size());
        }
        log.info("Read " + pages.size() + " interesting pages");
        return pages;
    }
}

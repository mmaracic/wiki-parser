package wiki.parser.app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.apache.commons.compress.compressors.CompressorException;
import org.springframework.stereotype.Service;
import wiki.parser.core.IndexParser;
import wiki.parser.core.WikiParser;
import wiki.parser.core.model.WikiIndex;
import wiki.parser.core.model.WikiPage;
import wiki.parser.core.reader.WikiMultipartReader;
import wiki.parser.core.reader.XmlReader;
import wiki.parser.database.model.WikiSourceEntity;
import wiki.parser.database.service.WikiService;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.List;
import java.util.Set;

@Service
@Log
@RequiredArgsConstructor
public class WikiFileProcessor {

    private final WikiService wikiService;

    public void process(String wikiFileName, String indexFileName, boolean decompress) throws XMLStreamException, CompressorException, IOException {
        IndexParser indexParser = new IndexParser(indexFileName);
        log.info("Reading index");
        List<WikiIndex> indices = indexParser.readAll();
        log.info("Index entry count: " + indices.size());
        log.info("Storing source");
        WikiSourceEntity source = wikiService.storeSource(wikiFileName, List.of());
        log.info("Parser setup");
        XmlReader reader = new WikiMultipartReader(wikiFileName, indices, decompress);
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
}

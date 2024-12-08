package wiki.parser.app.service;

import lombok.extern.java.Log;
import org.apache.commons.compress.compressors.CompressorException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import wiki.parser.core.model.WikiPage;
import wiki.parser.database.AbstractDatabaseTest;
import wiki.parser.database.DatabaseConfiguration;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.List;

@Log
@SpringBootTest
@ContextConfiguration(classes = {DatabaseConfiguration.class, WikiFileProcessor.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class WikiFileProcessorTest extends AbstractDatabaseTest {

    @Autowired
    private WikiFileProcessor wikiFileProcessor;

    //ToDo test this one
    @Disabled("Full wiki processing not appropriate for automated tests")
    @Test
    public void testFullWikiProcessing() throws XMLStreamException, IOException, CompressorException {
        String indexFileName = "../data/simplewiki-20240901-pages-articles-multistream-index.txt.bz2";
        String wikiFileName = "../data/simplewiki-20240901-pages-articles-multistream.xml.bz2";

        log.info("Starting to read source");
        wikiFileProcessor.process(wikiFileName, indexFileName, true);
        log.info("Done reading source");
    }

    @Test
    //@Disabled("Full wiki processing not appropriate for automated tests")
    public void testSelectiveWikiProcess() throws XMLStreamException, IOException, CompressorException {
        String indexFileName = "../data/simplewiki-20240901-pages-articles-multistream-index.txt.bz2";
        String wikiFileName = "../data/simplewiki-20240901-pages-articles-multistream.xml.bz2";

        List<WikiPage> interestingPages = wikiFileProcessor.process(wikiFileName, indexFileName, true, "april");
        Assertions.assertFalse(interestingPages.isEmpty());
        var fetchedTitleList = interestingPages.stream().map(WikiPage::getTitle).toList();
        var expectedTitleList = List.of("Emilio Caprile", "Caprile", "Henrique Capriles");
        Assertions.assertTrue(
                fetchedTitleList.size() == expectedTitleList.size()
                        && fetchedTitleList.containsAll(expectedTitleList)
                        && expectedTitleList.containsAll(fetchedTitleList));
    }
}

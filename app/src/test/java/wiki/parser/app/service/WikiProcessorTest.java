package wiki.parser.app.service;

import lombok.extern.java.Log;
import org.apache.commons.compress.compressors.CompressorException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import wiki.parser.database.AbstractDatabaseTest;
import wiki.parser.database.DatabaseConfiguration;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

@Log
@SpringBootTest
@ContextConfiguration(classes = {DatabaseConfiguration.class, WikiFileProcessor.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class WikiProcessorTest extends AbstractDatabaseTest {

    @Autowired
    private WikiFileProcessor wikiFileProcessor;

    @Test
    public void testWiki() throws XMLStreamException, IOException, CompressorException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        String indexFileName = "../data/simplewiki-20240901-pages-articles-multistream-index.txt.bz2";
        String wikiFileName = "../data/simplewiki-20230820-pages-articles-multistream.xml.bz2";

        log.info("Starting to read source");
        wikiFileProcessor.process(wikiFileName, indexFileName, true);
        log.info("Done reading source");
    }
}

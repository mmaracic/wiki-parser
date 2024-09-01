package wiki.parser.core;

import lombok.extern.java.Log;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import wiki.parser.model.WikiPage;

import javax.xml.stream.XMLStreamException;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;

import static wiki.parser.core.Tags.MEDIAWIKI;

@Log
public class XmlParserTest {

    @Test
    public void getPath() {
        log.info(Paths.get("").toFile().getAbsolutePath());
    }

    @Test
    public void testFileExists() {
        Assertions.assertDoesNotThrow(() -> {
            new XmlParser("./src/test/resources/test.xml");
        });
    }

    @Test
    public void testReadTwoPages() throws XMLStreamException, FileNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        var parser = new XmlParser("./src/test/resources/test.xml");
        WikiPage page = parser.readNext(WikiPage.class, new HashSet<>(List.of(MEDIAWIKI)));
        Assertions.assertNotNull(page.getTitle());
        Assertions.assertNotNull(page.getRevison());
        Assertions.assertNotNull(page.getText());
    }

    @Test
    public void testReadOnePgeWithHeader() throws XMLStreamException, FileNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        var parser = new XmlParser("./src/test/resources/test2.xml");
        WikiPage page = parser.readNext(WikiPage.class, new HashSet<>(List.of(MEDIAWIKI)));
        Assertions.assertNotNull(page.getTitle());
        Assertions.assertNotNull(page.getRevison());
        Assertions.assertNotNull(page.getText());
    }

    @Test
    public void testReadOnePage() throws XMLStreamException, FileNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        var parser = new XmlParser("./src/test/resources/test3.xml");
        WikiPage page = parser.readNext(WikiPage.class, new HashSet<>(List.of(MEDIAWIKI)));
        Assertions.assertNotNull(page.getTitle());
        Assertions.assertNotNull(page.getRevison());
        Assertions.assertNotNull(page.getText());
    }
}

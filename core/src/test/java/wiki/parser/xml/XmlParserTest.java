package wiki.parser.xml;

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

import static wiki.parser.xml.Tags.MEDIAWIKI;

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
        var title1 = "April";
        WikiPage page = parser.readNext(WikiPage.class, new HashSet<>(List.of(MEDIAWIKI)));
        Assertions.assertEquals(title1, page.getTitle());
        Assertions.assertNotNull(page.getRevision());
        Assertions.assertNotNull(page.getText());
        Assertions.assertTrue(page.getText().contains(title1));
        var title2 = "August";
        WikiPage page2 = parser.readNext(WikiPage.class, new HashSet<>(List.of(MEDIAWIKI)));
        Assertions.assertEquals(title2, page2.getTitle());
        Assertions.assertNotNull(page2.getRevision());
        Assertions.assertNotNull(page2.getText());
        Assertions.assertTrue(page2.getText().contains(title2));
    }

    @Test
    public void testReadOnePgeWithHeader() throws XMLStreamException, FileNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        var parser = new XmlParser("./src/test/resources/test2.xml");
        var title1 = "April";
        WikiPage page = parser.readNext(WikiPage.class, new HashSet<>(List.of(MEDIAWIKI)));
        Assertions.assertEquals(title1, page.getTitle());
        Assertions.assertNotNull(page.getRevision());
        Assertions.assertNotNull(page.getText());
        Assertions.assertTrue(page.getText().contains(title1));
    }

    @Test
    public void testReadOnePage() throws XMLStreamException, FileNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        var parser = new XmlParser("./src/test/resources/test3.xml");
        WikiPage page = parser.readNext(WikiPage.class, new HashSet<>(List.of(MEDIAWIKI)));
        Assertions.assertNotNull(page.getTitle());
        Assertions.assertNotNull(page.getRevision());
        Assertions.assertNotNull(page.getText());
    }
}

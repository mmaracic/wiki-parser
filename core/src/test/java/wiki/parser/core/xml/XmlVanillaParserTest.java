package wiki.parser.core.xml;

import lombok.extern.java.Log;
import org.apache.commons.compress.compressors.CompressorException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;
import wiki.parser.core.model.WikiPage;
import wiki.parser.core.reader.XmlMultipartReader;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;

import static wiki.parser.core.xml.Tags.MEDIAWIKI;

@Log
public class XmlVanillaParserTest {

    private static final int bufferSize = 100000;

    @Test
    public void getPath() {
        log.info(Paths.get("").toFile().getAbsolutePath());
    }

    @Test
    public void testFileExists() {
        Assertions.assertDoesNotThrow(() -> {
            new XmlVanillaParser(new XmlMultipartReader("./src/test/resources/test.xml", false), bufferSize);
        });
    }

    @Test
    public void testReadTwoPages() throws XMLStreamException, IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, CompressorException, XmlParserException, SAXException {
        var parser = new XmlVanillaParser(new XmlMultipartReader("./src/test/resources/test.xml", false), bufferSize);
        var title1 = "April";
        WikiPage page = parser.readNext(WikiPage.class, new HashSet<>(List.of(MEDIAWIKI)));
        Assertions.assertEquals(title1, page.getTitle());
        Assertions.assertNotNull(page.getRevision());
        Assertions.assertNotNull(page.getText());
        Assertions.assertTrue(page.getText().contains(title1));
        Assertions.assertEquals(-1, parser.getBufferPosition());
        var title2 = "August";
        WikiPage page2 = parser.readNext(WikiPage.class, new HashSet<>(List.of(MEDIAWIKI)));
        Assertions.assertEquals(title2, page2.getTitle());
        Assertions.assertNotNull(page2.getRevision());
        Assertions.assertNotNull(page2.getText());
        Assertions.assertTrue(page2.getText().contains(title2));
        Assertions.assertEquals(-1, parser.getBufferPosition());
    }

    @Test
    public void testReadOnePageWithHeader() throws XMLStreamException, IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, CompressorException, XmlParserException, SAXException {
        var parser = new XmlVanillaParser(new XmlMultipartReader("./src/test/resources/test2.xml", false), bufferSize);
        var title1 = "April";
        WikiPage page = parser.readNext(WikiPage.class, new HashSet<>(List.of(MEDIAWIKI)));
        Assertions.assertEquals(title1, page.getTitle());
        Assertions.assertNotNull(page.getRevision());
        Assertions.assertNotNull(page.getText());
        Assertions.assertTrue(page.getText().contains(title1));
    }

    @Test
    public void testReadOnePage() throws XMLStreamException, IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, CompressorException, XmlParserException, SAXException {
        var parser = new XmlVanillaParser(new XmlMultipartReader("./src/test/resources/test3.xml", false), bufferSize);
        WikiPage page = parser.readNext(WikiPage.class, new HashSet<>(List.of(MEDIAWIKI)));
        Assertions.assertNotNull(page.getTitle());
        Assertions.assertNotNull(page.getRevision());
        Assertions.assertNotNull(page.getText());
    }

    @Test
    public void testReadOnePageWithFullStructure() throws XMLStreamException, IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, CompressorException, XmlParserException, SAXException {
        var parser = new XmlVanillaParser(new XmlMultipartReader("./src/test/resources/test4.xml", false), bufferSize);
        var title1 = "April";
        WikiPage page = parser.readNext(WikiPage.class, new HashSet<>(List.of(MEDIAWIKI)));
        Assertions.assertEquals(title1, page.getTitle());
        Assertions.assertNotNull(page.getRevision());
        Assertions.assertNotNull(page.getText());
        Assertions.assertTrue(page.getText().contains(title1));
    }

    @Test
    public void testReadOnePageAfterPartialPage() throws XMLStreamException, IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, CompressorException, XmlParserException, SAXException {
        var parser = new XmlVanillaParser(new XmlMultipartReader("./src/test/resources/testFormattingError.xml", false), bufferSize);
        var title1 = "Pirate radio";
        WikiPage page = parser.readNext(WikiPage.class, new HashSet<>(List.of(MEDIAWIKI)));
        Assertions.assertEquals(title1, page.getTitle());
        Assertions.assertNotNull(page.getRevision());
        Assertions.assertNotNull(page.getText());
        Assertions.assertTrue(page.getText().contains(title1));
        Assertions.assertEquals(-1, parser.getBufferPosition());
    }

    @Test
    public void testReadOnePageAfterPartialPage2() throws XMLStreamException, IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, CompressorException, XmlParserException, SAXException {
        var parser = new XmlVanillaParser(new XmlMultipartReader("./src/test/resources/testFormattingError2.xml", false), bufferSize);
        var title1 = "Pirate radio";
        WikiPage page = parser.readNext(WikiPage.class, new HashSet<>(List.of(MEDIAWIKI)));
        Assertions.assertEquals(title1, page.getTitle());
        Assertions.assertNotNull(page.getRevision());
        Assertions.assertNotNull(page.getText());
        Assertions.assertTrue(page.getText().contains(title1));
        Assertions.assertEquals(-1, parser.getBufferPosition());
    }
}

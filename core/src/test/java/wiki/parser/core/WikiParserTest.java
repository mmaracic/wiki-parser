package wiki.parser.core;

import org.apache.commons.compress.compressors.CompressorException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;
import wiki.parser.core.model.WikiPage;
import wiki.parser.core.reader.XmlMultipartReader;
import wiki.parser.core.xml.XmlParserException;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class WikiParserTest {

    @Test
    public void testReadOnePage() throws XMLStreamException, IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, CompressorException, XmlParserException, SAXException {
        var parser = new WikiParser(new XmlMultipartReader("./src/test/resources/test4.xml", false));
        WikiPage page = parser.readNext();
        var title1 = "April";
        Assertions.assertEquals(title1, page.getTitle());
        Assertions.assertNotNull(page.getRevision());
        Assertions.assertNotNull(page.getText());
        Assertions.assertTrue(page.getText().contains(title1));
    }
}

package wiki.parser.core;

import org.apache.commons.compress.compressors.CompressorException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import wiki.parser.core.model.WikiPage;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class WikiParserTest {

    @Test
    public void testReadOnePage() throws XMLStreamException, IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, CompressorException {
        var parser = new WikiParser("./src/test/resources/test3.xml", false);
        WikiPage page = parser.readNext();
        var title1 = "April";
        Assertions.assertEquals(title1, page.getTitle());
        Assertions.assertNotNull(page.getRevision());
        Assertions.assertNotNull(page.getText());
        Assertions.assertTrue(page.getText().contains(title1));
    }
}

package wiki.parser.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import wiki.parser.core.model.WikiPage;

import javax.xml.stream.XMLStreamException;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;

public class WikiParserTest {

    @Test
    public void testReadOnePage() throws XMLStreamException, FileNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        var parser = new WikiParser("./src/test/resources/test3.xml");
        WikiPage page = parser.readNext();
        var title1 = "April";
        Assertions.assertEquals(title1, page.getTitle());
        Assertions.assertNotNull(page.getRevision());
        Assertions.assertNotNull(page.getText());
        Assertions.assertTrue(page.getText().contains(title1));
    }
}

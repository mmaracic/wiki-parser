package wiki.parser.core;

import org.xml.sax.SAXException;
import wiki.parser.core.filter.MarkupFilter;
import wiki.parser.core.filter.WikiMarkupFilter;
import wiki.parser.core.model.WikiPage;
import wiki.parser.core.reader.XmlReader;
import wiki.parser.core.xml.XmlParser;
import wiki.parser.core.xml.XmlParserException;
import wiki.parser.core.xml.XmlVanillaParser;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.List;

import static wiki.parser.core.xml.Tags.MEDIAWIKI;

public class WikiParser implements Parser<WikiPage> {

    private final MarkupFilter markupFilter = new WikiMarkupFilter();
    private final XmlParser xmlParser;

    public WikiParser(XmlReader reader) {
        xmlParser = new XmlVanillaParser(reader, 1000000);
    }

    @Override
    public void close() throws XMLStreamException, IOException {
        xmlParser.close();
    }

    @Override
    public WikiPage readNext() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, XmlParserException, XMLStreamException, IOException, SAXException {
        WikiPage page = xmlParser.readNext(WikiPage.class, new HashSet<>(List.of(MEDIAWIKI)));
        String filterInput = page.getText();
        String filteredOutput = markupFilter.filterText(filterInput);
        page.setText(filteredOutput);
        return page;
    }
}

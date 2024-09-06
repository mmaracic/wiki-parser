package wiki.parser;

import wiki.parser.filter.MarkupFilter;
import wiki.parser.filter.WikiMarkupFilter;
import wiki.parser.model.WikiPage;
import wiki.parser.xml.XmlParser;

import javax.xml.stream.XMLStreamException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.List;

import static wiki.parser.xml.Tags.MEDIAWIKI;

public class WikiParser implements Parser<WikiPage> {

    private final MarkupFilter markupFilter = new WikiMarkupFilter();
    private final XmlParser xmlParser;

    public WikiParser(String filename) throws XMLStreamException, FileNotFoundException {
        xmlParser = new XmlParser(filename);
    }

    @Override
    public void close() throws XMLStreamException, IOException {
        xmlParser.close();
    }

    @Override
    public WikiPage readNext() throws XMLStreamException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        WikiPage page = xmlParser.readNext(WikiPage.class, new HashSet<>(List.of(MEDIAWIKI)));
        String filterInput = page.getText();
        String filteredOutput = markupFilter.filterText(filterInput);
        page.setText(filteredOutput);
        return page;
    }
}

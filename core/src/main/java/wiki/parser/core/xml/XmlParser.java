package wiki.parser.core.xml;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;

public interface XmlParser {

    void close() throws XMLStreamException, IOException;

    <T> T readNext(Class<T> c, Set<String> ignoredTags) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, XmlParserException, XMLStreamException, IOException;
}

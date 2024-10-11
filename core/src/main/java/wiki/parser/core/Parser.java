package wiki.parser.core;

import wiki.parser.core.xml.XmlParserException;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public interface Parser<T> {
    void close() throws XMLStreamException, IOException;

    T readNext() throws XMLStreamException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, IOException, XmlParserException;
}

package wiki.parser.core;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public interface Parser<T> {
    void close() throws XMLStreamException, IOException;

    T readNext() throws XMLStreamException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, IOException;
}

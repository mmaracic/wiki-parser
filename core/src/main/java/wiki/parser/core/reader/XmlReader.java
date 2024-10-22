package wiki.parser.core.reader;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;

public interface XmlReader {

    XMLEventReader getReader() throws XMLStreamException;

    long skipBytes(long n) throws IOException, XMLStreamException;

    void close() throws XMLStreamException, IOException;
}

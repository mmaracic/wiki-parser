package wiki.parser.core.reader;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.InputStream;

public interface XmlReader {

    InputStream getStream();

    long skipBytes(long n) throws IOException, XMLStreamException;

    void close() throws XMLStreamException, IOException;
}

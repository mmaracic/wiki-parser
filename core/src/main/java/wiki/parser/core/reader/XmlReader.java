package wiki.parser.core.reader;

import org.apache.commons.compress.compressors.CompressorException;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;

public interface XmlReader {

    XMLEventReader getReader() throws XMLStreamException, IOException, CompressorException;

    void close() throws XMLStreamException, IOException;
}

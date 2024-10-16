package wiki.parser.core.reader;

import org.apache.commons.compress.compressors.CompressorException;
import wiki.parser.core.stream.ByteRange;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.Set;

public interface XmlReader {

    XMLEventReader getReader() throws XMLStreamException, IOException, CompressorException;

    XMLEventReader getReader(Set<ByteRange> ranges) throws XMLStreamException, IOException, CompressorException;

    void close() throws XMLStreamException, IOException;
}

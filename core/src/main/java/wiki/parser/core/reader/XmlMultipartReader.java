package wiki.parser.core.reader;

import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import wiki.parser.core.stream.ByteRange;
import wiki.parser.core.stream.PositionedBoundedStream;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.*;
import java.util.Set;

public class XmlMultipartReader implements XmlReader {

    private final String filename;
    private final Boolean decompress;

    private InputStream inputStream;

    public XmlMultipartReader(String filename, Boolean decompress) {
        this.filename = filename;
        this.decompress = decompress;
    }

    @Override
    public XMLEventReader getReader() throws XMLStreamException, IOException, CompressorException {
        return getReader(Set.of(new ByteRange(0)));
    }

    @Override
    public XMLEventReader getReader(Set<ByteRange> ranges) throws XMLStreamException, IOException, CompressorException {
        if (this.inputStream != null) {
            inputStream.close();
        }
        this.inputStream = (decompress) ?
                new CompressorStreamFactory(true).createCompressorInputStream(
                        new BufferedInputStream(new PositionedBoundedStream(new FileInputStream(filename), ranges))
                ) :
                new BufferedInputStream(new PositionedBoundedStream(new FileInputStream(filename)));
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        return xmlInputFactory.createXMLEventReader(inputStream);
    }

    @Override
    public void close() throws IOException {
        if (this.inputStream != null) {
            inputStream.close();
        }
    }
}

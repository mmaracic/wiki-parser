package wiki.parser.core.reader;

import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import wiki.parser.core.stream.ByteRange;
import wiki.parser.core.stream.PositionedBoundedStream;

import javax.xml.stream.XMLStreamException;
import java.io.*;
import java.util.Set;

public class XmlMultipartReader implements XmlReader {

    private final InputStream inputStream;

    public XmlMultipartReader(String filename, Boolean decompress, Set<ByteRange> ranges) throws FileNotFoundException, CompressorException {
        this.inputStream = (decompress) ?
                new CompressorStreamFactory(true).createCompressorInputStream(
                        new BufferedInputStream(new PositionedBoundedStream(new FileInputStream(filename), ranges))
                ) :
                new PositionedBoundedStream(new FileInputStream(filename));
    }

    public XmlMultipartReader(String filename, Boolean decompress) throws CompressorException, FileNotFoundException {
        this(filename, decompress, Set.of(new ByteRange(0)));
    }

    @Override
    public InputStream getStream() {
        return inputStream;
    }

    @Override
    public long skipBytes(long n) throws IOException, XMLStreamException {
        long skipCount = inputStream.skip(n);
        return skipCount;
    }

    @Override
    public void close() throws IOException {
        if (this.inputStream != null) {
            inputStream.close();
        }
    }
}

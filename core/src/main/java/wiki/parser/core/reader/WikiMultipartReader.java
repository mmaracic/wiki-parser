package wiki.parser.core.reader;

import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import wiki.parser.core.model.WikiIndex;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class WikiMultipartReader implements XmlReader {

    private final Map<Long, List<WikiIndex>> indexMap;
    private final List<Long> indexOffsets;
    private final Boolean decompress;
    private int indexPosition = 0;

    private final BufferedInputStream bufferedInputStream;
    private long streamPosition = 0;

    public WikiMultipartReader(String filename, List<WikiIndex> indexList, Boolean decompress) throws FileNotFoundException {
        this.decompress = decompress;
        this.indexMap = indexList.stream().collect(Collectors.groupingBy(WikiIndex::getOffset));
        this.indexOffsets = indexMap.keySet().stream().sorted().toList();
        this.bufferedInputStream = new BufferedInputStream(new FileInputStream(filename));
    }

    @Override
    public XMLEventReader getReader() throws XMLStreamException, IOException, CompressorException {
        byte[] data = readBytes();
        InputStream inputStream = (decompress) ? new CompressorStreamFactory().createCompressorInputStream("bzip2", new ByteArrayInputStream(data)) : new ByteArrayInputStream(data);
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        return xmlInputFactory.createXMLEventReader(inputStream);
    }

    @Override
    public void close() throws IOException {
        bufferedInputStream.close();
    }

    private byte[] readBytes() throws IOException {
        if (indexPosition < indexOffsets.size() - 1) {
            Long startPosition = indexOffsets.get(indexPosition);
            Long endPosition = indexOffsets.get(indexPosition + 1);
            int len = (int) (endPosition - startPosition);
            byte[] buffer = new byte[len];
            if (streamPosition != startPosition) {
                long skipSize = startPosition - streamPosition;
                bufferedInputStream.skipNBytes(skipSize);
                streamPosition += skipSize;
            }
            indexPosition++;
            int size = bufferedInputStream.read(buffer, 0, len);
            streamPosition += size;
            if (len != size) {
                throw new IllegalStateException("Read " + " bytes into " + len + " buffer!");
            }
            return buffer;
        } else {
            return bufferedInputStream.readAllBytes();
        }
    }
}

package wiki.parser.core.reader;

import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorStreamFactory;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.*;

public class WikiFullReader implements XmlReader {

    private final XMLEventReader reader;
    private final InputStream inputStream;

    public WikiFullReader(String filename, Boolean decompress) throws FileNotFoundException, CompressorException, XMLStreamException {
        if (decompress) {
            inputStream = new CompressorStreamFactory().createCompressorInputStream(
                    new BufferedInputStream(new FileInputStream(filename))
            );
        } else {
            inputStream = new FileInputStream(filename);
        }
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        reader = xmlInputFactory.createXMLEventReader(inputStream);
    }

    @Override
    public XMLEventReader getReader() {
        return reader;
    }

    @Override
    public void close() throws XMLStreamException, IOException {
        reader.close();
        inputStream.close();
    }
}

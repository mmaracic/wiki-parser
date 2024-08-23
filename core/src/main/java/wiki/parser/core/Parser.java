package wiki.parser.core;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class Parser {

    private XMLEventReader reader;
    private InputStream inputStream;

    public Parser(String filename) throws FileNotFoundException, XMLStreamException {
        inputStream = new FileInputStream(filename);
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        reader = xmlInputFactory.createXMLEventReader(inputStream);
    }

    public void close() throws XMLStreamException, IOException {
        reader.close();
        inputStream.close();
    }

    public void readNext() {

    }
}

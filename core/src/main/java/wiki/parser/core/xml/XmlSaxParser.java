package wiki.parser.core.xml;

import lombok.extern.java.Log;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import wiki.parser.core.reader.XmlReader;

import javax.xml.namespace.QName;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.logging.Level;

//Reference
//https://www.baeldung.com/java-sax-parser

@Log
public class XmlSaxParser extends XmlParser {

    private final SAXParser saxParser;

    public XmlSaxParser(XmlReader xmlReader) throws ParserConfigurationException, SAXException {
        super(xmlReader);
        SAXParserFactory factory = SAXParserFactory.newInstance();

        saxParser = factory.newSAXParser();
    }

    @Override
    protected <T> void readNext(T object, Set<String> ignoredTags, XStreamFieldStack fieldStack) throws IOException {

        XmlHandler<T> handler = new XmlHandler<>(object, fieldStack, ignoredTags);
        try {
            saxParser.parse(xmlReader.getStream(), handler);
        } catch (SAXException ex) {
            if (!(ex.getException() != null && ex.getException() instanceof StopException)) {
                log.log(Level.SEVERE, ex.getMessage());
            }
        }
    }

    @Override
    public void close() throws XMLStreamException, IOException {
        xmlReader.close();
    }

    private class XmlHandler<T> extends DefaultHandler {

        private final T containerObject;
        private final XStreamFieldStack fieldStack;
        private final Set<String> ignoredTags;

        public XmlHandler(T containerObject, XStreamFieldStack fieldStack, Set<String> ignoredTags) {
            this.containerObject = containerObject;
            this.fieldStack = fieldStack;
            this.ignoredTags = ignoredTags;
        }

        @Override
        public void startDocument() throws SAXException {
            // no op
        }

        @Override
        public void endDocument() throws SAXException {
            // no op
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            if (!ignoredTags.contains(qName)) {
                fieldStack.push(new QName(qName));
                //log.info("Opening tag " + qName);
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if (!ignoredTags.contains(qName)) {
                fieldStack.pop(new QName(qName));
                //log.info("Closing tag " + qName);

                if (fieldStack.isEmpty() && fieldStack.getInstancesVisited() > 0) {
                    throw new SAXException(new StopException());
                }
            }
        }

        @Override
        public void characters(char ch[], int start, int length) throws SAXException {
            try {
                String value = new String(Arrays.copyOfRange(ch, start, start + length)).trim();
                if (!value.isEmpty()) {
                    setFields(fieldStack, containerObject, value);
                }
            } catch (IllegalAccessException e) {
                throw new XmlParserException(fieldStack, e);
            }
        }
    }

    private static class StopException extends Exception {
    }
}

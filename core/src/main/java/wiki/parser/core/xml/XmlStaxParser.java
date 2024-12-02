package wiki.parser.core.xml;

import lombok.extern.java.Log;
import wiki.parser.core.reader.XmlReader;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.*;
import java.io.IOException;
import java.util.*;

/**
 * Origin material: https://www.geeksforgeeks.org/stax-xml-parser-java/
 * Parser to parse large wiki files
 */
@Log
public class XmlStaxParser extends XmlParser {

    private final XMLEventReader xmlEventReader;

    public XmlStaxParser(XmlReader xmlReader) throws XMLStreamException {
        super(xmlReader);
        this.xmlEventReader = XMLInputFactory.newInstance().createXMLEventReader(xmlReader.getStream());
    }

    @Override
    public void close() throws XMLStreamException, IOException {
        xmlEventReader.close();
        xmlReader.close();
    }

    @Override
    protected  <T> void readNext(T object, Set<String> ignoredTags, XStreamFieldStack fieldStack) throws XMLStreamException, IllegalAccessException {

        while (xmlEventReader.hasNext() && fieldStack.getInstancesVisited() < 1) {
            // Event is actually the tag . It is of 3 types
            // <name> = StartEvent
            // </name> = EndEvent
            // data between the StartEvent and the EndEvent
            // which is Characters Event
            XMLEvent event = xmlEventReader.nextEvent();

            // This will trigger when the tag is of type <...>
            if (event.isStartElement()) {
                StartElement element = (StartElement) event;
                String elementName = element.getName().getLocalPart();
                //log.info("Start element: " + elementName + " in path " + fieldStack.toString());

                if (!ignoredTags.contains(elementName)) {
                    fieldStack.push(element.getName());
                }

            } else if (event.isEndElement()) {
                // This will be triggered when the tag is of type </...>
                EndElement element = (EndElement) event;
                String elementName = element.getName().getLocalPart();
                fieldStack.pop(element.getName());
                //log.info("End element: " + elementName + " in path " + fieldStack.toString());

            } else if (event.isCharacters()) {
                // Triggered when there is data after the tag which is currently opened.
                Characters element = (Characters) event;
                if (!element.getData().trim().isEmpty()) {
                    if (fieldStack.getMatchingFieldsCount() > 0) {
                        setFields(fieldStack, object, element.getData());
                    }
                    //log.info("Element " + fieldStack.toString() + " data: " + element.getData());
                }
            } else if (event.isStartDocument() || event.isNamespace()) {
                //skip
            } else {
                throw new IllegalArgumentException("Unsupported element " + event);
            }
        }
        //log.info("Parsing of object done");
    }
}
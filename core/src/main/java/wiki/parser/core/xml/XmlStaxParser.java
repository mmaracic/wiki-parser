package wiki.parser.core.xml;

import lombok.extern.java.Log;
import wiki.parser.annotation.WikiPath;
import wiki.parser.core.reader.XmlReader;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.*;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Origin material: https://www.geeksforgeeks.org/stax-xml-parser-java/
 * Parser to parse large wiki files
 */
@Log
public class XmlStaxParser implements XmlParser {

    private final XmlReader xmlReader;
    private final XMLEventReader xmlEventReader;

    public XmlStaxParser(XmlReader xmlReader) throws XMLStreamException {
        this.xmlReader = xmlReader;
        this.xmlEventReader = XMLInputFactory.newInstance().createXMLEventReader(xmlReader.getStream());
    }

    @Override
    public void close() throws XMLStreamException, IOException {
        xmlEventReader.close();
        xmlReader.close();
    }

    @Override
    public <T> T readNext(Class<T> c, Set<String> ignoredTags) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, XmlParserException, XMLStreamException, IOException {
        String classPath = analyzeClass(c);
        //key is path, value is attribute object field
        Map<String, Field> fields = analyzeClassFields(c);
        final XStreamFieldStack fieldStack = new XStreamFieldStack(classPath, fields);

        T object = c.getConstructor().newInstance();
        if (!fieldStack.isEmpty()) {
            throw new IllegalAccessException("Unexpected data end, stack: " + fieldStack);
        }
        readNext(xmlEventReader, object, ignoredTags, fieldStack);
        return object;
    }

    private <T> void readNext(XMLEventReader reader, T object, Set<String> ignoredTags, XStreamFieldStack fieldStack) throws XMLStreamException, IllegalAccessException {

        while (reader.hasNext() && fieldStack.getInstancesVisited() < 1) {
            // Event is actually the tag . It is of 3 types
            // <name> = StartEvent
            // </name> = EndEvent
            // data between the StartEvent and the EndEvent
            // which is Characters Event
            XMLEvent event = reader.nextEvent();

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
                        for (Field field : fieldStack.getMatchingFields()) {
                            var existingValue = field.get(object);
                            var newTextSection = element.getData();
                            field.set(object, (existingValue != null) ? existingValue + newTextSection : newTextSection);
                        }
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

    private Map<QName, String> extractAttributes(StartElement element) {
        // Iterator for accessing the metadeta related the tag started.
        Map<QName, String> attributes = new HashMap<>(5);
        Iterator<Attribute> iterator = element.getAttributes();
        while (iterator.hasNext()) {
            Attribute attribute = iterator.next();
            QName name = attribute.getName();
            String value = attribute.getValue();
            attributes.put(name, value);
        }
        return attributes;
    }

    private String analyzeClass(Class<?> clazz) {
        if (clazz.isAnnotationPresent(WikiPath.class)) {
            //Annotation postprocessor ensures the class has a path
            WikiPath annotation = clazz.getAnnotation(WikiPath.class);
            return annotation.path();
        }
        return null;
    }

    private Map<String, Field> analyzeClassFields(Class<?> clazz) {
        Map<String, Field> wikiProperties = new HashMap<>();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(WikiPath.class)) {
                //Annotation postprocessor ensures the field is a string
                WikiPath annotation = field.getAnnotation(WikiPath.class);
                wikiProperties.put(annotation.path(), field);
            }
        }
        return wikiProperties;
    }
}
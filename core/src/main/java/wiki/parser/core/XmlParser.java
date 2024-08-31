package wiki.parser.core;

import lombok.extern.java.Log;
import wiki.parser.annotation.WikiPath;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Origin material: https://www.geeksforgeeks.org/stax-xml-parser-java/
 * Parser to parse large wiki files
 */
@Log
public class XmlParser {

    private final XMLEventReader reader;
    private final InputStream inputStream;

    public XmlParser(String filename) throws FileNotFoundException, XMLStreamException {
        inputStream = new FileInputStream(filename);
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        reader = xmlInputFactory.createXMLEventReader(inputStream);
    }

    public void close() throws XMLStreamException, IOException {
        reader.close();
        inputStream.close();
    }

    public <T> T readNext(Class<T> c, Set<String> ignoredTags) throws XMLStreamException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        //key is path, value is attribute object field
        Map<String, Field> fields = analyzeClass(c);
        final XStreamFieldStack fieldStack = new XStreamFieldStack(fields);

        T object = c.getConstructor().newInstance();
        if (!fieldStack.isEmpty()) {
            throw new IllegalAccessException("Unexpected data end, stack: " + fieldStack);
        }

        readNext(object, ignoredTags, fieldStack);
        return object;
    }

    private <T> void readNext(T object, Set<String> ignoredTags, XStreamFieldStack fieldStack) throws XMLStreamException, IllegalAccessException {

        while (reader.hasNext() && (fieldStack.getMaxDepth() == 0 || !fieldStack.isEmpty())) {
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
                    if (fieldStack.getMatchingFieldsCount()>0) {
                        for(Field field: fieldStack.getMatchingFields()) {
                            field.set(object, element.getData());
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
        log.info("Parsing of object done");
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

    private Map<String, Field> analyzeClass(Class<?> clazz) {
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
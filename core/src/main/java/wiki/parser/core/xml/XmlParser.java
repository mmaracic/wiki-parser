package wiki.parser.core.xml;

import org.xml.sax.SAXException;
import wiki.parser.annotation.WikiPath;
import wiki.parser.core.reader.XmlReader;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public abstract class XmlParser {

    protected final XmlReader xmlReader;

    protected XmlParser(XmlReader xmlReader) {
        this.xmlReader = xmlReader;
    }

    public <T> T readNext(Class<T> c, Set<String> ignoredTags) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, XMLStreamException, IOException, SAXException {
        String classPath = analyzeClass(c);
        //key is path, value is attribute object field
        Map<String, Field> fields = analyzeClassFields(c);
        final XStreamFieldStack fieldStack = new XStreamFieldStack(classPath, fields);

        T object = c.getConstructor().newInstance();
        if (!fieldStack.isEmpty()) {
            throw new IllegalAccessException("Unexpected data end, stack: " + fieldStack);
        }
        readNext(object, ignoredTags, fieldStack);
        return object;
    }

    protected abstract <T> void readNext(T object, Set<String> ignoredTags, XStreamFieldStack fieldStack) throws XMLStreamException, IllegalAccessException, IOException, SAXException;

    public abstract void close() throws XMLStreamException, IOException;

    protected <T> void setFields(XStreamFieldStack fieldStack, T object, String newValue) throws IllegalAccessException {
        for (Field field : fieldStack.getMatchingFields()) {
            var existingValue = field.get(object);
            field.set(object, (existingValue != null) ? existingValue + newValue : newValue);
        }
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

}

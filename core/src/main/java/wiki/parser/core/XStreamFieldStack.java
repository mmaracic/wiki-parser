package wiki.parser.core;

import javax.xml.namespace.QName;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class XStreamFieldStack implements XStreamStack {

    private final Map<String, Field> initFields;

    private final XStreamPath xPath = new XStreamPath();
    private final List<Set<Map.Entry<String, Field>>> fieldsQualifyAtPathLevel = new ArrayList<>(10);
    private final List<Set<Map.Entry<String, Field>>> fieldsMatchAtPathLevel = new ArrayList<>(10);

    public XStreamFieldStack(Map<String, Field> initFields) {
        this.initFields = initFields;
    }


    @Override
    public boolean push(QName element) {
        Set<Map.Entry<String, Field>> levelInput = (xPath.isEmpty()) ? initFields.entrySet() : fieldsQualifyAtPathLevel.getLast();

        xPath.push(element);
        fieldsQualifyAtPathLevel.addLast(filterQualifyingFields(xPath, levelInput));
        fieldsMatchAtPathLevel.addLast(filterMatchingFields(xPath, levelInput));
        return true;
    }

    @Override
    public boolean pop(QName element) {
        fieldsQualifyAtPathLevel.removeLast();
        fieldsMatchAtPathLevel.removeLast();
        return xPath.pop(element);
    }

    @Override
    public int getMaxDepth() {
        return xPath.getMaxDepth();
    }

    @Override
    public boolean isEmpty() {
        return xPath.isEmpty();
    }

    @Override
    public int size() {
        return xPath.size();
    }

    @Override
    public String toString() {
        return xPath.toString();
    }

    public Set<Field> getMatchingFields() {
        return fieldsMatchAtPathLevel.getLast().stream().map(Map.Entry::getValue).collect(Collectors.toSet());
    }

    public int getMatchingFieldsCount() {
        return fieldsMatchAtPathLevel.getLast().size();
    }
    private static Set<Map.Entry<String, Field>> filterQualifyingFields(XStreamPath currentPath, Set<Map.Entry<String, Field>> input) {
        return input.stream().filter((it) -> currentPath.match(it.getKey()) > 0).collect(Collectors.toSet());
    }

    private static Set<Map.Entry<String, Field>> filterMatchingFields(XStreamPath currentPath, Set<Map.Entry<String, Field>> input) {
        return input.stream().filter((it) -> currentPath.match(it.getKey()) == 1f).collect(Collectors.toSet());
    }
}

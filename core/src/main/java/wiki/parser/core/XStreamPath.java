package wiki.parser.core;

import javax.xml.namespace.QName;
import java.util.*;
import java.util.stream.Collectors;

public class XStreamPath implements XStreamStack {

    private final List<QName> orderedPath = new ArrayList<>(10);
    private final Map<QName, Integer> unorderedPath = new HashMap<>();
    private int maxDepth = 0;

    @Override
    public boolean push(QName element) {
        maxDepth++;
        orderedPath.addLast(element);
        Integer unorderedElementCount = unorderedPath.getOrDefault(element, 0);
        return unorderedPath.put(element, unorderedElementCount + 1) != null;
    }

    @Override
    public boolean pop(QName input) {
        if (orderedPath.isEmpty()) {
            throw new IllegalStateException("Trying to pop from an empty path");
        } else {
            QName element = orderedPath.removeLast();
            if (!element.equals(input)) {
                throw new IllegalStateException("Trying to do illegal pop, popping " + element + " instead " + input);
            }
            int unorderedElementCount = unorderedPath.getOrDefault(element, 0);
            if (unorderedElementCount == 1) {
                unorderedPath.remove(element);
            } else if (unorderedElementCount > 1) {
                unorderedPath.put(element, unorderedElementCount - 1);
            } else {
                throw new IllegalStateException("Trying to do illegal pop for count: " + unorderedElementCount + " and element " + element);
            }
            return true;
        }
    }

    @Override
    public int getMaxDepth() {
        return maxDepth;
    }

    @Override
    public boolean isEmpty() {
        return orderedPath.isEmpty();
    }

    public boolean contains(QName element) {
        return unorderedPath.containsKey(element);
    }

    @Override
    public int size() {
        return orderedPath.size();
    }

    @Override
    public String toString() {
        return orderedPath.stream().map(QName::getLocalPart).collect(Collectors.joining("."));
    }

    /**
     * Degree of match between this XStream path and input - simple '.' delimited string
     *
     * @param rawPath String path to which this path is being matched, components delimited by '.'
     * @return Match degree, from 0f (no match) to 1f (full match)
     */
    public float match(String rawPath) {
        String[] rawPathElements = rawPath.split("\\.");
        int minLength = Math.min(this.size(), rawPathElements.length);
        int maxLength = Math.max(this.size(), rawPathElements.length);
        for (int i = 0; i < minLength; i++) {
            String rawElement = rawPathElements[i];
            String pathElement = this.orderedPath.get(i).getLocalPart();
            if (!rawElement.equals(pathElement)) {
                return 0;
            }
        }
        return minLength / (float) maxLength;
    }

    public boolean equals(String rawPath) {
        return this.match(rawPath) == 1f;
    }
}

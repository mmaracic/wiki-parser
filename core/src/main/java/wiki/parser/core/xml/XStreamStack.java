package wiki.parser.core.xml;

import javax.xml.namespace.QName;

public interface XStreamStack {
    boolean push(QName element);

    boolean pop(QName element);

    int getMaxDepth();

    boolean isEmpty();

    int size();

    String toString();
}

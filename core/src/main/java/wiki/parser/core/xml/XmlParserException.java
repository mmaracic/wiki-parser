package wiki.parser.core.xml;

public class XmlParserException extends Exception {

    XmlParserException(XStreamStack path, Throwable cause) {
        super(path.toString(), cause);
    }
}

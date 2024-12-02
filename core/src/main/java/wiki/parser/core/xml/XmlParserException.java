package wiki.parser.core.xml;

public class XmlParserException extends RuntimeException {

    XmlParserException(XStreamFieldStack fieldStack, Throwable cause) {
        super(fieldStack.toString(), cause);
    }
}

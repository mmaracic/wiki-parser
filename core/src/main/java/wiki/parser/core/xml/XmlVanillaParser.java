package wiki.parser.core.xml;

import lombok.extern.java.Log;
import org.xml.sax.SAXException;
import wiki.parser.core.reader.XmlReader;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Log
public class XmlVanillaParser extends XmlParser {

    private final BufferedScanner scanner;

    public XmlVanillaParser(XmlReader xmlReader, int bufferSize) {
        super(xmlReader);
        this.scanner = new BufferedScanner(xmlReader.getStream(), bufferSize);
    }

    @Override
    protected <T> void readNext(T object, Set<String> ignoredTags, XStreamFieldStack fieldStack) throws XMLStreamException, IllegalAccessException, IOException, SAXException {
        while (true) {
            var data = scanner.readNext();
            if (data == null) {
                throw new IllegalStateException("End of stream reached before object was finished!");
            } else {
                if (data.type == BufferedScanner.DataType.TAG_OPEN && !ignoredTags.contains(data.data)) {
                    fieldStack.push(new QName(data.data));
                    //log.info("Opening tag " + data.data);
                } else if (data.type == BufferedScanner.DataType.TAG_CLOSED && !ignoredTags.contains(data.data)) {
                    var result = fieldStack.pop(new QName(data.data));
                    //log.info("Closing tag " + data.data);
                    if (fieldStack.isEmpty() && fieldStack.getInstancesVisited() > 0) {
                        break;
                    }
                } else if (data.type == BufferedScanner.DataType.TEXT) {
                    var cleanedData = data.data.trim();
                    if (!cleanedData.isEmpty()) {
                        setFields(fieldStack, object, data.data);
                        //log.info("Data: " + cleanedData);
                    }

                }
            }
        }
    }

    @Override
    public void close() throws XMLStreamException, IOException {
        xmlReader.close();
    }

    public int getBufferPosition() {
        return this.scanner.getBufferPosition();
    }

    private static class BufferedScanner {
        private final int bufferSize;
        private final int[] buffer;

        private int bufferLastReadLocation = -1;
        private ScannerState state = ScannerState.TEXT;

        private final InputStream is;

        public BufferedScanner(InputStream is, int bufferSize) {
            this.bufferSize = bufferSize;
            this.buffer = new int[bufferSize];
            this.is = is;
        }

        public Data readNext() throws IOException {
            while (true) {
                int b = is.read();
                if (b == -1) {
                    if (bufferLastReadLocation > -1) {
                        String data = getBufferData();
                        resetBuffer();
                        if (state == ScannerState.TEXT) {
                            return new Data(data, DataType.TEXT, List.of());
                        } else {
                            return parseTag(data);
                        }
                    } else {
                        return null;
                    }
                }
                if (bufferLastReadLocation >= bufferSize - 1) {
                    throw new IllegalStateException("Buffer capacity reached: " + bufferSize + " characters");
                }
                if (state == ScannerState.TEXT && b == '<') {
                    state = ScannerState.TAG;
                    if (bufferLastReadLocation > -1) {
                        String data = getBufferData();
                        resetBuffer();
                        return new Data(data, DataType.TEXT, List.of());
                    }
                } else if (state == ScannerState.TAG && b == '>') {
                    state = ScannerState.TEXT;
                    if (bufferLastReadLocation > -1) {
                        String data = getBufferData();
                        resetBuffer();
                        return parseTag(data);
                    }
                } else {
                    bufferLastReadLocation++;
                    buffer[bufferLastReadLocation] = b;
                }
            }
        }

        public void reset() {
            resetBuffer();
            this.state = ScannerState.TEXT;
        }

        public int getBufferPosition() {
            return bufferLastReadLocation;
        }

        private void resetBuffer() {
            this.bufferLastReadLocation = -1;
        }

        private String getBufferData() {
            return new String(buffer, 0, bufferLastReadLocation + 1);
        }

        private Data parseTag(String data) {
            var segments = new ArrayList<>(Arrays.stream(data.split("\\s+")).toList());
            var tag = segments.removeFirst();
            if (!segments.isEmpty() && segments.getLast().equals("/")) {
                segments.removeLast();
            }
            if (tag.charAt(0) == '/') {
                return new Data(tag.substring(1), DataType.TAG_CLOSED, segments);
            } else if (data.charAt(data.length() - 1) == '/') {
                return new Data(tag, DataType.TAG_OPEN_CLOSED, segments);
            } else {
                return new Data(tag, DataType.TAG_OPEN, segments);
            }
        }

        public record Data(String data, DataType type, List<String> attributes) {
        }

        private enum ScannerState {
            TEXT,
            TAG
        }

        private enum DataType {
            TEXT,
            TAG_OPEN,
            TAG_CLOSED,
            TAG_OPEN_CLOSED
        }
    }
}

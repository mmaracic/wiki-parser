package wiki.parser.core.stream;

import org.apache.tools.ant.filters.StringInputStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Set;

public class PositionedBoundedStreamTest {

    @Test
    public void testReadWholeStream() throws IOException {
        String testString = "Today is a nice and sunny day";
        StringInputStream sinStream = new StringInputStream(testString);
        PositionedBoundedStream stream = new PositionedBoundedStream(sinStream);
        String readString = new String(stream.readAllBytes());
        Assertions.assertEquals(testString, readString);
    }

    @Test
    public void testReadFromBeginning() throws IOException {
        String testString = "Today is a nice and sunny day";
        StringInputStream sinStream = new StringInputStream(testString);
        PositionedBoundedStream stream = new PositionedBoundedStream(sinStream, Set.of(new ByteRange(0, 2)));
        String readString = new String(stream.readAllBytes());
        Assertions.assertEquals("To", readString);
    }

    @Test
    public void testReadFromMiddle() throws IOException {
        String testString = "Today is a nice and sunny day";
        StringInputStream sinStream = new StringInputStream(testString);
        PositionedBoundedStream stream = new PositionedBoundedStream(sinStream, Set.of(new ByteRange(6, 8)));
        String readString = new String(stream.readAllBytes());
        Assertions.assertEquals("is", readString);
    }

    @Test
    public void testReadFromMultipleRanges() throws IOException {
        String testString = "Today is a nice and sunny day";
        StringInputStream sinStream = new StringInputStream(testString);
        PositionedBoundedStream stream = new PositionedBoundedStream(sinStream, Set.of(new ByteRange(6, 9), new ByteRange(11, 15)));
        String readString = new String(stream.readAllBytes());
        Assertions.assertEquals("is nice", readString);
    }

    @Test
    public void testReadFromMultipleOverlappingRanges() throws IOException {
        String testString = "Today is a nice and sunny day";
        StringInputStream sinStream = new StringInputStream(testString);
        PositionedBoundedStream stream = new PositionedBoundedStream(sinStream, Set.of(new ByteRange(6, 11), new ByteRange(11, 15)));
        String readString = new String(stream.readAllBytes());
        Assertions.assertEquals("is a nice", readString);
    }

    @Test
    public void testReadFromMultipleOverlappingRanges2() throws IOException {
        String testString = "Today is a nice and sunny day";
        StringInputStream sinStream = new StringInputStream(testString);
        PositionedBoundedStream stream = new PositionedBoundedStream(sinStream, Set.of(new ByteRange(6, 11), new ByteRange(10, 15)));
        String readString = new String(stream.readAllBytes());
        Assertions.assertEquals("is a nice", readString);
    }
}

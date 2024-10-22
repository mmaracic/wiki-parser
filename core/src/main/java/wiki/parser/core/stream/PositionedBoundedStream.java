package wiki.parser.core.stream;

import lombok.Getter;
import lombok.extern.java.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;

@Log
public class PositionedBoundedStream extends InputStream {

    private final InputStream in;

    //minByte is included, maxByte is excluded when positive or ignored if the value is -1
    private final List<ByteRange> rangeList;

    @Getter
    private long position = 0; //positioned on the first byte that was not read (or after last byte if all bytes were read)
    private int rangePosition; //last know range  into which position could fit

    public PositionedBoundedStream(InputStream in, Set<ByteRange> rangeList) {
        if (in == null) {
            throw new IllegalArgumentException("Input stream can not be null");
        }
        if (rangeList == null || rangeList.isEmpty()) {
            throw new IllegalArgumentException("Range list has to be a list with elements in it");
        }
        this.in = in;
        this.rangeList = rangeList.stream().sorted((a, b) -> Long.compare(b.minByte(), a.minByte())).toList();
        this.rangePosition = (this.rangeList.getFirst().contains(position)) ? 0 : -1;
    }

    public PositionedBoundedStream(InputStream in) {
        this(in, Set.of(new ByteRange(0)));
    }

    @Override
    public int read() throws IOException {
        //log.info("Position: " + position);
        long skipBytes = skipToValidPosition(position);
        if (skipBytes == -1) {
            return -1;
        } else if (skipBytes > 0) {
            in.skipNBytes(skipBytes);
            rangePosition++;
            position += skipBytes;
        }
        int b = in.read();
        if (b != -1) {
            position++;
        }
        return b;
    }

    /**
     * Return number of bytes to skip to get to valid stream position
     *
     * @return =0, position is valid, byte can be read
     * -1, there are no more valid positions, all valid positions have been read
     * >0, number of bytes to skip to get to next valid position
     */
    private long skipToValidPosition(long position) {
        ByteRange currentRange = (rangePosition >= 0) ? rangeList.get(rangePosition) : null;
        ByteRange nextRange = (rangePosition >= 0 && rangePosition < rangeList.size() - 1) ? rangeList.get(rangePosition) : null;
        if (currentRange != null && currentRange.contains(position)) {
            return 0;
        } else if (currentRange == null) {
            return rangeList.getFirst().minByte() - position;
        } else if (nextRange == null) {
            return -1;
        } else {
            return nextRange.minByte() - position;
        }
    }
}

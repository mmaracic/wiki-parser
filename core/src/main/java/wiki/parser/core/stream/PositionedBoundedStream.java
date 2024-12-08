package wiki.parser.core.stream;

import lombok.Getter;
import lombok.extern.java.Log;
import wiki.parser.core.util.Pair;

import java.io.IOException;
import java.io.InputStream;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Log
public class PositionedBoundedStream extends InputStream {

    private final InputStream in;

    //minByte is included, maxByte is excluded when positive or ignored if the value is -1
    private final List<ByteRange> rangeList;

    @Getter
    private long position = 0; //positioned on the first byte that was not read (or after last byte if all bytes were read)
    private int rangePosition; //last known range  into which position could fit

    public PositionedBoundedStream(InputStream in, Set<ByteRange> rangeList) {
        if (in == null) {
            throw new IllegalArgumentException("Input stream can not be null");
        }
        if (rangeList == null || rangeList.isEmpty()) {
            throw new IllegalArgumentException("Range list has to be a list with elements in it");
        }
        this.in = in;
        this.rangeList = rangeList.stream().sorted(Comparator.comparingLong(ByteRange::minByte)).toList();
        this.rangePosition = (this.rangeList.getFirst().contains(position)) ? 0 : -1;
        //log.info("Range list: " + this.rangeList);
    }

    public PositionedBoundedStream(InputStream in) {
        this(in, Set.of(new ByteRange(0)));
    }

    @Override
    public int read() throws IOException {
        //log.info("Position: " + position);
        Pair<Long, Boolean> skipBytes = skipToValidPosition(position);
        //log.info("Skip bytes: " + skipBytes);
        if (skipBytes.first() == -1) {
            return -1;
        } else if (skipBytes.first() > 0) {
            in.skipNBytes(skipBytes.first());
            //log.info("Range position before: " + rangePosition);
            if (skipBytes.second()) {
                rangePosition++;
            }
            //log.info("Range position after: " + rangePosition);
            position += skipBytes.first();
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
     * @return Pair<BytesToSkip, MoveToNextRange>
     * <p>
     * BytesToSkip meaning:
     * =0, position is valid, byte can be read
     * -1, there are no more valid positions, all valid positions have been read
     * >0, number of bytes to skip to get to next valid position
     * <p>
     * MoveToNextRange meaning:
     * false - do not skip, stay in current range
     * true - skip to next range
     */
    private Pair<Long, Boolean> skipToValidPosition(long position) {
        ByteRange currentRange = (rangePosition >= 0) ? rangeList.get(rangePosition) : null;
        ByteRange nextRange = (rangePosition >= 0 && rangePosition < rangeList.size() - 1) ? rangeList.get(rangePosition + 1) : null;
        if (currentRange != null && currentRange.contains(position)) {
            return new Pair<>(0L, false);
        } else if (currentRange == null) {
            return new Pair<>(rangeList.getFirst().minByte() - position, true);
        } else if (nextRange != null && position < nextRange.minByte()) {
            return new Pair<>(nextRange.minByte() - position, true);
        } else if (nextRange != null && nextRange.contains(position)) {
            return new Pair<>(0L, true);
        } else {
            return new Pair<>(-1L, false);
        }
    }
}

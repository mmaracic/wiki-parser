package wiki.parser.core.stream;

//minByte is included, maxByte is excluded when positive or ignored if the value is -1
public record ByteRange(long minByte, long maxByte) {

    public ByteRange(long minByte) {
        this(minByte, -1);
    }

    boolean contains(long index) {
        return index >= minByte && (index < maxByte || maxByte == -1);
    }
}

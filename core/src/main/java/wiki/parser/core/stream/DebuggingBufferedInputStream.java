package wiki.parser.core.stream;

import lombok.extern.java.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

@Log
public class DebuggingBufferedInputStream extends BufferedInputStream {

    public DebuggingBufferedInputStream(InputStream in) {
        super(in);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int readBytes = super.read(b, off, len);
        log.info("Read buffer: " + new String(Arrays.copyOfRange(b, off, readBytes)));
        return readBytes;
    }
}
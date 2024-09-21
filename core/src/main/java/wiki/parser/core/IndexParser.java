package wiki.parser.core;

import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import wiki.parser.core.model.WikiIndex;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IndexParser {

    private final InputStream inputStream;
    private final BufferedReader reader;

    public IndexParser(String filename) throws IOException, CompressorException {
        //using BufferedInputStream after FileInputStream resolved Mark is not supported
        inputStream = new CompressorStreamFactory().createCompressorInputStream(
                new BufferedInputStream(new FileInputStream(filename))
        );
        reader = new BufferedReader(new InputStreamReader(inputStream));
    }

    public void close() throws IOException {
        inputStream.close();
        reader.close();
    }

    public List<WikiIndex> readAll() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, IOException {
        List<WikiIndex> index = new ArrayList<>(1000);
        String line = reader.readLine();
        while (line != null && !line.isBlank()) {
            var parts = Arrays.stream(line.split(":")).toList();
            WikiIndex entry = null;
            if (parts.size() >= 3) {
                entry = WikiIndex.builder()
                        .offset(Long.parseLong(parts.get(0)))
                        .pageId(Long.parseLong(parts.get(1)))
                        .title(parts.subList(2, parts.size())).build();
            } else {
                throw new IllegalStateException("Each index entry has to have 3 or more components, this one has " + parts.size());
            }
            index.add(entry);
            line = reader.readLine();
        }
        return index;
    }
}

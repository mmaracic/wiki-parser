package wiki.parser.core.util;

import java.util.Arrays;
import java.util.List;

public record StringList(List<String> content) {
    public StringList(List<String> content) {
        this.content = List.copyOf(content);
    }

    public StringList(String... content) {
        this(Arrays.stream(content).toList());
    }
}
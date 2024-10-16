package wiki.parser.core;

import wiki.parser.core.model.WikiIndex;
import wiki.parser.core.stream.ByteRange;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class WikiIndexTransformer {

    private final List<Long> indexOffsets;

    public WikiIndexTransformer(List<WikiIndex> indexList) {
        Map<Long, List<WikiIndex>> indexMap = indexList.stream().collect(Collectors.groupingBy(WikiIndex::getOffset));
        this.indexOffsets = indexMap.keySet().stream().sorted().toList();

    }

    public Set<ByteRange> toRange(List<WikiIndex> pageList) {
        return pageList.stream().map(WikiIndex::getOffset).map((it) -> {
            int minIndex = indexOffsets.indexOf(it);
            boolean hasMax = minIndex > -1 && minIndex < indexOffsets.size() - 1;
            long maxByte = (hasMax) ? indexOffsets.get(minIndex + 1) : -1;
            return new ByteRange(it, maxByte);
        }).collect(Collectors.toSet());
    }
}

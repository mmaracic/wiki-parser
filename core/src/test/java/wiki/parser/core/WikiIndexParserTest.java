package wiki.parser.core;

import lombok.extern.java.Log;
import org.apache.commons.compress.compressors.CompressorException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import wiki.parser.core.model.WikiIndex;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

@Log
public class WikiIndexParserTest {

    @Test
    public void parseIndex() throws IOException, CompressorException {
        var parser = new WikiIndexParser("./src/test/resources/simplewiki-20240901-pages-articles-multistream-index.txt.bz2");
        List<WikiIndex> index = parser.readAll();
        Assertions.assertFalse(index.isEmpty());
        log.info("Index count: " + index.size());
        var titleExclusionList = List.of("Wikipedia", "Wikimedia", "File", "Template");
        //Title has 10 entries
        var maxTitle = index.stream().map(WikiIndex::getTitle).max(Comparator.comparingInt((it) -> it.content().size()));
        log.info("Maximum title: " + maxTitle);
        log.info("Maximum title components: " + maxTitle.get().content().size());
        var maxTitleSkipWikipediaSource = index.stream()
                .map(WikiIndex::getTitle).filter((it) -> !titleExclusionList.contains(it.content().getFirst()))
                .max(Comparator.comparingInt((it) -> it.content().size()));
        //Title now has 4 entries
        log.info("Maximum title components skipping Wikipedia entries: " + maxTitleSkipWikipediaSource.get().content().size());
        //Count articles
        var articlesCount = index.stream()
                .map(WikiIndex::getTitle).filter((it) -> !titleExclusionList.contains(it.content().getFirst())).count();
        log.info("Articles count: " + articlesCount);
    }
}

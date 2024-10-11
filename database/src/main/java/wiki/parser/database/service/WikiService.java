package wiki.parser.database.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wiki.parser.core.model.WikiIndex;
import wiki.parser.core.model.WikiPage;
import wiki.parser.database.model.WikiIndexEntity;
import wiki.parser.database.model.WikiPageEntity;
import wiki.parser.database.model.WikiSourceEntity;
import wiki.parser.database.repository.WikiPageRepository;
import wiki.parser.database.repository.WikiSourceRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class WikiService {

    private final WikiSourceRepository wikiSourceRepository;
    private final WikiPageRepository wikiPageRepository;

    public List<WikiPageEntity> storePages(WikiSourceEntity wikiSourceEntity, Set<WikiPage> pages) {

        var pageEntities = pages.stream().map((it) -> WikiPageEntity.builder().page(it).source(wikiSourceEntity).build()).collect(Collectors.toSet());
        return wikiPageRepository.saveAll(pageEntities);
    }

    public WikiSourceEntity storeSource(String source, List<WikiIndex> index) {

        WikiSourceEntity wikiSourceEntity = wikiSourceRepository.save(WikiSourceEntity.builder().source(source).build());
        var indexEntities = index.stream().map((it) -> WikiIndexEntity.builder().index(it).source(wikiSourceEntity).build()).collect(Collectors.toSet());
        wikiSourceEntity.setIndexes(indexEntities);
        return wikiSourceEntity;
    }
}

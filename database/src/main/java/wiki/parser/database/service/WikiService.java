package wiki.parser.database.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wiki.parser.core.model.WikiPage;
import wiki.parser.database.model.WikiPageEntity;
import wiki.parser.database.model.WikiSourceEntity;
import wiki.parser.database.repository.WikiSourceRepository;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class WikiService {

    private final WikiSourceRepository wikiSourceRepository;

    public void storePages(String source, Set<WikiPage> pages) {

        WikiSourceEntity wikiSourceEntity = WikiSourceEntity.builder().source(source).build();
        wikiSourceRepository.save(wikiSourceEntity);
        var pageEntities = pages.stream().map((it) -> WikiPageEntity.builder().page(it).source(wikiSourceEntity).build()).collect(Collectors.toSet());
        wikiSourceEntity.setPages(pageEntities);
    }
}

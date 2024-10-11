package wiki.parser.database.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import wiki.parser.core.model.WikiIndex;
import wiki.parser.core.util.StringList;
import wiki.parser.database.AbstractDatabaseTest;
import wiki.parser.database.DatabaseConfiguration;
import wiki.parser.core.model.WikiPage;
import wiki.parser.database.model.WikiSourceEntity;
import wiki.parser.database.repository.WikiPageRepository;
import wiki.parser.database.repository.WikiSourceRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@DataJpaTest
@ContextConfiguration(classes = DatabaseConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class WikiServiceTest extends AbstractDatabaseTest {

    @Autowired
    private WikiSourceRepository wikiSourceRepository;

    @Autowired
    private WikiPageRepository wikiPageRepository;

    @Autowired
    private WikiService wikiService;


    @Test
    public void contextTest() {

    }

    @Test
    public void storePageTest() {
        Set<WikiPage> pages = new HashSet<>();
        pages.add(WikiPage.builder().title("title1").revision("1").text("Testing text 1").build());
        pages.add(WikiPage.builder().title("title2").revision("2").text("Testing text 2").build());

        WikiSourceEntity source = wikiService.storeSource("Test source", List.of());
        wikiService.storePages(source, pages);

        var dbSources = wikiSourceRepository.findAll();
        Assertions.assertEquals(1, dbSources.size());
        Assertions.assertEquals(1, dbSources.getFirst().getId());
        Assertions.assertTrue(dbSources.getFirst().getIndexes().isEmpty());
        var dbPages = wikiPageRepository.findAll();
        Assertions.assertEquals(2, dbPages.size());
        Assertions.assertEquals(1, dbPages.getFirst().getId());
        Assertions.assertEquals("title2", dbPages.getFirst().getPage().getTitle());
        Assertions.assertEquals("Test source", dbPages.getFirst().getSource().getSource());
        Assertions.assertEquals(2, dbPages.get(1).getId());
        Assertions.assertEquals("title1", dbPages.get(1).getPage().getTitle());
        Assertions.assertEquals("Test source", dbPages.get(1).getSource().getSource());

    }

    @Test
    public void storeIndexTest() {
        List<WikiIndex> indexes = new ArrayList<>();
        indexes.add(WikiIndex.builder().offset(10L).pageId(11L).title(new StringList("title1", "subtitle1")).build());
        indexes.add(WikiIndex.builder().offset(20L).pageId(21L).title(new StringList("title2", "subtitle2")).build());

        wikiService.storeSource("Test source", indexes);

        var dbSources = wikiSourceRepository.findAll();
        Assertions.assertEquals(1, dbSources.size());
        Assertions.assertNotNull(dbSources.getFirst().getId());
        Assertions.assertFalse(dbSources.getFirst().getIndexes().isEmpty());
        var dbIndexes = dbSources.getFirst().getIndexes().stream().toList();
        Assertions.assertEquals(2, dbIndexes.size());

        Assertions.assertEquals(1, dbIndexes.getFirst().getId());
        Assertions.assertEquals(20L, dbIndexes.getFirst().getIndex().getOffset());
        Assertions.assertEquals(2, dbIndexes.getFirst().getIndex().getTitle().content().size());
        Assertions.assertEquals("title2", dbIndexes.getFirst().getIndex().getTitle().content().getFirst());
        Assertions.assertEquals("subtitle2", dbIndexes.getFirst().getIndex().getTitle().content().getLast());

        Assertions.assertEquals(2, dbIndexes.getLast().getId());
        Assertions.assertEquals(10L, dbIndexes.getLast().getIndex().getOffset());
        Assertions.assertEquals(2, dbIndexes.getLast().getIndex().getTitle().content().size());
        Assertions.assertEquals("title1", dbIndexes.getLast().getIndex().getTitle().content().getFirst());
        Assertions.assertEquals("subtitle1", dbIndexes.getLast().getIndex().getTitle().content().getLast());
    }

}

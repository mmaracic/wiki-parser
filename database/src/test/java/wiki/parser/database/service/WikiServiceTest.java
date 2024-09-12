package wiki.parser.database.service;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import wiki.parser.database.DatabaseConfiguration;
import wiki.parser.core.model.WikiPage;
import wiki.parser.database.repository.WikiPageRepository;
import wiki.parser.database.repository.WikiSourceRepository;

import java.util.HashSet;
import java.util.Set;

@DataJpaTest
@ContextConfiguration(classes = DatabaseConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class WikiServiceTest {

    @Autowired
    private WikiSourceRepository wikiSourceRepository;

    @Autowired
    private WikiPageRepository wikiPageRepository;

    @Autowired
    private WikiService wikiService;


    @BeforeAll
    static void beforeAll() {
        DatabaseConfiguration.postgresTestContainer.start();
    }

    @AfterAll
    static void afterAll() {
        DatabaseConfiguration.postgresTestContainer.stop();
    }

    @Test
    public void contextTest() {

    }

    @Test
    public void simpleStoreTest() {
        Set<WikiPage> pages = new HashSet<>();
        pages.add(WikiPage.builder().title("title1").revision("1").text("Testing text 1").build());
        pages.add(WikiPage.builder().title("title2").revision("2").text("Testing text 2").build());

        wikiService.storePages("Test source", pages);

        var dbSources = wikiSourceRepository.findAll();
        Assertions.assertEquals(1, dbSources.size());
        Assertions.assertEquals(1, dbSources.getFirst().getId());
        var dbPages = wikiPageRepository.findAll();
        Assertions.assertEquals(2, dbPages.size());
        Assertions.assertEquals(1, dbPages.getFirst().getId());
        Assertions.assertEquals(2, dbPages.get(1).getId());
    }

}

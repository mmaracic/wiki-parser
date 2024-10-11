package wiki.parser.database;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

public class AbstractDatabaseTest {

    @BeforeAll
    public static void beforeAll() {
        DatabaseConfiguration.postgresTestContainer.start();
    }

    @AfterAll
    public static void afterAll() {
        DatabaseConfiguration.postgresTestContainer.stop();
    }

}

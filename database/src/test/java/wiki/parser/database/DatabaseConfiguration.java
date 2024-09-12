package wiki.parser.database;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;

@Configuration
@EnableAutoConfiguration
@ComponentScan
@EntityScan
public class DatabaseConfiguration {

    public static PostgreSQLContainer<?> postgresTestContainer = new PostgreSQLContainer<>(
            "postgres:16-alpine"
    ).withDatabaseName("db").withUsername("admin").withPassword("admin").withInitScript("init_script.sql");

    @Bean
    public DataSource dataSource() {
        return DataSourceBuilder
                .create()
                .username(postgresTestContainer.getUsername())
                .password(postgresTestContainer.getPassword())
                .url(postgresTestContainer.getJdbcUrl())
                .driverClassName(postgresTestContainer.getDriverClassName())
                .build();
    }
}

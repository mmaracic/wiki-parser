/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package wiki.parser.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan(basePackages = {"wiki.parser"})
@EnableJpaRepositories(basePackages = {"wiki.parser"})
@SpringBootApplication(scanBasePackages = {"wiki.parser"})
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}

package wiki.parser.annotation;

import io.toolisticon.cute.Cute;
import org.junit.jupiter.api.Test;

public class WikiAnnotationProcessorTest {

    @Test
    public void unitWikiModelTest() {
        Cute.blackBoxTest()
                .given()
                .processor(WikiAnnotationProcessor.class)
                .andSourceFiles("WikiPage.java")
                .whenCompiled()
                .thenExpectThat()
                .compilationSucceeds()
                .executeTest();
    }

    @Test
    public void unitWikiModelClassError() {
        Cute.blackBoxTest()
                .given()
                .processor(WikiAnnotationProcessor.class)
                .andSourceFiles("WikiPageClassError.java")
                .whenCompiled()
                .thenExpectThat()
                .compilationFails()
                .andThat()
                .compilerMessage()
                .ofKindError()
                .contains("Class that represents Wiki object has to have WikiPath annotation")
                .executeTest();
    }

    @Test
    public void unitWikiModelFieldError() {
        Cute.blackBoxTest()
                .given()
                .processor(WikiAnnotationProcessor.class)
                .andSourceFiles("WikiPageFieldError.java")
                .whenCompiled()
                .thenExpectThat()
                .compilationFails()
                .andThat()
                .compilerMessage()
                .ofKindError()
                .contains("WikiPath field must be of type String")
                .executeTest();
    }
}

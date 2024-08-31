package wiki.parser.annotation;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import java.util.Set;

@SupportedAnnotationTypes(
        {"wiki.parser.annotation.WikiPath"}
)
public class WikiAnnotationProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {
        Messager messager = processingEnv.getMessager();
        for (TypeElement annotation : annotations) {
            for (Element e : env.getElementsAnnotatedWith(annotation)) {
                if (annotation.getSimpleName().toString().equals(WikiPath.class.getSimpleName())) {
                    if (e.getKind().equals(ElementKind.FIELD)) {
                        TypeElement te = (TypeElement) e;
                        if (!String.class.getSimpleName().equals(te.getSimpleName().toString())) {
                            messager.printError("Type is not a string", e);
                        }
                    }
                }
            }
        }
        return false;
    }
}

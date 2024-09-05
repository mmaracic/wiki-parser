package wiki.parser.annotation;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import java.util.Set;

/**
 * Processor should ensure that all WikiPath annotations on fields are on String fields
 * and also that class with WikiPath pm fields also has WikiPath as class annotation
 */

@SupportedSourceVersion(SourceVersion.RELEASE_21)
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
                        TypeMirror te = e.asType();
                        if (!String.class.getName().equals(te.toString())) {
                            messager.printError("WikiPath field must be of type String", e);
                        }
                        Element parent = e.getEnclosingElement();
                        if (!parent.getKind().equals(ElementKind.CLASS)) {
                            messager.printError("WikiPath can only be a field in a class with the same annotation", parent);
                        }
                        if (parent.getAnnotation(WikiPath.class) == null) {
                            messager.printError("Class that represents Wiki object has to have WikiPath annotation", parent);
                        }
                    } else if (e.getKind().equals(ElementKind.CLASS)) {
                        if (e.getAnnotation(WikiPath.class) == null) {
                            messager.printError("Class that represents Wiki object has to have WikiPath annotation", e);
                        }
                    }
                }
            }
        }
        return true;
    }
}

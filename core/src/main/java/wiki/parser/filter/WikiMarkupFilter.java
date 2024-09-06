package wiki.parser.filter;

import org.wikiclean.WikiClean;
import org.wikiclean.languages.English;

public class WikiMarkupFilter implements MarkupFilter {

    private final WikiClean cleaner;

    public WikiMarkupFilter() {
        cleaner = new WikiClean.Builder()
                .withLanguage(new English())
                .withTitle(false)
                .withFooter(false).build();
    }

    @Override
    public String filterText(String input) {
        return cleaner.clean("<text xml:space=\"preserve\">" + input + "</text>");
    }
}

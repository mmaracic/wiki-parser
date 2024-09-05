package wiki.parser.model;

import wiki.parser.annotation.WikiPath;

@WikiPath(path = "page")
public class WikiPage {
    @WikiPath(path = "page.title")
    private String title;
    @WikiPath(path = "page.revision.id")
    private String revison;
    @WikiPath(path = "page.revision.text")
    private String text;
}

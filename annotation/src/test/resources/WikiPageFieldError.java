package wiki.parser.model;

import wiki.parser.annotation.WikiPath;

@WikiPath(path = "page")
public class WikiPageFieldError {
    @WikiPath(path = "page.title")
    private Integer title;
    @WikiPath(path = "page.revision.id")
    private String revison;
    @WikiPath(path = "page.revision.text")
    private String text;
}

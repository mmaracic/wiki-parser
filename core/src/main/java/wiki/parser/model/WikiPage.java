package wiki.parser.model;

import lombok.Builder;
import lombok.Data;
import wiki.parser.annotation.WikiPath;

@Data
@Builder
@WikiPath(path = "page")
public class WikiPage {
    @WikiPath(path = "page.title")
    private String title;
    @WikiPath(path = "page.revision.id")
    private String revision;
    @WikiPath(path = "page.revision.text")
    private String text;
}

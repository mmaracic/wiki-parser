package wiki.parser.core.model;

import lombok.*;
import wiki.parser.annotation.WikiPath;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@WikiPath(path = "page")
public class WikiPage {
    @WikiPath(path = "page.title")
    private String title;
    @WikiPath(path = "page.revision.id")
    private String revision;
    @WikiPath(path = "page.revision.text")
    private String text;
}

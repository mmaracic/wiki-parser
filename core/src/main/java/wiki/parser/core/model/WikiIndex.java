package wiki.parser.core.model;

import lombok.*;
import wiki.parser.core.util.StringList;

/**
 * Title can have multiple components on its own
 * offset1:pageId1:title1
 * offset2:pageId2:title2
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class WikiIndex {

    /**
     * offset is starting offset of bz2 stream. You need to read bytes from specific offset to next item offset from bz2 file
     * and pass them to bz2 decoder to get 1 page from that stream
     */
    private Long offset;

    private Long pageId;

    private StringList title;
}

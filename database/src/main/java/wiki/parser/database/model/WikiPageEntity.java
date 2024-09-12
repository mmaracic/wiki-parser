package wiki.parser.database.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import wiki.parser.core.model.WikiPage;

@Data
@Entity
@Builder
@Table(name = "wiki_page", schema = "wiki")
public class WikiPageEntity {

    /*
     * Allocation size has to be explicitly specified and equal to sequence increment or negative values can appear
     */
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "wiki_page_id")
    @SequenceGenerator(name = "wiki_page_id", sequenceName = "wiki.wiki_page_seq", allocationSize = 1)
    private Long id;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "text", column = @Column(name = "page_text")),
    })
    private WikiPage page;

    @ManyToOne
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JoinColumn(name="source_id", nullable=false)
    private WikiSourceEntity source;

}

package wiki.parser.database.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

import static jakarta.persistence.CascadeType.ALL;

@Data
@Entity
@Builder
@Table(name = "wiki_source", schema = "wiki")
public class WikiSourceEntity {

    /*
     * Allocation size has to be explicitly specified and equal to sequence increment or negative values can appear
     */
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "wiki_source_id")
    @SequenceGenerator(name = "wiki_source_id", sequenceName = "wiki.wiki_source_seq", allocationSize = 1)
    private Long id;

    private String source;

    @CreationTimestamp
    private OffsetDateTime createdAt;

    @Builder.Default
    @OneToMany(cascade=ALL, mappedBy="source")
    private Set<WikiPageEntity> pages = new HashSet<>();
}

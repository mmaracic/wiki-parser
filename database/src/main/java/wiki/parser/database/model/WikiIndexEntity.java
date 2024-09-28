package wiki.parser.database.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.TypeRegistration;
import wiki.parser.core.model.WikiIndex;
import wiki.parser.core.util.StringList;
import wiki.parser.database.converter.StringListType;

@Data
@Entity
@Builder
@Table(name = "wiki_index", schema = "wiki")
@TypeRegistration(basicClass = StringList. class, userType = StringListType. class)
public class WikiIndexEntity {

    /*
     * Allocation size has to be explicitly specified and equal to sequence increment or negative values can appear
     */
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "wiki_index_id")
    @SequenceGenerator(name = "wiki_index_id", sequenceName = "wiki.wiki_index_seq", allocationSize = 1)
    private Long id;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "pageId", column = @Column(name = "page_id")),
            @AttributeOverride(name = "offset", column = @Column(name = "byte_offset")),
    })
    //Converters do not work with custom db types because the converter standard types usually convert to standard db types
    //For custom db types we need userType which is registered using @TypeRegistration as above
    //@Convert(attributeName = "title", converter = StringListAttributeConverter.class)
    private WikiIndex index;

    @ManyToOne
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JoinColumn(name="source_id", nullable=false)
    private WikiSourceEntity source;

}

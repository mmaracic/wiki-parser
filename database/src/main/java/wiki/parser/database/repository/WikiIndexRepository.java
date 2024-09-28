package wiki.parser.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wiki.parser.database.model.WikiIndexEntity;

public interface WikiIndexRepository extends JpaRepository<WikiIndexEntity, Long> {
}

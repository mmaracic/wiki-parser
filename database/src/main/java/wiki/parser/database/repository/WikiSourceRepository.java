package wiki.parser.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wiki.parser.database.model.WikiSourceEntity;

public interface WikiSourceRepository extends JpaRepository<WikiSourceEntity, Long> {
}

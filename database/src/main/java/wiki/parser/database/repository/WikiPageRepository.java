package wiki.parser.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wiki.parser.database.model.WikiPageEntity;

public interface WikiPageRepository extends JpaRepository<WikiPageEntity, Long> {
}

package polytech.g6.blog.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import polytech.g6.blog.domain.Rappel;

/**
 * Spring Data JPA repository for the Rappel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RappelRepository extends JpaRepository<Rappel, Long> {}

package polytech.g6.blog.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import polytech.g6.blog.domain.Repas;

/**
 * Spring Data JPA repository for the Repas entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RepasRepository extends JpaRepository<Repas, Long> {}

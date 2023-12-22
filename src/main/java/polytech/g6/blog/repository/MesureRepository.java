package polytech.g6.blog.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import polytech.g6.blog.domain.Mesure;

/**
 * Spring Data JPA repository for the Mesure entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MesureRepository extends JpaRepository<Mesure, Long> {}

package polytech.g6.blog.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import polytech.g6.blog.domain.Chambre;

/**
 * Spring Data JPA repository for the Chambre entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChambreRepository extends JpaRepository<Chambre, Long> {}

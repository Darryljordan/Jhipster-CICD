package polytech.g6.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import polytech.g6.blog.domain.Authority;

/**
 * Spring Data JPA repository for the {@link Authority} entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {}

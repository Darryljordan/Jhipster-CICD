package polytech.g6.blog.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import polytech.g6.blog.domain.Etablissement;

/**
 * Spring Data JPA repository for the Etablissement entity.
 *
 * When extending this class, extend EtablissementRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface EtablissementRepository extends EtablissementRepositoryWithBagRelationships, JpaRepository<Etablissement, Long> {
    default Optional<Etablissement> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<Etablissement> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<Etablissement> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}

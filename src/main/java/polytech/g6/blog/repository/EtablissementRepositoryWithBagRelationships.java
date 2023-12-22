package polytech.g6.blog.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import polytech.g6.blog.domain.Etablissement;

public interface EtablissementRepositoryWithBagRelationships {
    Optional<Etablissement> fetchBagRelationships(Optional<Etablissement> etablissement);

    List<Etablissement> fetchBagRelationships(List<Etablissement> etablissements);

    Page<Etablissement> fetchBagRelationships(Page<Etablissement> etablissements);
}

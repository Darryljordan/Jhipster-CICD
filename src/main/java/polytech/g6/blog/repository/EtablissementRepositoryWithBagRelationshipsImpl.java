package polytech.g6.blog.repository;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import polytech.g6.blog.domain.Etablissement;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class EtablissementRepositoryWithBagRelationshipsImpl implements EtablissementRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Etablissement> fetchBagRelationships(Optional<Etablissement> etablissement) {
        return etablissement.map(this::fetchUtilisateurs);
    }

    @Override
    public Page<Etablissement> fetchBagRelationships(Page<Etablissement> etablissements) {
        return new PageImpl<>(
            fetchBagRelationships(etablissements.getContent()),
            etablissements.getPageable(),
            etablissements.getTotalElements()
        );
    }

    @Override
    public List<Etablissement> fetchBagRelationships(List<Etablissement> etablissements) {
        return Optional.of(etablissements).map(this::fetchUtilisateurs).orElse(Collections.emptyList());
    }

    Etablissement fetchUtilisateurs(Etablissement result) {
        return entityManager
            .createQuery(
                "select etablissement from Etablissement etablissement left join fetch etablissement.utilisateurs where etablissement is :etablissement",
                Etablissement.class
            )
            .setParameter("etablissement", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Etablissement> fetchUtilisateurs(List<Etablissement> etablissements) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, etablissements.size()).forEach(index -> order.put(etablissements.get(index).getId(), index));
        List<Etablissement> result = entityManager
            .createQuery(
                "select distinct etablissement from Etablissement etablissement left join fetch etablissement.utilisateurs where etablissement in :etablissements",
                Etablissement.class
            )
            .setParameter("etablissements", etablissements)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}

package polytech.g6.blog.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import polytech.g6.blog.domain.Utilisateur;

public interface UtilisateurRepositoryWithBagRelationships {
    Optional<Utilisateur> fetchBagRelationships(Optional<Utilisateur> utilisateur);

    List<Utilisateur> fetchBagRelationships(List<Utilisateur> utilisateurs);

    Page<Utilisateur> fetchBagRelationships(Page<Utilisateur> utilisateurs);
}

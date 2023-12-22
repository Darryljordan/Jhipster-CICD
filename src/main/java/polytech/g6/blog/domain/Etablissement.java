package polytech.g6.blog.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Etablissement.
 */
@Entity
@Table(name = "etablissement")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Etablissement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Size(min = 1)
    @Column(name = "nom_e")
    private String nomE;

    @Column(name = "adresse_e")
    private String adresseE;

    @OneToMany(mappedBy = "etablissement")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "etablissement", "patients" }, allowSetters = true)
    private Set<Chambre> chambres = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_etablissement__utilisateur",
        joinColumns = @JoinColumn(name = "etablissement_id"),
        inverseJoinColumns = @JoinColumn(name = "utilisateur_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "user", "userRoles", "patients", "etablissements" }, allowSetters = true)
    private Set<Utilisateur> utilisateurs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Etablissement id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomE() {
        return this.nomE;
    }

    public Etablissement nomE(String nomE) {
        this.setNomE(nomE);
        return this;
    }

    public void setNomE(String nomE) {
        this.nomE = nomE;
    }

    public String getAdresseE() {
        return this.adresseE;
    }

    public Etablissement adresseE(String adresseE) {
        this.setAdresseE(adresseE);
        return this;
    }

    public void setAdresseE(String adresseE) {
        this.adresseE = adresseE;
    }

    public Set<Chambre> getChambres() {
        return this.chambres;
    }

    public void setChambres(Set<Chambre> chambres) {
        if (this.chambres != null) {
            this.chambres.forEach(i -> i.setEtablissement(null));
        }
        if (chambres != null) {
            chambres.forEach(i -> i.setEtablissement(this));
        }
        this.chambres = chambres;
    }

    public Etablissement chambres(Set<Chambre> chambres) {
        this.setChambres(chambres);
        return this;
    }

    public Etablissement addChambres(Chambre chambre) {
        this.chambres.add(chambre);
        chambre.setEtablissement(this);
        return this;
    }

    public Etablissement removeChambres(Chambre chambre) {
        this.chambres.remove(chambre);
        chambre.setEtablissement(null);
        return this;
    }

    public Set<Utilisateur> getUtilisateurs() {
        return this.utilisateurs;
    }

    public void setUtilisateurs(Set<Utilisateur> utilisateurs) {
        this.utilisateurs = utilisateurs;
    }

    public Etablissement utilisateurs(Set<Utilisateur> utilisateurs) {
        this.setUtilisateurs(utilisateurs);
        return this;
    }

    public Etablissement addUtilisateur(Utilisateur utilisateur) {
        this.utilisateurs.add(utilisateur);
        utilisateur.getEtablissements().add(this);
        return this;
    }

    public Etablissement removeUtilisateur(Utilisateur utilisateur) {
        this.utilisateurs.remove(utilisateur);
        utilisateur.getEtablissements().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Etablissement)) {
            return false;
        }
        return id != null && id.equals(((Etablissement) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Etablissement{" +
            "id=" + getId() +
            ", nomE='" + getNomE() + "'" +
            ", adresseE='" + getAdresseE() + "'" +
            "}";
    }
}

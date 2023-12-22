package polytech.g6.blog.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import polytech.g6.blog.domain.enumeration.Sexe;

/**
 * A Patient.
 */
@Entity
@Table(name = "patient")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Patient implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Size(min = 1)
    @Column(name = "nom_p")
    private String nomP;

    @Size(min = 1)
    @Column(name = "prenom_p")
    private String prenomP;

    @Column(name = "date_naissance_p")
    private LocalDate dateNaissanceP;

    @Column(name = "taille_p")
    private Float tailleP;

    @Enumerated(EnumType.STRING)
    @Column(name = "sexe_p")
    private Sexe sexeP;

    @Column(name = "date_arrivee")
    private LocalDate dateArrivee;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "etablissement", "patients" }, allowSetters = true)
    private Chambre chambres;

    @ManyToMany(mappedBy = "patients")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "user", "userRoles", "patients", "etablissements" }, allowSetters = true)
    private Set<Utilisateur> utilisateurs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Patient id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomP() {
        return this.nomP;
    }

    public Patient nomP(String nomP) {
        this.setNomP(nomP);
        return this;
    }

    public void setNomP(String nomP) {
        this.nomP = nomP;
    }

    public String getPrenomP() {
        return this.prenomP;
    }

    public Patient prenomP(String prenomP) {
        this.setPrenomP(prenomP);
        return this;
    }

    public void setPrenomP(String prenomP) {
        this.prenomP = prenomP;
    }

    public LocalDate getDateNaissanceP() {
        return this.dateNaissanceP;
    }

    public Patient dateNaissanceP(LocalDate dateNaissanceP) {
        this.setDateNaissanceP(dateNaissanceP);
        return this;
    }

    public void setDateNaissanceP(LocalDate dateNaissanceP) {
        this.dateNaissanceP = dateNaissanceP;
    }

    public Float getTailleP() {
        return this.tailleP;
    }

    public Patient tailleP(Float tailleP) {
        this.setTailleP(tailleP);
        return this;
    }

    public void setTailleP(Float tailleP) {
        this.tailleP = tailleP;
    }

    public Sexe getSexeP() {
        return this.sexeP;
    }

    public Patient sexeP(Sexe sexeP) {
        this.setSexeP(sexeP);
        return this;
    }

    public void setSexeP(Sexe sexeP) {
        this.sexeP = sexeP;
    }

    public LocalDate getDateArrivee() {
        return this.dateArrivee;
    }

    public Patient dateArrivee(LocalDate dateArrivee) {
        this.setDateArrivee(dateArrivee);
        return this;
    }

    public void setDateArrivee(LocalDate dateArrivee) {
        this.dateArrivee = dateArrivee;
    }

    public Chambre getChambres() {
        return this.chambres;
    }

    public void setChambres(Chambre chambre) {
        this.chambres = chambre;
    }

    public Patient chambres(Chambre chambre) {
        this.setChambres(chambre);
        return this;
    }

    public Set<Utilisateur> getUtilisateurs() {
        return this.utilisateurs;
    }

    public void setUtilisateurs(Set<Utilisateur> utilisateurs) {
        if (this.utilisateurs != null) {
            this.utilisateurs.forEach(i -> i.removePatient(this));
        }
        if (utilisateurs != null) {
            utilisateurs.forEach(i -> i.addPatient(this));
        }
        this.utilisateurs = utilisateurs;
    }

    public Patient utilisateurs(Set<Utilisateur> utilisateurs) {
        this.setUtilisateurs(utilisateurs);
        return this;
    }

    public Patient addUtilisateur(Utilisateur utilisateur) {
        this.utilisateurs.add(utilisateur);
        utilisateur.getPatients().add(this);
        return this;
    }

    public Patient removeUtilisateur(Utilisateur utilisateur) {
        this.utilisateurs.remove(utilisateur);
        utilisateur.getPatients().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Patient)) {
            return false;
        }
        return id != null && id.equals(((Patient) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Patient{" +
            "id=" + getId() +
            ", nomP='" + getNomP() + "'" +
            ", prenomP='" + getPrenomP() + "'" +
            ", dateNaissanceP='" + getDateNaissanceP() + "'" +
            ", tailleP=" + getTailleP() +
            ", sexeP='" + getSexeP() + "'" +
            ", dateArrivee='" + getDateArrivee() + "'" +
            "}";
    }
}

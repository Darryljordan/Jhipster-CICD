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

/**
 * A Utilisateur.
 */
@Entity
@Table(name = "utilisateur")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Utilisateur implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "date_naissance_u")
    private LocalDate dateNaissanceU;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @ManyToMany
    @NotNull
    @JoinTable(
        name = "rel_utilisateur__user_role",
        joinColumns = @JoinColumn(name = "utilisateur_id"),
        inverseJoinColumns = @JoinColumn(name = "user_role_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "utilisateurs" }, allowSetters = true)
    private Set<UserRole> userRoles = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_utilisateur__patient",
        joinColumns = @JoinColumn(name = "utilisateur_id"),
        inverseJoinColumns = @JoinColumn(name = "patient_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "chambres", "utilisateurs" }, allowSetters = true)
    private Set<Patient> patients = new HashSet<>();

    @ManyToMany(mappedBy = "utilisateurs")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "chambres", "utilisateurs" }, allowSetters = true)
    private Set<Etablissement> etablissements = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Utilisateur id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateNaissanceU() {
        return this.dateNaissanceU;
    }

    public Utilisateur dateNaissanceU(LocalDate dateNaissanceU) {
        this.setDateNaissanceU(dateNaissanceU);
        return this;
    }

    public void setDateNaissanceU(LocalDate dateNaissanceU) {
        this.dateNaissanceU = dateNaissanceU;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Utilisateur user(User user) {
        this.setUser(user);
        return this;
    }

    public Set<UserRole> getUserRoles() {
        return this.userRoles;
    }

    public void setUserRoles(Set<UserRole> userRoles) {
        this.userRoles = userRoles;
    }

    public Utilisateur userRoles(Set<UserRole> userRoles) {
        this.setUserRoles(userRoles);
        return this;
    }

    public Utilisateur addUserRole(UserRole userRole) {
        this.userRoles.add(userRole);
        userRole.getUtilisateurs().add(this);
        return this;
    }

    public Utilisateur removeUserRole(UserRole userRole) {
        this.userRoles.remove(userRole);
        userRole.getUtilisateurs().remove(this);
        return this;
    }

    public Set<Patient> getPatients() {
        return this.patients;
    }

    public void setPatients(Set<Patient> patients) {
        this.patients = patients;
    }

    public Utilisateur patients(Set<Patient> patients) {
        this.setPatients(patients);
        return this;
    }

    public Utilisateur addPatient(Patient patient) {
        this.patients.add(patient);
        patient.getUtilisateurs().add(this);
        return this;
    }

    public Utilisateur removePatient(Patient patient) {
        this.patients.remove(patient);
        patient.getUtilisateurs().remove(this);
        return this;
    }

    public Set<Etablissement> getEtablissements() {
        return this.etablissements;
    }

    public void setEtablissements(Set<Etablissement> etablissements) {
        if (this.etablissements != null) {
            this.etablissements.forEach(i -> i.removeUtilisateur(this));
        }
        if (etablissements != null) {
            etablissements.forEach(i -> i.addUtilisateur(this));
        }
        this.etablissements = etablissements;
    }

    public Utilisateur etablissements(Set<Etablissement> etablissements) {
        this.setEtablissements(etablissements);
        return this;
    }

    public Utilisateur addEtablissement(Etablissement etablissement) {
        this.etablissements.add(etablissement);
        etablissement.getUtilisateurs().add(this);
        return this;
    }

    public Utilisateur removeEtablissement(Etablissement etablissement) {
        this.etablissements.remove(etablissement);
        etablissement.getUtilisateurs().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Utilisateur)) {
            return false;
        }
        return id != null && id.equals(((Utilisateur) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Utilisateur{" +
            "id=" + getId() +
            ", dateNaissanceU='" + getDateNaissanceU() + "'" +
            "}";
    }
}

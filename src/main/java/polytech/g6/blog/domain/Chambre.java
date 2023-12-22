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
 * A Chambre.
 */
@Entity
@Table(name = "chambre")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Chambre implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "num_c", nullable = false)
    private String numC;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "chambres", "utilisateurs" }, allowSetters = true)
    private Etablissement etablissement;

    @OneToMany(mappedBy = "chambres")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "chambres", "utilisateurs" }, allowSetters = true)
    private Set<Patient> patients = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Chambre id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumC() {
        return this.numC;
    }

    public Chambre numC(String numC) {
        this.setNumC(numC);
        return this;
    }

    public void setNumC(String numC) {
        this.numC = numC;
    }

    public Etablissement getEtablissement() {
        return this.etablissement;
    }

    public void setEtablissement(Etablissement etablissement) {
        this.etablissement = etablissement;
    }

    public Chambre etablissement(Etablissement etablissement) {
        this.setEtablissement(etablissement);
        return this;
    }

    public Set<Patient> getPatients() {
        return this.patients;
    }

    public void setPatients(Set<Patient> patients) {
        if (this.patients != null) {
            this.patients.forEach(i -> i.setChambres(null));
        }
        if (patients != null) {
            patients.forEach(i -> i.setChambres(this));
        }
        this.patients = patients;
    }

    public Chambre patients(Set<Patient> patients) {
        this.setPatients(patients);
        return this;
    }

    public Chambre addPatient(Patient patient) {
        this.patients.add(patient);
        patient.setChambres(this);
        return this;
    }

    public Chambre removePatient(Patient patient) {
        this.patients.remove(patient);
        patient.setChambres(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Chambre)) {
            return false;
        }
        return id != null && id.equals(((Chambre) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Chambre{" +
            "id=" + getId() +
            ", numC='" + getNumC() + "'" +
            "}";
    }
}

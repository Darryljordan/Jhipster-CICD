package polytech.g6.blog.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Repas.
 */
@Entity
@Table(name = "repas")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Repas implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "date_r")
    private LocalDate dateR;

    @Column(name = "heure_r")
    private Instant heureR;

    @Column(name = "epa")
    private Long epa;

    @ManyToOne
    @JsonIgnoreProperties(value = { "chambres", "utilisateurs" }, allowSetters = true)
    private Patient patient;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Repas id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateR() {
        return this.dateR;
    }

    public Repas dateR(LocalDate dateR) {
        this.setDateR(dateR);
        return this;
    }

    public void setDateR(LocalDate dateR) {
        this.dateR = dateR;
    }

    public Instant getHeureR() {
        return this.heureR;
    }

    public Repas heureR(Instant heureR) {
        this.setHeureR(heureR);
        return this;
    }

    public void setHeureR(Instant heureR) {
        this.heureR = heureR;
    }

    public Long getEpa() {
        return this.epa;
    }

    public Repas epa(Long epa) {
        this.setEpa(epa);
        return this;
    }

    public void setEpa(Long epa) {
        this.epa = epa;
    }

    public Patient getPatient() {
        return this.patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Repas patient(Patient patient) {
        this.setPatient(patient);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Repas)) {
            return false;
        }
        return id != null && id.equals(((Repas) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Repas{" +
            "id=" + getId() +
            ", dateR='" + getDateR() + "'" +
            ", heureR='" + getHeureR() + "'" +
            ", epa=" + getEpa() +
            "}";
    }
}

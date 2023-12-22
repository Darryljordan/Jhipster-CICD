package polytech.g6.blog.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import polytech.g6.blog.domain.enumeration.TypeMesure;

/**
 * A Mesure.
 */
@Entity
@Table(name = "mesure")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Mesure implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private TypeMesure type;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "valeur")
    private Float valeur;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "chambres", "utilisateurs" }, allowSetters = true)
    private Patient patient;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Mesure id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TypeMesure getType() {
        return this.type;
    }

    public Mesure type(TypeMesure type) {
        this.setType(type);
        return this;
    }

    public void setType(TypeMesure type) {
        this.type = type;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public Mesure date(LocalDate date) {
        this.setDate(date);
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Float getValeur() {
        return this.valeur;
    }

    public Mesure valeur(Float valeur) {
        this.setValeur(valeur);
        return this;
    }

    public void setValeur(Float valeur) {
        this.valeur = valeur;
    }

    public Patient getPatient() {
        return this.patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Mesure patient(Patient patient) {
        this.setPatient(patient);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Mesure)) {
            return false;
        }
        return id != null && id.equals(((Mesure) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Mesure{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", date='" + getDate() + "'" +
            ", valeur=" + getValeur() +
            "}";
    }
}

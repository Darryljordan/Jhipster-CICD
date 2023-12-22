package polytech.g6.blog.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import polytech.g6.blog.IntegrationTest;
import polytech.g6.blog.domain.Chambre;
import polytech.g6.blog.domain.Patient;
import polytech.g6.blog.domain.Utilisateur;
import polytech.g6.blog.domain.enumeration.Sexe;
import polytech.g6.blog.repository.PatientRepository;

/**
 * Integration tests for the {@link PatientResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PatientResourceIT {

    private static final String DEFAULT_NOM_P = "AAAAAAAAAA";
    private static final String UPDATED_NOM_P = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOM_P = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM_P = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_NAISSANCE_P = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_NAISSANCE_P = LocalDate.now(ZoneId.systemDefault());

    private static final Float DEFAULT_TAILLE_P = 1F;
    private static final Float UPDATED_TAILLE_P = 2F;

    private static final Sexe DEFAULT_SEXE_P = Sexe.HOMME;
    private static final Sexe UPDATED_SEXE_P = Sexe.FEMME;

    private static final LocalDate DEFAULT_DATE_ARRIVEE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_ARRIVEE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/patients";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPatientMockMvc;

    private Patient patient;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Patient createEntity(EntityManager em) {
        Patient patient = new Patient()
            .nomP(DEFAULT_NOM_P)
            .prenomP(DEFAULT_PRENOM_P)
            .dateNaissanceP(DEFAULT_DATE_NAISSANCE_P)
            .tailleP(DEFAULT_TAILLE_P)
            .sexeP(DEFAULT_SEXE_P)
            .dateArrivee(DEFAULT_DATE_ARRIVEE);
        // Add required entity
        Chambre chambre;
        if (TestUtil.findAll(em, Chambre.class).isEmpty()) {
            chambre = ChambreResourceIT.createEntity(em);
            em.persist(chambre);
            em.flush();
        } else {
            chambre = TestUtil.findAll(em, Chambre.class).get(0);
        }
        patient.setChambres(chambre);
        // Add required entity
        Utilisateur utilisateur;
        if (TestUtil.findAll(em, Utilisateur.class).isEmpty()) {
            utilisateur = UtilisateurResourceIT.createEntity(em);
            em.persist(utilisateur);
            em.flush();
        } else {
            utilisateur = TestUtil.findAll(em, Utilisateur.class).get(0);
        }
        patient.getUtilisateurs().add(utilisateur);
        return patient;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Patient createUpdatedEntity(EntityManager em) {
        Patient patient = new Patient()
            .nomP(UPDATED_NOM_P)
            .prenomP(UPDATED_PRENOM_P)
            .dateNaissanceP(UPDATED_DATE_NAISSANCE_P)
            .tailleP(UPDATED_TAILLE_P)
            .sexeP(UPDATED_SEXE_P)
            .dateArrivee(UPDATED_DATE_ARRIVEE);
        // Add required entity
        Chambre chambre;
        if (TestUtil.findAll(em, Chambre.class).isEmpty()) {
            chambre = ChambreResourceIT.createUpdatedEntity(em);
            em.persist(chambre);
            em.flush();
        } else {
            chambre = TestUtil.findAll(em, Chambre.class).get(0);
        }
        patient.setChambres(chambre);
        // Add required entity
        Utilisateur utilisateur;
        if (TestUtil.findAll(em, Utilisateur.class).isEmpty()) {
            utilisateur = UtilisateurResourceIT.createUpdatedEntity(em);
            em.persist(utilisateur);
            em.flush();
        } else {
            utilisateur = TestUtil.findAll(em, Utilisateur.class).get(0);
        }
        patient.getUtilisateurs().add(utilisateur);
        return patient;
    }

    @BeforeEach
    public void initTest() {
        patient = createEntity(em);
    }

    @Test
    @Transactional
    void createPatient() throws Exception {
        int databaseSizeBeforeCreate = patientRepository.findAll().size();
        // Create the Patient
        restPatientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(patient)))
            .andExpect(status().isCreated());

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeCreate + 1);
        Patient testPatient = patientList.get(patientList.size() - 1);
        assertThat(testPatient.getNomP()).isEqualTo(DEFAULT_NOM_P);
        assertThat(testPatient.getPrenomP()).isEqualTo(DEFAULT_PRENOM_P);
        assertThat(testPatient.getDateNaissanceP()).isEqualTo(DEFAULT_DATE_NAISSANCE_P);
        assertThat(testPatient.getTailleP()).isEqualTo(DEFAULT_TAILLE_P);
        assertThat(testPatient.getSexeP()).isEqualTo(DEFAULT_SEXE_P);
        assertThat(testPatient.getDateArrivee()).isEqualTo(DEFAULT_DATE_ARRIVEE);
    }

    @Test
    @Transactional
    void createPatientWithExistingId() throws Exception {
        // Create the Patient with an existing ID
        patient.setId(1L);

        int databaseSizeBeforeCreate = patientRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPatientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(patient)))
            .andExpect(status().isBadRequest());

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPatients() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList
        restPatientMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(patient.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomP").value(hasItem(DEFAULT_NOM_P)))
            .andExpect(jsonPath("$.[*].prenomP").value(hasItem(DEFAULT_PRENOM_P)))
            .andExpect(jsonPath("$.[*].dateNaissanceP").value(hasItem(DEFAULT_DATE_NAISSANCE_P.toString())))
            .andExpect(jsonPath("$.[*].tailleP").value(hasItem(DEFAULT_TAILLE_P.doubleValue())))
            .andExpect(jsonPath("$.[*].sexeP").value(hasItem(DEFAULT_SEXE_P.toString())))
            .andExpect(jsonPath("$.[*].dateArrivee").value(hasItem(DEFAULT_DATE_ARRIVEE.toString())));
    }

    @Test
    @Transactional
    void getPatient() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get the patient
        restPatientMockMvc
            .perform(get(ENTITY_API_URL_ID, patient.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(patient.getId().intValue()))
            .andExpect(jsonPath("$.nomP").value(DEFAULT_NOM_P))
            .andExpect(jsonPath("$.prenomP").value(DEFAULT_PRENOM_P))
            .andExpect(jsonPath("$.dateNaissanceP").value(DEFAULT_DATE_NAISSANCE_P.toString()))
            .andExpect(jsonPath("$.tailleP").value(DEFAULT_TAILLE_P.doubleValue()))
            .andExpect(jsonPath("$.sexeP").value(DEFAULT_SEXE_P.toString()))
            .andExpect(jsonPath("$.dateArrivee").value(DEFAULT_DATE_ARRIVEE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPatient() throws Exception {
        // Get the patient
        restPatientMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPatient() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        int databaseSizeBeforeUpdate = patientRepository.findAll().size();

        // Update the patient
        Patient updatedPatient = patientRepository.findById(patient.getId()).get();
        // Disconnect from session so that the updates on updatedPatient are not directly saved in db
        em.detach(updatedPatient);
        updatedPatient
            .nomP(UPDATED_NOM_P)
            .prenomP(UPDATED_PRENOM_P)
            .dateNaissanceP(UPDATED_DATE_NAISSANCE_P)
            .tailleP(UPDATED_TAILLE_P)
            .sexeP(UPDATED_SEXE_P)
            .dateArrivee(UPDATED_DATE_ARRIVEE);

        restPatientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPatient.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPatient))
            )
            .andExpect(status().isOk());

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeUpdate);
        Patient testPatient = patientList.get(patientList.size() - 1);
        assertThat(testPatient.getNomP()).isEqualTo(UPDATED_NOM_P);
        assertThat(testPatient.getPrenomP()).isEqualTo(UPDATED_PRENOM_P);
        assertThat(testPatient.getDateNaissanceP()).isEqualTo(UPDATED_DATE_NAISSANCE_P);
        assertThat(testPatient.getTailleP()).isEqualTo(UPDATED_TAILLE_P);
        assertThat(testPatient.getSexeP()).isEqualTo(UPDATED_SEXE_P);
        assertThat(testPatient.getDateArrivee()).isEqualTo(UPDATED_DATE_ARRIVEE);
    }

    @Test
    @Transactional
    void putNonExistingPatient() throws Exception {
        int databaseSizeBeforeUpdate = patientRepository.findAll().size();
        patient.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPatientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, patient.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(patient))
            )
            .andExpect(status().isBadRequest());

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPatient() throws Exception {
        int databaseSizeBeforeUpdate = patientRepository.findAll().size();
        patient.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPatientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(patient))
            )
            .andExpect(status().isBadRequest());

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPatient() throws Exception {
        int databaseSizeBeforeUpdate = patientRepository.findAll().size();
        patient.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPatientMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(patient)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePatientWithPatch() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        int databaseSizeBeforeUpdate = patientRepository.findAll().size();

        // Update the patient using partial update
        Patient partialUpdatedPatient = new Patient();
        partialUpdatedPatient.setId(patient.getId());

        partialUpdatedPatient
            .nomP(UPDATED_NOM_P)
            .prenomP(UPDATED_PRENOM_P)
            .dateNaissanceP(UPDATED_DATE_NAISSANCE_P)
            .tailleP(UPDATED_TAILLE_P)
            .dateArrivee(UPDATED_DATE_ARRIVEE);

        restPatientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPatient.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPatient))
            )
            .andExpect(status().isOk());

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeUpdate);
        Patient testPatient = patientList.get(patientList.size() - 1);
        assertThat(testPatient.getNomP()).isEqualTo(UPDATED_NOM_P);
        assertThat(testPatient.getPrenomP()).isEqualTo(UPDATED_PRENOM_P);
        assertThat(testPatient.getDateNaissanceP()).isEqualTo(UPDATED_DATE_NAISSANCE_P);
        assertThat(testPatient.getTailleP()).isEqualTo(UPDATED_TAILLE_P);
        assertThat(testPatient.getSexeP()).isEqualTo(DEFAULT_SEXE_P);
        assertThat(testPatient.getDateArrivee()).isEqualTo(UPDATED_DATE_ARRIVEE);
    }

    @Test
    @Transactional
    void fullUpdatePatientWithPatch() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        int databaseSizeBeforeUpdate = patientRepository.findAll().size();

        // Update the patient using partial update
        Patient partialUpdatedPatient = new Patient();
        partialUpdatedPatient.setId(patient.getId());

        partialUpdatedPatient
            .nomP(UPDATED_NOM_P)
            .prenomP(UPDATED_PRENOM_P)
            .dateNaissanceP(UPDATED_DATE_NAISSANCE_P)
            .tailleP(UPDATED_TAILLE_P)
            .sexeP(UPDATED_SEXE_P)
            .dateArrivee(UPDATED_DATE_ARRIVEE);

        restPatientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPatient.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPatient))
            )
            .andExpect(status().isOk());

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeUpdate);
        Patient testPatient = patientList.get(patientList.size() - 1);
        assertThat(testPatient.getNomP()).isEqualTo(UPDATED_NOM_P);
        assertThat(testPatient.getPrenomP()).isEqualTo(UPDATED_PRENOM_P);
        assertThat(testPatient.getDateNaissanceP()).isEqualTo(UPDATED_DATE_NAISSANCE_P);
        assertThat(testPatient.getTailleP()).isEqualTo(UPDATED_TAILLE_P);
        assertThat(testPatient.getSexeP()).isEqualTo(UPDATED_SEXE_P);
        assertThat(testPatient.getDateArrivee()).isEqualTo(UPDATED_DATE_ARRIVEE);
    }

    @Test
    @Transactional
    void patchNonExistingPatient() throws Exception {
        int databaseSizeBeforeUpdate = patientRepository.findAll().size();
        patient.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPatientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, patient.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(patient))
            )
            .andExpect(status().isBadRequest());

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPatient() throws Exception {
        int databaseSizeBeforeUpdate = patientRepository.findAll().size();
        patient.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPatientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(patient))
            )
            .andExpect(status().isBadRequest());

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPatient() throws Exception {
        int databaseSizeBeforeUpdate = patientRepository.findAll().size();
        patient.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPatientMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(patient)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePatient() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        int databaseSizeBeforeDelete = patientRepository.findAll().size();

        // Delete the patient
        restPatientMockMvc
            .perform(delete(ENTITY_API_URL_ID, patient.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

package polytech.g6.blog.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
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
import polytech.g6.blog.domain.Repas;
import polytech.g6.blog.repository.RepasRepository;

/**
 * Integration tests for the {@link RepasResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RepasResourceIT {

    private static final LocalDate DEFAULT_DATE_R = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_R = LocalDate.now(ZoneId.systemDefault());

    private static final Instant DEFAULT_HEURE_R = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_HEURE_R = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_EPA = 1L;
    private static final Long UPDATED_EPA = 2L;

    private static final String ENTITY_API_URL = "/api/repas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RepasRepository repasRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRepasMockMvc;

    private Repas repas;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Repas createEntity(EntityManager em) {
        Repas repas = new Repas().dateR(DEFAULT_DATE_R).heureR(DEFAULT_HEURE_R).epa(DEFAULT_EPA);
        return repas;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Repas createUpdatedEntity(EntityManager em) {
        Repas repas = new Repas().dateR(UPDATED_DATE_R).heureR(UPDATED_HEURE_R).epa(UPDATED_EPA);
        return repas;
    }

    @BeforeEach
    public void initTest() {
        repas = createEntity(em);
    }

    @Test
    @Transactional
    void createRepas() throws Exception {
        int databaseSizeBeforeCreate = repasRepository.findAll().size();
        // Create the Repas
        restRepasMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(repas)))
            .andExpect(status().isCreated());

        // Validate the Repas in the database
        List<Repas> repasList = repasRepository.findAll();
        assertThat(repasList).hasSize(databaseSizeBeforeCreate + 1);
        Repas testRepas = repasList.get(repasList.size() - 1);
        assertThat(testRepas.getDateR()).isEqualTo(DEFAULT_DATE_R);
        assertThat(testRepas.getHeureR()).isEqualTo(DEFAULT_HEURE_R);
        assertThat(testRepas.getEpa()).isEqualTo(DEFAULT_EPA);
    }

    @Test
    @Transactional
    void createRepasWithExistingId() throws Exception {
        // Create the Repas with an existing ID
        repas.setId(1L);

        int databaseSizeBeforeCreate = repasRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRepasMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(repas)))
            .andExpect(status().isBadRequest());

        // Validate the Repas in the database
        List<Repas> repasList = repasRepository.findAll();
        assertThat(repasList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllRepas() throws Exception {
        // Initialize the database
        repasRepository.saveAndFlush(repas);

        // Get all the repasList
        restRepasMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(repas.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateR").value(hasItem(DEFAULT_DATE_R.toString())))
            .andExpect(jsonPath("$.[*].heureR").value(hasItem(DEFAULT_HEURE_R.toString())))
            .andExpect(jsonPath("$.[*].epa").value(hasItem(DEFAULT_EPA.intValue())));
    }

    @Test
    @Transactional
    void getRepas() throws Exception {
        // Initialize the database
        repasRepository.saveAndFlush(repas);

        // Get the repas
        restRepasMockMvc
            .perform(get(ENTITY_API_URL_ID, repas.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(repas.getId().intValue()))
            .andExpect(jsonPath("$.dateR").value(DEFAULT_DATE_R.toString()))
            .andExpect(jsonPath("$.heureR").value(DEFAULT_HEURE_R.toString()))
            .andExpect(jsonPath("$.epa").value(DEFAULT_EPA.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingRepas() throws Exception {
        // Get the repas
        restRepasMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRepas() throws Exception {
        // Initialize the database
        repasRepository.saveAndFlush(repas);

        int databaseSizeBeforeUpdate = repasRepository.findAll().size();

        // Update the repas
        Repas updatedRepas = repasRepository.findById(repas.getId()).get();
        // Disconnect from session so that the updates on updatedRepas are not directly saved in db
        em.detach(updatedRepas);
        updatedRepas.dateR(UPDATED_DATE_R).heureR(UPDATED_HEURE_R).epa(UPDATED_EPA);

        restRepasMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRepas.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedRepas))
            )
            .andExpect(status().isOk());

        // Validate the Repas in the database
        List<Repas> repasList = repasRepository.findAll();
        assertThat(repasList).hasSize(databaseSizeBeforeUpdate);
        Repas testRepas = repasList.get(repasList.size() - 1);
        assertThat(testRepas.getDateR()).isEqualTo(UPDATED_DATE_R);
        assertThat(testRepas.getHeureR()).isEqualTo(UPDATED_HEURE_R);
        assertThat(testRepas.getEpa()).isEqualTo(UPDATED_EPA);
    }

    @Test
    @Transactional
    void putNonExistingRepas() throws Exception {
        int databaseSizeBeforeUpdate = repasRepository.findAll().size();
        repas.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRepasMockMvc
            .perform(
                put(ENTITY_API_URL_ID, repas.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(repas))
            )
            .andExpect(status().isBadRequest());

        // Validate the Repas in the database
        List<Repas> repasList = repasRepository.findAll();
        assertThat(repasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRepas() throws Exception {
        int databaseSizeBeforeUpdate = repasRepository.findAll().size();
        repas.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRepasMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(repas))
            )
            .andExpect(status().isBadRequest());

        // Validate the Repas in the database
        List<Repas> repasList = repasRepository.findAll();
        assertThat(repasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRepas() throws Exception {
        int databaseSizeBeforeUpdate = repasRepository.findAll().size();
        repas.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRepasMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(repas)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Repas in the database
        List<Repas> repasList = repasRepository.findAll();
        assertThat(repasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRepasWithPatch() throws Exception {
        // Initialize the database
        repasRepository.saveAndFlush(repas);

        int databaseSizeBeforeUpdate = repasRepository.findAll().size();

        // Update the repas using partial update
        Repas partialUpdatedRepas = new Repas();
        partialUpdatedRepas.setId(repas.getId());

        partialUpdatedRepas.heureR(UPDATED_HEURE_R).epa(UPDATED_EPA);

        restRepasMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRepas.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRepas))
            )
            .andExpect(status().isOk());

        // Validate the Repas in the database
        List<Repas> repasList = repasRepository.findAll();
        assertThat(repasList).hasSize(databaseSizeBeforeUpdate);
        Repas testRepas = repasList.get(repasList.size() - 1);
        assertThat(testRepas.getDateR()).isEqualTo(DEFAULT_DATE_R);
        assertThat(testRepas.getHeureR()).isEqualTo(UPDATED_HEURE_R);
        assertThat(testRepas.getEpa()).isEqualTo(UPDATED_EPA);
    }

    @Test
    @Transactional
    void fullUpdateRepasWithPatch() throws Exception {
        // Initialize the database
        repasRepository.saveAndFlush(repas);

        int databaseSizeBeforeUpdate = repasRepository.findAll().size();

        // Update the repas using partial update
        Repas partialUpdatedRepas = new Repas();
        partialUpdatedRepas.setId(repas.getId());

        partialUpdatedRepas.dateR(UPDATED_DATE_R).heureR(UPDATED_HEURE_R).epa(UPDATED_EPA);

        restRepasMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRepas.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRepas))
            )
            .andExpect(status().isOk());

        // Validate the Repas in the database
        List<Repas> repasList = repasRepository.findAll();
        assertThat(repasList).hasSize(databaseSizeBeforeUpdate);
        Repas testRepas = repasList.get(repasList.size() - 1);
        assertThat(testRepas.getDateR()).isEqualTo(UPDATED_DATE_R);
        assertThat(testRepas.getHeureR()).isEqualTo(UPDATED_HEURE_R);
        assertThat(testRepas.getEpa()).isEqualTo(UPDATED_EPA);
    }

    @Test
    @Transactional
    void patchNonExistingRepas() throws Exception {
        int databaseSizeBeforeUpdate = repasRepository.findAll().size();
        repas.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRepasMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, repas.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(repas))
            )
            .andExpect(status().isBadRequest());

        // Validate the Repas in the database
        List<Repas> repasList = repasRepository.findAll();
        assertThat(repasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRepas() throws Exception {
        int databaseSizeBeforeUpdate = repasRepository.findAll().size();
        repas.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRepasMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(repas))
            )
            .andExpect(status().isBadRequest());

        // Validate the Repas in the database
        List<Repas> repasList = repasRepository.findAll();
        assertThat(repasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRepas() throws Exception {
        int databaseSizeBeforeUpdate = repasRepository.findAll().size();
        repas.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRepasMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(repas)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Repas in the database
        List<Repas> repasList = repasRepository.findAll();
        assertThat(repasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRepas() throws Exception {
        // Initialize the database
        repasRepository.saveAndFlush(repas);

        int databaseSizeBeforeDelete = repasRepository.findAll().size();

        // Delete the repas
        restRepasMockMvc
            .perform(delete(ENTITY_API_URL_ID, repas.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Repas> repasList = repasRepository.findAll();
        assertThat(repasList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

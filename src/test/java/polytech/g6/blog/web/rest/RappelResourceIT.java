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
import polytech.g6.blog.domain.Rappel;
import polytech.g6.blog.domain.Utilisateur;
import polytech.g6.blog.repository.RappelRepository;

/**
 * Integration tests for the {@link RappelResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RappelResourceIT {

    private static final LocalDate DEFAULT_DATE_DEBUT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DEBUT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_FIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_FIN = LocalDate.now(ZoneId.systemDefault());

    private static final Long DEFAULT_FREQUENCE = 1L;
    private static final Long UPDATED_FREQUENCE = 2L;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/rappels";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RappelRepository rappelRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRappelMockMvc;

    private Rappel rappel;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Rappel createEntity(EntityManager em) {
        Rappel rappel = new Rappel()
            .dateDebut(DEFAULT_DATE_DEBUT)
            .dateFin(DEFAULT_DATE_FIN)
            .frequence(DEFAULT_FREQUENCE)
            .description(DEFAULT_DESCRIPTION);
        // Add required entity
        Utilisateur utilisateur;
        if (TestUtil.findAll(em, Utilisateur.class).isEmpty()) {
            utilisateur = UtilisateurResourceIT.createEntity(em);
            em.persist(utilisateur);
            em.flush();
        } else {
            utilisateur = TestUtil.findAll(em, Utilisateur.class).get(0);
        }
        rappel.setUtilisateur(utilisateur);
        return rappel;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Rappel createUpdatedEntity(EntityManager em) {
        Rappel rappel = new Rappel()
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .frequence(UPDATED_FREQUENCE)
            .description(UPDATED_DESCRIPTION);
        // Add required entity
        Utilisateur utilisateur;
        if (TestUtil.findAll(em, Utilisateur.class).isEmpty()) {
            utilisateur = UtilisateurResourceIT.createUpdatedEntity(em);
            em.persist(utilisateur);
            em.flush();
        } else {
            utilisateur = TestUtil.findAll(em, Utilisateur.class).get(0);
        }
        rappel.setUtilisateur(utilisateur);
        return rappel;
    }

    @BeforeEach
    public void initTest() {
        rappel = createEntity(em);
    }

    @Test
    @Transactional
    void createRappel() throws Exception {
        int databaseSizeBeforeCreate = rappelRepository.findAll().size();
        // Create the Rappel
        restRappelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rappel)))
            .andExpect(status().isCreated());

        // Validate the Rappel in the database
        List<Rappel> rappelList = rappelRepository.findAll();
        assertThat(rappelList).hasSize(databaseSizeBeforeCreate + 1);
        Rappel testRappel = rappelList.get(rappelList.size() - 1);
        assertThat(testRappel.getDateDebut()).isEqualTo(DEFAULT_DATE_DEBUT);
        assertThat(testRappel.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);
        assertThat(testRappel.getFrequence()).isEqualTo(DEFAULT_FREQUENCE);
        assertThat(testRappel.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createRappelWithExistingId() throws Exception {
        // Create the Rappel with an existing ID
        rappel.setId(1L);

        int databaseSizeBeforeCreate = rappelRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRappelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rappel)))
            .andExpect(status().isBadRequest());

        // Validate the Rappel in the database
        List<Rappel> rappelList = rappelRepository.findAll();
        assertThat(rappelList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDateDebutIsRequired() throws Exception {
        int databaseSizeBeforeTest = rappelRepository.findAll().size();
        // set the field null
        rappel.setDateDebut(null);

        // Create the Rappel, which fails.

        restRappelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rappel)))
            .andExpect(status().isBadRequest());

        List<Rappel> rappelList = rappelRepository.findAll();
        assertThat(rappelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFrequenceIsRequired() throws Exception {
        int databaseSizeBeforeTest = rappelRepository.findAll().size();
        // set the field null
        rappel.setFrequence(null);

        // Create the Rappel, which fails.

        restRappelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rappel)))
            .andExpect(status().isBadRequest());

        List<Rappel> rappelList = rappelRepository.findAll();
        assertThat(rappelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRappels() throws Exception {
        // Initialize the database
        rappelRepository.saveAndFlush(rappel);

        // Get all the rappelList
        restRappelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rappel.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())))
            .andExpect(jsonPath("$.[*].frequence").value(hasItem(DEFAULT_FREQUENCE.intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getRappel() throws Exception {
        // Initialize the database
        rappelRepository.saveAndFlush(rappel);

        // Get the rappel
        restRappelMockMvc
            .perform(get(ENTITY_API_URL_ID, rappel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rappel.getId().intValue()))
            .andExpect(jsonPath("$.dateDebut").value(DEFAULT_DATE_DEBUT.toString()))
            .andExpect(jsonPath("$.dateFin").value(DEFAULT_DATE_FIN.toString()))
            .andExpect(jsonPath("$.frequence").value(DEFAULT_FREQUENCE.intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingRappel() throws Exception {
        // Get the rappel
        restRappelMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRappel() throws Exception {
        // Initialize the database
        rappelRepository.saveAndFlush(rappel);

        int databaseSizeBeforeUpdate = rappelRepository.findAll().size();

        // Update the rappel
        Rappel updatedRappel = rappelRepository.findById(rappel.getId()).get();
        // Disconnect from session so that the updates on updatedRappel are not directly saved in db
        em.detach(updatedRappel);
        updatedRappel.dateDebut(UPDATED_DATE_DEBUT).dateFin(UPDATED_DATE_FIN).frequence(UPDATED_FREQUENCE).description(UPDATED_DESCRIPTION);

        restRappelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRappel.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedRappel))
            )
            .andExpect(status().isOk());

        // Validate the Rappel in the database
        List<Rappel> rappelList = rappelRepository.findAll();
        assertThat(rappelList).hasSize(databaseSizeBeforeUpdate);
        Rappel testRappel = rappelList.get(rappelList.size() - 1);
        assertThat(testRappel.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testRappel.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
        assertThat(testRappel.getFrequence()).isEqualTo(UPDATED_FREQUENCE);
        assertThat(testRappel.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingRappel() throws Exception {
        int databaseSizeBeforeUpdate = rappelRepository.findAll().size();
        rappel.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRappelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rappel.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rappel))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rappel in the database
        List<Rappel> rappelList = rappelRepository.findAll();
        assertThat(rappelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRappel() throws Exception {
        int databaseSizeBeforeUpdate = rappelRepository.findAll().size();
        rappel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRappelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rappel))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rappel in the database
        List<Rappel> rappelList = rappelRepository.findAll();
        assertThat(rappelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRappel() throws Exception {
        int databaseSizeBeforeUpdate = rappelRepository.findAll().size();
        rappel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRappelMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rappel)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Rappel in the database
        List<Rappel> rappelList = rappelRepository.findAll();
        assertThat(rappelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRappelWithPatch() throws Exception {
        // Initialize the database
        rappelRepository.saveAndFlush(rappel);

        int databaseSizeBeforeUpdate = rappelRepository.findAll().size();

        // Update the rappel using partial update
        Rappel partialUpdatedRappel = new Rappel();
        partialUpdatedRappel.setId(rappel.getId());

        partialUpdatedRappel.dateDebut(UPDATED_DATE_DEBUT).frequence(UPDATED_FREQUENCE).description(UPDATED_DESCRIPTION);

        restRappelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRappel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRappel))
            )
            .andExpect(status().isOk());

        // Validate the Rappel in the database
        List<Rappel> rappelList = rappelRepository.findAll();
        assertThat(rappelList).hasSize(databaseSizeBeforeUpdate);
        Rappel testRappel = rappelList.get(rappelList.size() - 1);
        assertThat(testRappel.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testRappel.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);
        assertThat(testRappel.getFrequence()).isEqualTo(UPDATED_FREQUENCE);
        assertThat(testRappel.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateRappelWithPatch() throws Exception {
        // Initialize the database
        rappelRepository.saveAndFlush(rappel);

        int databaseSizeBeforeUpdate = rappelRepository.findAll().size();

        // Update the rappel using partial update
        Rappel partialUpdatedRappel = new Rappel();
        partialUpdatedRappel.setId(rappel.getId());

        partialUpdatedRappel
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .frequence(UPDATED_FREQUENCE)
            .description(UPDATED_DESCRIPTION);

        restRappelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRappel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRappel))
            )
            .andExpect(status().isOk());

        // Validate the Rappel in the database
        List<Rappel> rappelList = rappelRepository.findAll();
        assertThat(rappelList).hasSize(databaseSizeBeforeUpdate);
        Rappel testRappel = rappelList.get(rappelList.size() - 1);
        assertThat(testRappel.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testRappel.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
        assertThat(testRappel.getFrequence()).isEqualTo(UPDATED_FREQUENCE);
        assertThat(testRappel.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingRappel() throws Exception {
        int databaseSizeBeforeUpdate = rappelRepository.findAll().size();
        rappel.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRappelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, rappel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rappel))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rappel in the database
        List<Rappel> rappelList = rappelRepository.findAll();
        assertThat(rappelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRappel() throws Exception {
        int databaseSizeBeforeUpdate = rappelRepository.findAll().size();
        rappel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRappelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rappel))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rappel in the database
        List<Rappel> rappelList = rappelRepository.findAll();
        assertThat(rappelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRappel() throws Exception {
        int databaseSizeBeforeUpdate = rappelRepository.findAll().size();
        rappel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRappelMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(rappel)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Rappel in the database
        List<Rappel> rappelList = rappelRepository.findAll();
        assertThat(rappelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRappel() throws Exception {
        // Initialize the database
        rappelRepository.saveAndFlush(rappel);

        int databaseSizeBeforeDelete = rappelRepository.findAll().size();

        // Delete the rappel
        restRappelMockMvc
            .perform(delete(ENTITY_API_URL_ID, rappel.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Rappel> rappelList = rappelRepository.findAll();
        assertThat(rappelList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

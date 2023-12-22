package polytech.g6.blog.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
import polytech.g6.blog.domain.Etablissement;
import polytech.g6.blog.repository.ChambreRepository;

/**
 * Integration tests for the {@link ChambreResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ChambreResourceIT {

    private static final String DEFAULT_NUM_C = "AAAAAAAAAA";
    private static final String UPDATED_NUM_C = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/chambres";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ChambreRepository chambreRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restChambreMockMvc;

    private Chambre chambre;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Chambre createEntity(EntityManager em) {
        Chambre chambre = new Chambre().numC(DEFAULT_NUM_C);
        // Add required entity
        Etablissement etablissement;
        if (TestUtil.findAll(em, Etablissement.class).isEmpty()) {
            etablissement = EtablissementResourceIT.createEntity(em);
            em.persist(etablissement);
            em.flush();
        } else {
            etablissement = TestUtil.findAll(em, Etablissement.class).get(0);
        }
        chambre.setEtablissement(etablissement);
        return chambre;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Chambre createUpdatedEntity(EntityManager em) {
        Chambre chambre = new Chambre().numC(UPDATED_NUM_C);
        // Add required entity
        Etablissement etablissement;
        if (TestUtil.findAll(em, Etablissement.class).isEmpty()) {
            etablissement = EtablissementResourceIT.createUpdatedEntity(em);
            em.persist(etablissement);
            em.flush();
        } else {
            etablissement = TestUtil.findAll(em, Etablissement.class).get(0);
        }
        chambre.setEtablissement(etablissement);
        return chambre;
    }

    @BeforeEach
    public void initTest() {
        chambre = createEntity(em);
    }

    @Test
    @Transactional
    void createChambre() throws Exception {
        int databaseSizeBeforeCreate = chambreRepository.findAll().size();
        // Create the Chambre
        restChambreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(chambre)))
            .andExpect(status().isCreated());

        // Validate the Chambre in the database
        List<Chambre> chambreList = chambreRepository.findAll();
        assertThat(chambreList).hasSize(databaseSizeBeforeCreate + 1);
        Chambre testChambre = chambreList.get(chambreList.size() - 1);
        assertThat(testChambre.getNumC()).isEqualTo(DEFAULT_NUM_C);
    }

    @Test
    @Transactional
    void createChambreWithExistingId() throws Exception {
        // Create the Chambre with an existing ID
        chambre.setId(1L);

        int databaseSizeBeforeCreate = chambreRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restChambreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(chambre)))
            .andExpect(status().isBadRequest());

        // Validate the Chambre in the database
        List<Chambre> chambreList = chambreRepository.findAll();
        assertThat(chambreList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNumCIsRequired() throws Exception {
        int databaseSizeBeforeTest = chambreRepository.findAll().size();
        // set the field null
        chambre.setNumC(null);

        // Create the Chambre, which fails.

        restChambreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(chambre)))
            .andExpect(status().isBadRequest());

        List<Chambre> chambreList = chambreRepository.findAll();
        assertThat(chambreList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllChambres() throws Exception {
        // Initialize the database
        chambreRepository.saveAndFlush(chambre);

        // Get all the chambreList
        restChambreMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(chambre.getId().intValue())))
            .andExpect(jsonPath("$.[*].numC").value(hasItem(DEFAULT_NUM_C)));
    }

    @Test
    @Transactional
    void getChambre() throws Exception {
        // Initialize the database
        chambreRepository.saveAndFlush(chambre);

        // Get the chambre
        restChambreMockMvc
            .perform(get(ENTITY_API_URL_ID, chambre.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(chambre.getId().intValue()))
            .andExpect(jsonPath("$.numC").value(DEFAULT_NUM_C));
    }

    @Test
    @Transactional
    void getNonExistingChambre() throws Exception {
        // Get the chambre
        restChambreMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingChambre() throws Exception {
        // Initialize the database
        chambreRepository.saveAndFlush(chambre);

        int databaseSizeBeforeUpdate = chambreRepository.findAll().size();

        // Update the chambre
        Chambre updatedChambre = chambreRepository.findById(chambre.getId()).get();
        // Disconnect from session so that the updates on updatedChambre are not directly saved in db
        em.detach(updatedChambre);
        updatedChambre.numC(UPDATED_NUM_C);

        restChambreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedChambre.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedChambre))
            )
            .andExpect(status().isOk());

        // Validate the Chambre in the database
        List<Chambre> chambreList = chambreRepository.findAll();
        assertThat(chambreList).hasSize(databaseSizeBeforeUpdate);
        Chambre testChambre = chambreList.get(chambreList.size() - 1);
        assertThat(testChambre.getNumC()).isEqualTo(UPDATED_NUM_C);
    }

    @Test
    @Transactional
    void putNonExistingChambre() throws Exception {
        int databaseSizeBeforeUpdate = chambreRepository.findAll().size();
        chambre.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChambreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, chambre.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(chambre))
            )
            .andExpect(status().isBadRequest());

        // Validate the Chambre in the database
        List<Chambre> chambreList = chambreRepository.findAll();
        assertThat(chambreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchChambre() throws Exception {
        int databaseSizeBeforeUpdate = chambreRepository.findAll().size();
        chambre.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChambreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(chambre))
            )
            .andExpect(status().isBadRequest());

        // Validate the Chambre in the database
        List<Chambre> chambreList = chambreRepository.findAll();
        assertThat(chambreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamChambre() throws Exception {
        int databaseSizeBeforeUpdate = chambreRepository.findAll().size();
        chambre.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChambreMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(chambre)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Chambre in the database
        List<Chambre> chambreList = chambreRepository.findAll();
        assertThat(chambreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateChambreWithPatch() throws Exception {
        // Initialize the database
        chambreRepository.saveAndFlush(chambre);

        int databaseSizeBeforeUpdate = chambreRepository.findAll().size();

        // Update the chambre using partial update
        Chambre partialUpdatedChambre = new Chambre();
        partialUpdatedChambre.setId(chambre.getId());

        restChambreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedChambre.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedChambre))
            )
            .andExpect(status().isOk());

        // Validate the Chambre in the database
        List<Chambre> chambreList = chambreRepository.findAll();
        assertThat(chambreList).hasSize(databaseSizeBeforeUpdate);
        Chambre testChambre = chambreList.get(chambreList.size() - 1);
        assertThat(testChambre.getNumC()).isEqualTo(DEFAULT_NUM_C);
    }

    @Test
    @Transactional
    void fullUpdateChambreWithPatch() throws Exception {
        // Initialize the database
        chambreRepository.saveAndFlush(chambre);

        int databaseSizeBeforeUpdate = chambreRepository.findAll().size();

        // Update the chambre using partial update
        Chambre partialUpdatedChambre = new Chambre();
        partialUpdatedChambre.setId(chambre.getId());

        partialUpdatedChambre.numC(UPDATED_NUM_C);

        restChambreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedChambre.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedChambre))
            )
            .andExpect(status().isOk());

        // Validate the Chambre in the database
        List<Chambre> chambreList = chambreRepository.findAll();
        assertThat(chambreList).hasSize(databaseSizeBeforeUpdate);
        Chambre testChambre = chambreList.get(chambreList.size() - 1);
        assertThat(testChambre.getNumC()).isEqualTo(UPDATED_NUM_C);
    }

    @Test
    @Transactional
    void patchNonExistingChambre() throws Exception {
        int databaseSizeBeforeUpdate = chambreRepository.findAll().size();
        chambre.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChambreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, chambre.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(chambre))
            )
            .andExpect(status().isBadRequest());

        // Validate the Chambre in the database
        List<Chambre> chambreList = chambreRepository.findAll();
        assertThat(chambreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchChambre() throws Exception {
        int databaseSizeBeforeUpdate = chambreRepository.findAll().size();
        chambre.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChambreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(chambre))
            )
            .andExpect(status().isBadRequest());

        // Validate the Chambre in the database
        List<Chambre> chambreList = chambreRepository.findAll();
        assertThat(chambreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamChambre() throws Exception {
        int databaseSizeBeforeUpdate = chambreRepository.findAll().size();
        chambre.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChambreMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(chambre)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Chambre in the database
        List<Chambre> chambreList = chambreRepository.findAll();
        assertThat(chambreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteChambre() throws Exception {
        // Initialize the database
        chambreRepository.saveAndFlush(chambre);

        int databaseSizeBeforeDelete = chambreRepository.findAll().size();

        // Delete the chambre
        restChambreMockMvc
            .perform(delete(ENTITY_API_URL_ID, chambre.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Chambre> chambreList = chambreRepository.findAll();
        assertThat(chambreList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

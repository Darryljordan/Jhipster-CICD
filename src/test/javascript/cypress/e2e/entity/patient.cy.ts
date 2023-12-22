import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('Patient e2e test', () => {
  const patientPageUrl = '/patient';
  const patientPageUrlPattern = new RegExp('/patient(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  // const patientSample = {};

  let patient;
  // let chambre;
  // let utilisateur;

  beforeEach(() => {
    cy.login(username, password);
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/chambres',
      body: {"numC":"Executif Gloves"},
    }).then(({ body }) => {
      chambre = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/utilisateurs',
      body: {"dateNaissanceU":"2023-12-21"},
    }).then(({ body }) => {
      utilisateur = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/patients+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/patients').as('postEntityRequest');
    cy.intercept('DELETE', '/api/patients/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/chambres', {
      statusCode: 200,
      body: [chambre],
    });

    cy.intercept('GET', '/api/utilisateurs', {
      statusCode: 200,
      body: [utilisateur],
    });

  });
   */

  afterEach(() => {
    if (patient) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/patients/${patient.id}`,
      }).then(() => {
        patient = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (chambre) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/chambres/${chambre.id}`,
      }).then(() => {
        chambre = undefined;
      });
    }
    if (utilisateur) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/utilisateurs/${utilisateur.id}`,
      }).then(() => {
        utilisateur = undefined;
      });
    }
  });
   */

  it('Patients menu should load Patients page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('patient');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Patient').should('exist');
    cy.url().should('match', patientPageUrlPattern);
  });

  describe('Patient page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(patientPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Patient page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/patient/new$'));
        cy.getEntityCreateUpdateHeading('Patient');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', patientPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/patients',
          body: {
            ...patientSample,
            chambres: chambre,
            utilisateur: utilisateur,
          },
        }).then(({ body }) => {
          patient = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/patients+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [patient],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(patientPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(patientPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details Patient page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('patient');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', patientPageUrlPattern);
      });

      it('edit button click should load edit Patient page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Patient');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', patientPageUrlPattern);
      });

      it('edit button click should load edit Patient page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Patient');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', patientPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of Patient', () => {
        cy.intercept('GET', '/api/patients/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('patient').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', patientPageUrlPattern);

        patient = undefined;
      });
    });
  });

  describe('new Patient page', () => {
    beforeEach(() => {
      cy.visit(`${patientPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Patient');
    });

    it.skip('should create an instance of Patient', () => {
      cy.get(`[data-cy="nomP"]`).type('Generic').should('have.value', 'Generic');

      cy.get(`[data-cy="prenomP"]`).type('Rand Avon Buckinghamshire').should('have.value', 'Rand Avon Buckinghamshire');

      cy.get(`[data-cy="dateNaissanceP"]`).type('2023-12-22').blur().should('have.value', '2023-12-22');

      cy.get(`[data-cy="tailleP"]`).type('66838').should('have.value', '66838');

      cy.get(`[data-cy="sexeP"]`).select('FEMME');

      cy.get(`[data-cy="dateArrivee"]`).type('2023-12-21').blur().should('have.value', '2023-12-21');

      cy.get(`[data-cy="chambres"]`).select(1);
      cy.get(`[data-cy="utilisateur"]`).select([0]);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        patient = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', patientPageUrlPattern);
    });
  });
});

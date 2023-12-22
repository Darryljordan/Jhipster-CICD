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

describe('Mesure e2e test', () => {
  const mesurePageUrl = '/mesure';
  const mesurePageUrlPattern = new RegExp('/mesure(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  // const mesureSample = {};

  let mesure;
  // let patient;

  beforeEach(() => {
    cy.login(username, password);
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/patients',
      body: {"nomP":"orange","prenomP":"withdrawal Directeur","dateNaissanceP":"2023-12-21","tailleP":67253,"sexeP":"FEMME","dateArrivee":"2023-12-21"},
    }).then(({ body }) => {
      patient = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/mesures+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/mesures').as('postEntityRequest');
    cy.intercept('DELETE', '/api/mesures/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/patients', {
      statusCode: 200,
      body: [patient],
    });

  });
   */

  afterEach(() => {
    if (mesure) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/mesures/${mesure.id}`,
      }).then(() => {
        mesure = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
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
   */

  it('Mesures menu should load Mesures page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('mesure');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Mesure').should('exist');
    cy.url().should('match', mesurePageUrlPattern);
  });

  describe('Mesure page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(mesurePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Mesure page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/mesure/new$'));
        cy.getEntityCreateUpdateHeading('Mesure');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', mesurePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/mesures',
          body: {
            ...mesureSample,
            patient: patient,
          },
        }).then(({ body }) => {
          mesure = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/mesures+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [mesure],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(mesurePageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(mesurePageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details Mesure page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('mesure');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', mesurePageUrlPattern);
      });

      it('edit button click should load edit Mesure page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Mesure');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', mesurePageUrlPattern);
      });

      it('edit button click should load edit Mesure page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Mesure');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', mesurePageUrlPattern);
      });

      it.skip('last delete button click should delete instance of Mesure', () => {
        cy.intercept('GET', '/api/mesures/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('mesure').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', mesurePageUrlPattern);

        mesure = undefined;
      });
    });
  });

  describe('new Mesure page', () => {
    beforeEach(() => {
      cy.visit(`${mesurePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Mesure');
    });

    it.skip('should create an instance of Mesure', () => {
      cy.get(`[data-cy="type"]`).select('POIDS');

      cy.get(`[data-cy="date"]`).type('2023-12-22').blur().should('have.value', '2023-12-22');

      cy.get(`[data-cy="valeur"]`).type('77571').should('have.value', '77571');

      cy.get(`[data-cy="patient"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        mesure = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', mesurePageUrlPattern);
    });
  });
});

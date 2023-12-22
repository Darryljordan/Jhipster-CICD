import patient from 'app/entities/patient/patient.reducer';
import mesure from 'app/entities/mesure/mesure.reducer';
import rappel from 'app/entities/rappel/rappel.reducer';
import etablissement from 'app/entities/etablissement/etablissement.reducer';
import repas from 'app/entities/repas/repas.reducer';
import utilisateur from 'app/entities/utilisateur/utilisateur.reducer';
import userRole from 'app/entities/user-role/user-role.reducer';
import chambre from 'app/entities/chambre/chambre.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  patient,
  mesure,
  rappel,
  etablissement,
  repas,
  utilisateur,
  userRole,
  chambre,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;

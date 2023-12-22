import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Patient from './patient';
import Mesure from './mesure';
import Rappel from './rappel';
import Etablissement from './etablissement';
import Repas from './repas';
import Utilisateur from './utilisateur';
import UserRole from './user-role';
import Chambre from './chambre';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="patient/*" element={<Patient />} />
        <Route path="mesure/*" element={<Mesure />} />
        <Route path="rappel/*" element={<Rappel />} />
        <Route path="etablissement/*" element={<Etablissement />} />
        <Route path="repas/*" element={<Repas />} />
        <Route path="utilisateur/*" element={<Utilisateur />} />
        <Route path="user-role/*" element={<UserRole />} />
        <Route path="chambre/*" element={<Chambre />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};

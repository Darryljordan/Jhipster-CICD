import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Chambre from './chambre';
import ChambreDetail from './chambre-detail';
import ChambreUpdate from './chambre-update';
import ChambreDeleteDialog from './chambre-delete-dialog';

const ChambreRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Chambre />} />
    <Route path="new" element={<ChambreUpdate />} />
    <Route path=":id">
      <Route index element={<ChambreDetail />} />
      <Route path="edit" element={<ChambreUpdate />} />
      <Route path="delete" element={<ChambreDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ChambreRoutes;

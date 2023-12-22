import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Mesure from './mesure';
import MesureDetail from './mesure-detail';
import MesureUpdate from './mesure-update';
import MesureDeleteDialog from './mesure-delete-dialog';

const MesureRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Mesure />} />
    <Route path="new" element={<MesureUpdate />} />
    <Route path=":id">
      <Route index element={<MesureDetail />} />
      <Route path="edit" element={<MesureUpdate />} />
      <Route path="delete" element={<MesureDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default MesureRoutes;

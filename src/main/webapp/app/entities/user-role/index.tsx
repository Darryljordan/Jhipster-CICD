import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import UserRole from './user-role';
import UserRoleDetail from './user-role-detail';
import UserRoleUpdate from './user-role-update';
import UserRoleDeleteDialog from './user-role-delete-dialog';

const UserRoleRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<UserRole />} />
    <Route path="new" element={<UserRoleUpdate />} />
    <Route path=":id">
      <Route index element={<UserRoleDetail />} />
      <Route path="edit" element={<UserRoleUpdate />} />
      <Route path="delete" element={<UserRoleDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default UserRoleRoutes;

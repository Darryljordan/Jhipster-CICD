import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IUserRole } from 'app/shared/model/user-role.model';
import { getEntities } from './user-role.reducer';

export const UserRole = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const userRoleList = useAppSelector(state => state.userRole.entities);
  const loading = useAppSelector(state => state.userRole.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="user-role-heading" data-cy="UserRoleHeading">
        <Translate contentKey="nutriCareApp.userRole.home.title">User Roles</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="nutriCareApp.userRole.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/user-role/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="nutriCareApp.userRole.home.createLabel">Create new User Role</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {userRoleList && userRoleList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="nutriCareApp.userRole.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="nutriCareApp.userRole.role">Role</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {userRoleList.map((userRole, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/user-role/${userRole.id}`} color="link" size="sm">
                      {userRole.id}
                    </Button>
                  </td>
                  <td>
                    <Translate contentKey={`nutriCareApp.Role.${userRole.role}`} />
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/user-role/${userRole.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/user-role/${userRole.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/user-role/${userRole.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="nutriCareApp.userRole.home.notFound">No User Roles found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default UserRole;

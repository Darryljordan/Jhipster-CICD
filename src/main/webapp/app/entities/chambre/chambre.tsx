import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IChambre } from 'app/shared/model/chambre.model';
import { getEntities } from './chambre.reducer';

export const Chambre = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const chambreList = useAppSelector(state => state.chambre.entities);
  const loading = useAppSelector(state => state.chambre.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="chambre-heading" data-cy="ChambreHeading">
        <Translate contentKey="nutriCareApp.chambre.home.title">Chambres</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="nutriCareApp.chambre.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/chambre/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="nutriCareApp.chambre.home.createLabel">Create new Chambre</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {chambreList && chambreList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="nutriCareApp.chambre.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="nutriCareApp.chambre.numC">Num C</Translate>
                </th>
                <th>
                  <Translate contentKey="nutriCareApp.chambre.etablissement">Etablissement</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {chambreList.map((chambre, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/chambre/${chambre.id}`} color="link" size="sm">
                      {chambre.id}
                    </Button>
                  </td>
                  <td>{chambre.numC}</td>
                  <td>
                    {chambre.etablissement ? <Link to={`/etablissement/${chambre.etablissement.id}`}>{chambre.etablissement.id}</Link> : ''}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/chambre/${chambre.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/chambre/${chambre.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/chambre/${chambre.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
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
              <Translate contentKey="nutriCareApp.chambre.home.notFound">No Chambres found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Chambre;

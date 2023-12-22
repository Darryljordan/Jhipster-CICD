import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IRepas } from 'app/shared/model/repas.model';
import { getEntities } from './repas.reducer';

export const Repas = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const repasList = useAppSelector(state => state.repas.entities);
  const loading = useAppSelector(state => state.repas.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="repas-heading" data-cy="RepasHeading">
        <Translate contentKey="nutriCareApp.repas.home.title">Repas</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="nutriCareApp.repas.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/repas/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="nutriCareApp.repas.home.createLabel">Create new Repas</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {repasList && repasList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="nutriCareApp.repas.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="nutriCareApp.repas.dateR">Date R</Translate>
                </th>
                <th>
                  <Translate contentKey="nutriCareApp.repas.heureR">Heure R</Translate>
                </th>
                <th>
                  <Translate contentKey="nutriCareApp.repas.epa">Epa</Translate>
                </th>
                <th>
                  <Translate contentKey="nutriCareApp.repas.patient">Patient</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {repasList.map((repas, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/repas/${repas.id}`} color="link" size="sm">
                      {repas.id}
                    </Button>
                  </td>
                  <td>{repas.dateR ? <TextFormat type="date" value={repas.dateR} format={APP_LOCAL_DATE_FORMAT} /> : null}</td>
                  <td>{repas.heureR ? <TextFormat type="date" value={repas.heureR} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{repas.epa}</td>
                  <td>{repas.patient ? <Link to={`/patient/${repas.patient.id}`}>{repas.patient.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/repas/${repas.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/repas/${repas.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/repas/${repas.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
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
              <Translate contentKey="nutriCareApp.repas.home.notFound">No Repas found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Repas;

import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IRappel } from 'app/shared/model/rappel.model';
import { getEntities } from './rappel.reducer';

export const Rappel = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const rappelList = useAppSelector(state => state.rappel.entities);
  const loading = useAppSelector(state => state.rappel.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="rappel-heading" data-cy="RappelHeading">
        <Translate contentKey="nutriCareApp.rappel.home.title">Rappels</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="nutriCareApp.rappel.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/rappel/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="nutriCareApp.rappel.home.createLabel">Create new Rappel</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {rappelList && rappelList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="nutriCareApp.rappel.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="nutriCareApp.rappel.dateDebut">Date Debut</Translate>
                </th>
                <th>
                  <Translate contentKey="nutriCareApp.rappel.dateFin">Date Fin</Translate>
                </th>
                <th>
                  <Translate contentKey="nutriCareApp.rappel.frequence">Frequence</Translate>
                </th>
                <th>
                  <Translate contentKey="nutriCareApp.rappel.description">Description</Translate>
                </th>
                <th>
                  <Translate contentKey="nutriCareApp.rappel.patient">Patient</Translate>
                </th>
                <th>
                  <Translate contentKey="nutriCareApp.rappel.utilisateur">Utilisateur</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {rappelList.map((rappel, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/rappel/${rappel.id}`} color="link" size="sm">
                      {rappel.id}
                    </Button>
                  </td>
                  <td>{rappel.dateDebut ? <TextFormat type="date" value={rappel.dateDebut} format={APP_LOCAL_DATE_FORMAT} /> : null}</td>
                  <td>{rappel.dateFin ? <TextFormat type="date" value={rappel.dateFin} format={APP_LOCAL_DATE_FORMAT} /> : null}</td>
                  <td>{rappel.frequence}</td>
                  <td>{rappel.description}</td>
                  <td>{rappel.patient ? <Link to={`/patient/${rappel.patient.id}`}>{rappel.patient.id}</Link> : ''}</td>
                  <td>{rappel.utilisateur ? <Link to={`/utilisateur/${rappel.utilisateur.id}`}>{rappel.utilisateur.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/rappel/${rappel.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/rappel/${rappel.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/rappel/${rappel.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
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
              <Translate contentKey="nutriCareApp.rappel.home.notFound">No Rappels found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Rappel;

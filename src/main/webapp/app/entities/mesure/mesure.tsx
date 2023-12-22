import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IMesure } from 'app/shared/model/mesure.model';
import { getEntities } from './mesure.reducer';

export const Mesure = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const mesureList = useAppSelector(state => state.mesure.entities);
  const loading = useAppSelector(state => state.mesure.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="mesure-heading" data-cy="MesureHeading">
        <Translate contentKey="nutriCareApp.mesure.home.title">Mesures</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="nutriCareApp.mesure.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/mesure/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="nutriCareApp.mesure.home.createLabel">Create new Mesure</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {mesureList && mesureList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="nutriCareApp.mesure.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="nutriCareApp.mesure.type">Type</Translate>
                </th>
                <th>
                  <Translate contentKey="nutriCareApp.mesure.date">Date</Translate>
                </th>
                <th>
                  <Translate contentKey="nutriCareApp.mesure.valeur">Valeur</Translate>
                </th>
                <th>
                  <Translate contentKey="nutriCareApp.mesure.patient">Patient</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {mesureList.map((mesure, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/mesure/${mesure.id}`} color="link" size="sm">
                      {mesure.id}
                    </Button>
                  </td>
                  <td>
                    <Translate contentKey={`nutriCareApp.TypeMesure.${mesure.type}`} />
                  </td>
                  <td>{mesure.date ? <TextFormat type="date" value={mesure.date} format={APP_LOCAL_DATE_FORMAT} /> : null}</td>
                  <td>{mesure.valeur}</td>
                  <td>{mesure.patient ? <Link to={`/patient/${mesure.patient.id}`}>{mesure.patient.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/mesure/${mesure.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/mesure/${mesure.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/mesure/${mesure.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
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
              <Translate contentKey="nutriCareApp.mesure.home.notFound">No Mesures found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Mesure;

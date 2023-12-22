import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './patient.reducer';

export const PatientDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const patientEntity = useAppSelector(state => state.patient.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="patientDetailsHeading">
          <Translate contentKey="nutriCareApp.patient.detail.title">Patient</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{patientEntity.id}</dd>
          <dt>
            <span id="nomP">
              <Translate contentKey="nutriCareApp.patient.nomP">Nom P</Translate>
            </span>
          </dt>
          <dd>{patientEntity.nomP}</dd>
          <dt>
            <span id="prenomP">
              <Translate contentKey="nutriCareApp.patient.prenomP">Prenom P</Translate>
            </span>
          </dt>
          <dd>{patientEntity.prenomP}</dd>
          <dt>
            <span id="dateNaissanceP">
              <Translate contentKey="nutriCareApp.patient.dateNaissanceP">Date Naissance P</Translate>
            </span>
          </dt>
          <dd>
            {patientEntity.dateNaissanceP ? (
              <TextFormat value={patientEntity.dateNaissanceP} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="tailleP">
              <Translate contentKey="nutriCareApp.patient.tailleP">Taille P</Translate>
            </span>
          </dt>
          <dd>{patientEntity.tailleP}</dd>
          <dt>
            <span id="sexeP">
              <Translate contentKey="nutriCareApp.patient.sexeP">Sexe P</Translate>
            </span>
          </dt>
          <dd>{patientEntity.sexeP}</dd>
          <dt>
            <span id="dateArrivee">
              <Translate contentKey="nutriCareApp.patient.dateArrivee">Date Arrivee</Translate>
            </span>
          </dt>
          <dd>
            {patientEntity.dateArrivee ? <TextFormat value={patientEntity.dateArrivee} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <Translate contentKey="nutriCareApp.patient.chambres">Chambres</Translate>
          </dt>
          <dd>{patientEntity.chambres ? patientEntity.chambres.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/patient" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/patient/${patientEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PatientDetail;

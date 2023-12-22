import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './rappel.reducer';

export const RappelDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const rappelEntity = useAppSelector(state => state.rappel.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="rappelDetailsHeading">
          <Translate contentKey="nutriCareApp.rappel.detail.title">Rappel</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{rappelEntity.id}</dd>
          <dt>
            <span id="dateDebut">
              <Translate contentKey="nutriCareApp.rappel.dateDebut">Date Debut</Translate>
            </span>
          </dt>
          <dd>
            {rappelEntity.dateDebut ? <TextFormat value={rappelEntity.dateDebut} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="dateFin">
              <Translate contentKey="nutriCareApp.rappel.dateFin">Date Fin</Translate>
            </span>
          </dt>
          <dd>{rappelEntity.dateFin ? <TextFormat value={rappelEntity.dateFin} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="frequence">
              <Translate contentKey="nutriCareApp.rappel.frequence">Frequence</Translate>
            </span>
          </dt>
          <dd>{rappelEntity.frequence}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="nutriCareApp.rappel.description">Description</Translate>
            </span>
          </dt>
          <dd>{rappelEntity.description}</dd>
          <dt>
            <Translate contentKey="nutriCareApp.rappel.patient">Patient</Translate>
          </dt>
          <dd>{rappelEntity.patient ? rappelEntity.patient.id : ''}</dd>
          <dt>
            <Translate contentKey="nutriCareApp.rappel.utilisateur">Utilisateur</Translate>
          </dt>
          <dd>{rappelEntity.utilisateur ? rappelEntity.utilisateur.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/rappel" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/rappel/${rappelEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default RappelDetail;

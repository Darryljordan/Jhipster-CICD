import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './mesure.reducer';

export const MesureDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const mesureEntity = useAppSelector(state => state.mesure.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="mesureDetailsHeading">
          <Translate contentKey="nutriCareApp.mesure.detail.title">Mesure</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{mesureEntity.id}</dd>
          <dt>
            <span id="type">
              <Translate contentKey="nutriCareApp.mesure.type">Type</Translate>
            </span>
          </dt>
          <dd>{mesureEntity.type}</dd>
          <dt>
            <span id="date">
              <Translate contentKey="nutriCareApp.mesure.date">Date</Translate>
            </span>
          </dt>
          <dd>{mesureEntity.date ? <TextFormat value={mesureEntity.date} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="valeur">
              <Translate contentKey="nutriCareApp.mesure.valeur">Valeur</Translate>
            </span>
          </dt>
          <dd>{mesureEntity.valeur}</dd>
          <dt>
            <Translate contentKey="nutriCareApp.mesure.patient">Patient</Translate>
          </dt>
          <dd>{mesureEntity.patient ? mesureEntity.patient.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/mesure" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/mesure/${mesureEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default MesureDetail;

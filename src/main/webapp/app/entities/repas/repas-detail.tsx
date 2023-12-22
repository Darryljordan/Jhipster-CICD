import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './repas.reducer';

export const RepasDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const repasEntity = useAppSelector(state => state.repas.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="repasDetailsHeading">
          <Translate contentKey="nutriCareApp.repas.detail.title">Repas</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{repasEntity.id}</dd>
          <dt>
            <span id="dateR">
              <Translate contentKey="nutriCareApp.repas.dateR">Date R</Translate>
            </span>
          </dt>
          <dd>{repasEntity.dateR ? <TextFormat value={repasEntity.dateR} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="heureR">
              <Translate contentKey="nutriCareApp.repas.heureR">Heure R</Translate>
            </span>
          </dt>
          <dd>{repasEntity.heureR ? <TextFormat value={repasEntity.heureR} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="epa">
              <Translate contentKey="nutriCareApp.repas.epa">Epa</Translate>
            </span>
          </dt>
          <dd>{repasEntity.epa}</dd>
          <dt>
            <Translate contentKey="nutriCareApp.repas.patient">Patient</Translate>
          </dt>
          <dd>{repasEntity.patient ? repasEntity.patient.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/repas" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/repas/${repasEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default RepasDetail;

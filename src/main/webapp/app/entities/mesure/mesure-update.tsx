import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IPatient } from 'app/shared/model/patient.model';
import { getEntities as getPatients } from 'app/entities/patient/patient.reducer';
import { IMesure } from 'app/shared/model/mesure.model';
import { TypeMesure } from 'app/shared/model/enumerations/type-mesure.model';
import { getEntity, updateEntity, createEntity, reset } from './mesure.reducer';

export const MesureUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const patients = useAppSelector(state => state.patient.entities);
  const mesureEntity = useAppSelector(state => state.mesure.entity);
  const loading = useAppSelector(state => state.mesure.loading);
  const updating = useAppSelector(state => state.mesure.updating);
  const updateSuccess = useAppSelector(state => state.mesure.updateSuccess);
  const typeMesureValues = Object.keys(TypeMesure);

  const handleClose = () => {
    navigate('/mesure');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getPatients({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...mesureEntity,
      ...values,
      patient: patients.find(it => it.id.toString() === values.patient.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          type: 'POIDS',
          ...mesureEntity,
          patient: mesureEntity?.patient?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="nutriCareApp.mesure.home.createOrEditLabel" data-cy="MesureCreateUpdateHeading">
            <Translate contentKey="nutriCareApp.mesure.home.createOrEditLabel">Create or edit a Mesure</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="mesure-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField label={translate('nutriCareApp.mesure.type')} id="mesure-type" name="type" data-cy="type" type="select">
                {typeMesureValues.map(typeMesure => (
                  <option value={typeMesure} key={typeMesure}>
                    {translate('nutriCareApp.TypeMesure.' + typeMesure)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField label={translate('nutriCareApp.mesure.date')} id="mesure-date" name="date" data-cy="date" type="date" />
              <ValidatedField
                label={translate('nutriCareApp.mesure.valeur')}
                id="mesure-valeur"
                name="valeur"
                data-cy="valeur"
                type="text"
              />
              <ValidatedField
                id="mesure-patient"
                name="patient"
                data-cy="patient"
                label={translate('nutriCareApp.mesure.patient')}
                type="select"
                required
              >
                <option value="" key="0" />
                {patients
                  ? patients.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/mesure" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default MesureUpdate;

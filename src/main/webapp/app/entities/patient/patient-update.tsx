import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IChambre } from 'app/shared/model/chambre.model';
import { getEntities as getChambres } from 'app/entities/chambre/chambre.reducer';
import { IUtilisateur } from 'app/shared/model/utilisateur.model';
import { getEntities as getUtilisateurs } from 'app/entities/utilisateur/utilisateur.reducer';
import { IPatient } from 'app/shared/model/patient.model';
import { Sexe } from 'app/shared/model/enumerations/sexe.model';
import { getEntity, updateEntity, createEntity, reset } from './patient.reducer';

export const PatientUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const chambres = useAppSelector(state => state.chambre.entities);
  const utilisateurs = useAppSelector(state => state.utilisateur.entities);
  const patientEntity = useAppSelector(state => state.patient.entity);
  const loading = useAppSelector(state => state.patient.loading);
  const updating = useAppSelector(state => state.patient.updating);
  const updateSuccess = useAppSelector(state => state.patient.updateSuccess);
  const sexeValues = Object.keys(Sexe);

  const handleClose = () => {
    navigate('/patient');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getChambres({}));
    dispatch(getUtilisateurs({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...patientEntity,
      ...values,
      chambres: chambres.find(it => it.id.toString() === values.chambres.toString()),
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
          sexeP: 'HOMME',
          ...patientEntity,
          chambres: patientEntity?.chambres?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="nutriCareApp.patient.home.createOrEditLabel" data-cy="PatientCreateUpdateHeading">
            <Translate contentKey="nutriCareApp.patient.home.createOrEditLabel">Create or edit a Patient</Translate>
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
                  id="patient-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('nutriCareApp.patient.nomP')}
                id="patient-nomP"
                name="nomP"
                data-cy="nomP"
                type="text"
                validate={{
                  minLength: { value: 1, message: translate('entity.validation.minlength', { min: 1 }) },
                }}
              />
              <ValidatedField
                label={translate('nutriCareApp.patient.prenomP')}
                id="patient-prenomP"
                name="prenomP"
                data-cy="prenomP"
                type="text"
                validate={{
                  minLength: { value: 1, message: translate('entity.validation.minlength', { min: 1 }) },
                }}
              />
              <ValidatedField
                label={translate('nutriCareApp.patient.dateNaissanceP')}
                id="patient-dateNaissanceP"
                name="dateNaissanceP"
                data-cy="dateNaissanceP"
                type="date"
              />
              <ValidatedField
                label={translate('nutriCareApp.patient.tailleP')}
                id="patient-tailleP"
                name="tailleP"
                data-cy="tailleP"
                type="text"
              />
              <ValidatedField label={translate('nutriCareApp.patient.sexeP')} id="patient-sexeP" name="sexeP" data-cy="sexeP" type="select">
                {sexeValues.map(sexe => (
                  <option value={sexe} key={sexe}>
                    {translate('nutriCareApp.Sexe.' + sexe)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('nutriCareApp.patient.dateArrivee')}
                id="patient-dateArrivee"
                name="dateArrivee"
                data-cy="dateArrivee"
                type="date"
              />
              <ValidatedField
                id="patient-chambres"
                name="chambres"
                data-cy="chambres"
                label={translate('nutriCareApp.patient.chambres')}
                type="select"
                required
              >
                <option value="" key="0" />
                {chambres
                  ? chambres.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/patient" replace color="info">
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

export default PatientUpdate;

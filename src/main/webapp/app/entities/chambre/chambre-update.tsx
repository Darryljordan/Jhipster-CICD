import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IEtablissement } from 'app/shared/model/etablissement.model';
import { getEntities as getEtablissements } from 'app/entities/etablissement/etablissement.reducer';
import { IChambre } from 'app/shared/model/chambre.model';
import { getEntity, updateEntity, createEntity, reset } from './chambre.reducer';

export const ChambreUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const etablissements = useAppSelector(state => state.etablissement.entities);
  const chambreEntity = useAppSelector(state => state.chambre.entity);
  const loading = useAppSelector(state => state.chambre.loading);
  const updating = useAppSelector(state => state.chambre.updating);
  const updateSuccess = useAppSelector(state => state.chambre.updateSuccess);

  const handleClose = () => {
    navigate('/chambre');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getEtablissements({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...chambreEntity,
      ...values,
      etablissement: etablissements.find(it => it.id.toString() === values.etablissement.toString()),
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
          ...chambreEntity,
          etablissement: chambreEntity?.etablissement?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="nutriCareApp.chambre.home.createOrEditLabel" data-cy="ChambreCreateUpdateHeading">
            <Translate contentKey="nutriCareApp.chambre.home.createOrEditLabel">Create or edit a Chambre</Translate>
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
                  id="chambre-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('nutriCareApp.chambre.numC')}
                id="chambre-numC"
                name="numC"
                data-cy="numC"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                id="chambre-etablissement"
                name="etablissement"
                data-cy="etablissement"
                label={translate('nutriCareApp.chambre.etablissement')}
                type="select"
                required
              >
                <option value="" key="0" />
                {etablissements
                  ? etablissements.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/chambre" replace color="info">
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

export default ChambreUpdate;

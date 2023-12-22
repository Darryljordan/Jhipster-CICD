import dayjs from 'dayjs';
import { IPatient } from 'app/shared/model/patient.model';
import { TypeMesure } from 'app/shared/model/enumerations/type-mesure.model';

export interface IMesure {
  id?: number;
  type?: TypeMesure | null;
  date?: string | null;
  valeur?: number | null;
  patient?: IPatient;
}

export const defaultValue: Readonly<IMesure> = {};

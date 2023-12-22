import dayjs from 'dayjs';
import { IPatient } from 'app/shared/model/patient.model';

export interface IRepas {
  id?: number;
  dateR?: string | null;
  heureR?: string | null;
  epa?: number | null;
  patient?: IPatient | null;
}

export const defaultValue: Readonly<IRepas> = {};

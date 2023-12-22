import dayjs from 'dayjs';
import { IPatient } from 'app/shared/model/patient.model';
import { IUtilisateur } from 'app/shared/model/utilisateur.model';

export interface IRappel {
  id?: number;
  dateDebut?: string;
  dateFin?: string | null;
  frequence?: number;
  description?: string | null;
  patient?: IPatient | null;
  utilisateur?: IUtilisateur;
}

export const defaultValue: Readonly<IRappel> = {};

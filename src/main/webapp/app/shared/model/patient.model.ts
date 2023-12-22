import dayjs from 'dayjs';
import { IChambre } from 'app/shared/model/chambre.model';
import { IUtilisateur } from 'app/shared/model/utilisateur.model';
import { Sexe } from 'app/shared/model/enumerations/sexe.model';

export interface IPatient {
  id?: number;
  nomP?: string | null;
  prenomP?: string | null;
  dateNaissanceP?: string | null;
  tailleP?: number | null;
  sexeP?: Sexe | null;
  dateArrivee?: string | null;
  chambres?: IChambre;
  utilisateurs?: IUtilisateur[];
}

export const defaultValue: Readonly<IPatient> = {};

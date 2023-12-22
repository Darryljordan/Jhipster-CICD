import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { IUserRole } from 'app/shared/model/user-role.model';
import { IPatient } from 'app/shared/model/patient.model';
import { IEtablissement } from 'app/shared/model/etablissement.model';

export interface IUtilisateur {
  id?: number;
  dateNaissanceU?: string | null;
  user?: IUser | null;
  userRoles?: IUserRole[];
  patients?: IPatient[] | null;
  etablissements?: IEtablissement[];
}

export const defaultValue: Readonly<IUtilisateur> = {};

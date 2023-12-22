import { IUtilisateur } from 'app/shared/model/utilisateur.model';
import { Role } from 'app/shared/model/enumerations/role.model';

export interface IUserRole {
  id?: number;
  role?: Role;
  utilisateurs?: IUtilisateur[] | null;
}

export const defaultValue: Readonly<IUserRole> = {};

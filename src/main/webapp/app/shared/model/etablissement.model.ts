import { IChambre } from 'app/shared/model/chambre.model';
import { IUtilisateur } from 'app/shared/model/utilisateur.model';

export interface IEtablissement {
  id?: number;
  nomE?: string | null;
  adresseE?: string | null;
  chambres?: IChambre[];
  utilisateurs?: IUtilisateur[] | null;
}

export const defaultValue: Readonly<IEtablissement> = {};

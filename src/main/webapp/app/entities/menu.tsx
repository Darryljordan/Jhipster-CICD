import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/patient">
        <Translate contentKey="global.menu.entities.patient" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/mesure">
        <Translate contentKey="global.menu.entities.mesure" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/rappel">
        <Translate contentKey="global.menu.entities.rappel" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/etablissement">
        <Translate contentKey="global.menu.entities.etablissement" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/repas">
        <Translate contentKey="global.menu.entities.repas" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/utilisateur">
        <Translate contentKey="global.menu.entities.utilisateur" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/user-role">
        <Translate contentKey="global.menu.entities.userRole" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/chambre">
        <Translate contentKey="global.menu.entities.chambre" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;

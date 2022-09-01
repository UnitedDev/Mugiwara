package fr.uniteduhc.mugiwara.game.events.haki;

import fr.uniteduhc.mugiwara.game.events.HakiManager;
import fr.uniteduhc.mugiwara.game.events.haki.impl.*;
import fr.uniteduhc.mugiwara.roles.RolesType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.ListResourceBundle;

@Getter
@RequiredArgsConstructor
public enum HakiType {
    ARMEMENT("Haki de l'Armement", ArmementHaki.class, HakiManager.HAKI_ARMEMENT),
    OBSERVATION("Haki de l'Observation", ObservationHaki.class, HakiManager.HAKI_OBSERVATION),
    ROYAL("Haki Royal / Fluide Royal", RoyalHaki.class, HakiManager.HAKI_ROYAL);

    private final String displayName;
    private final Class<? extends AbstractHaki> haki;
    private final List<RolesType> roles;
}

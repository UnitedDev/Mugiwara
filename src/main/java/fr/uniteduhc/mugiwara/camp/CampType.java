package fr.uniteduhc.mugiwara.camp;

import fr.uniteduhc.mugiwara.camp.impl.*;
import fr.uniteduhc.uhc.module.manager.Camp;
import fr.uniteduhc.mugiwara.camp.impl.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CampType {

    MUGIWARA_HEART(new MugiwaraHeartCamp()),
    MARINE(new MarineCamp()),
    BIGMOM_KAIDO(new BigMomKaido()),
    SOLO(new SoloCamp()),
    SABO_KUMA(new SaboKumaCamp());

    private final Camp camp;

    public static abstract class MUCamp extends Camp {

        public abstract CampType getCampType();

    }
}

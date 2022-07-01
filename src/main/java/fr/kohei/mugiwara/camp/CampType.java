package fr.kohei.mugiwara.camp;

import fr.kohei.mugiwara.camp.impl.*;
import fr.kohei.uhc.module.manager.Camp;
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

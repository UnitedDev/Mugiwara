package fr.kohei.mugiwara.roles;

import fr.kohei.mugiwara.camp.CampType;
import fr.kohei.mugiwara.game.player.MUPlayer;
import fr.kohei.mugiwara.power.Power;
import fr.kohei.mugiwara.roles.impl.marine.BartholomewKumaRole;
import fr.kohei.mugiwara.roles.impl.alliance.*;
import fr.kohei.mugiwara.roles.impl.marine.*;
import fr.kohei.mugiwara.roles.impl.mugiwara.*;
import fr.kohei.mugiwara.roles.impl.marine.XDrakeRole;
import fr.kohei.uhc.module.manager.Camp;
import fr.kohei.uhc.module.manager.Role;
import fr.kohei.utils.ChatUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.List;

@Getter
@RequiredArgsConstructor
public enum RolesType {

    LUFFY("Monkey D. Luffy", CampType.MUGIWARA_HEART, LuffyRole.class),
    ZORO("Roronoa Zoro", CampType.MUGIWARA_HEART, ZoroRole.class),
    NAMI("Nami", CampType.MUGIWARA_HEART, NamiRole.class),
    USSOP("Ussop", CampType.MUGIWARA_HEART, UssopRole.class),
    SANJI("Vinsmoke Sanji", CampType.MUGIWARA_HEART, SanjiRole.class),
    CHOPPER("Tony Tony Chopper", CampType.MUGIWARA_HEART, ChopperRole.class),
    ROBIN("Nico Robin", CampType.MUGIWARA_HEART, RobinRole.class),
    FRANKY("Franky", CampType.MUGIWARA_HEART, FrankyRole.class),
    BROOK("Brook", CampType.MUGIWARA_HEART, BrookRole.class),
    JIMBE("Jimbe", CampType.MUGIWARA_HEART, JimbeRole.class),
    LAW("Trafalgar D. Water Law", CampType.MUGIWARA_HEART, LawRole.class),
    EUSTASS("Eustass Kid", CampType.MUGIWARA_HEART, null),
    PIRATE("Pirate", CampType.MUGIWARA_HEART, PirateRole.class),


    GARP("Monkey D. Garp", CampType.MARINE, GarpRole.class),
    COBY("Coby ", CampType.MARINE, CobyRole.class),
    DRAKE("X Drake", CampType.MARINE, XDrakeRole.class),


    COMMANDANT("Commandant", CampType.MARINE, CommandantRole.class),
    LIEUTENANT("Lieutenant", CampType.MARINE, LieutenantRole.class),
    MIHAWK("Mihawk", CampType.MARINE, MihawkRole.class),
    SMOKER("Smoker", CampType.MARINE, SmokerRole.class),
    HANCOCK("Boa Hancock", CampType.MARINE, HancockRole.class),
    KIZARU("Kizaru", CampType.MARINE, KizaruRole.class),
    FUJITORA("Amiral Fujitora", CampType.MARINE, null),
    AKAINU("Chef Akainu", CampType.MARINE, AkainuRole.class),
    SENGOKU("Sengoku", CampType.MARINE, null),
    KUMA("Bartholomew Kuma", CampType.MARINE, BartholomewKumaRole.class),
    SOLDAT("Soldat", CampType.MARINE, SoldatRole.class),
    TSURU("Tsuru", CampType.MARINE, null),


    KAIDO("Kaidö", CampType.BIGMOM_KAIDO, null),
    KING("King", CampType.BIGMOM_KAIDO, null),
    QUEEN("Queen", CampType.BIGMOM_KAIDO, null),
    JACK("Jack", CampType.BIGMOM_KAIDO, JackRole.class),
    KATAKURI("Charlotte Katakuri", CampType.BIGMOM_KAIDO, CharlotteKatakuriRole.class),
    BIG_MOM("Big Mom", CampType.BIGMOM_KAIDO, null),

    SABO("Sabo", CampType.SOLO, null),
    TEACH("Marshall D. Teach", CampType.SOLO, null),


    ;

    private final String name;
    private final CampType campType;
    private final Class<? extends MURole> roleClass;

    @Getter
    @Setter
    public static abstract class MURole extends Role {

        private List<Power> powers;

        public MURole(List<Power> powers) {
            this.powers = powers;
        }

        public abstract RolesType getRole();

        public void onClaim() {
            getPlayer().sendMessage(ChatUtil.prefix("&cVous n'avez aucun item à claim..."));
        }

        public double getStrengthBuffer() {
            return 1.0;
        }

        public double getResistanceBuffer() {
            return 1.0;
        }

        public void onAllUse(Player player, Player use) {

        }

        @Override
        public String getName() {
            return getRole().getName();
        }

        @Override
        public Camp getStartCamp() {
            return getRole().getCampType().getCamp();
        }

        public boolean isRole(Player player) {
            return (MUPlayer.get(player).getRole().getName().equalsIgnoreCase(getName()));
        }
    }
}

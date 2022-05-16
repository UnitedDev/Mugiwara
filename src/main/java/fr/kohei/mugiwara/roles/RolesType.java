package fr.kohei.mugiwara.roles;

import fr.kohei.mugiwara.camp.CampType;
import fr.kohei.mugiwara.game.MUPlayer;
import fr.kohei.mugiwara.power.Power;
import fr.kohei.mugiwara.roles.impl.mugiwara.LuffyRole;
import fr.kohei.mugiwara.roles.impl.mugiwara.ZoroRole;
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
    NAMI("Nami", CampType.MUGIWARA_HEART, null),
    USSOP("Ussop", CampType.MUGIWARA_HEART, null),
    SANJI("Vinsmoke Sanji", CampType.MUGIWARA_HEART, null),
    CHOPPER("Tony Tony Chopper", CampType.MUGIWARA_HEART, null),
    ROBIN("Nico Robin", CampType.MUGIWARA_HEART, null),
    FRANKY("Franky", CampType.MUGIWARA_HEART, null),
    BROOK("Brook", CampType.MUGIWARA_HEART, null),
    JIMBE("Jimbe", CampType.MUGIWARA_HEART, null),
    LAW("Trafalgar D. Water Law", CampType.MUGIWARA_HEART, null),
    EUSTASS("Eustass Kid", CampType.MUGIWARA_HEART, null),
    PIRATE("Pirate", CampType.MUGIWARA_HEART, null),


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

        public void onAllUse(Player player) {

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

package fr.uniteduhc.mugiwara.roles.marine;

import fr.uniteduhc.mugiwara.power.Power;
import fr.uniteduhc.mugiwara.power.impl.AnalysePower;
import fr.uniteduhc.mugiwara.power.impl.LazerLeftPower;
import fr.uniteduhc.mugiwara.power.impl.LazerRightPower;
import fr.uniteduhc.mugiwara.roles.RolesType;
import fr.uniteduhc.mugiwara.utils.config.Messages;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;

public class PacifistaRole extends RolesType.MURole {

    public PacifistaRole() {
        super(Arrays.asList(
                new AnalysePower(),
                new LazerRightPower(),
                new LazerLeftPower()
        ), 0L);
    }

    @Override
    public RolesType getRole() {
        return RolesType.PACIFISTA;
    }

    @Override
    public void onDistribute(Player player) {

        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));

    }

    @Override
    public void onDeath(Player player, Player killer) {

        Power power = getPowers().stream()
                .filter(power1 -> power1 instanceof AnalysePower)
                .findFirst()
                .orElse(null);

        if (power == null) return;

        AnalysePower analysePower = (AnalysePower) power;

        if (killer != getPlayer() && player != analysePower.getCible()) return;

        if (!analysePower.isUsed()) return;

        analysePower.setUsed(false);
        analysePower.setCanUse(true);
        Messages.PACIFISTA_ANALYSE_CAN.send(getPlayer());

    }

}

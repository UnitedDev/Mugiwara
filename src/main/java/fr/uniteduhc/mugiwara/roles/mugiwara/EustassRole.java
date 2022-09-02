package fr.uniteduhc.mugiwara.roles.mugiwara;

import fr.uniteduhc.mugiwara.Mugiwara;
import fr.uniteduhc.mugiwara.power.impl.AssignPower;
import fr.uniteduhc.mugiwara.power.impl.RepelPower;
import fr.uniteduhc.mugiwara.roles.RolesType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.Random;

public class EustassRole extends RolesType.MURole {

    private int inWater = 0;

    private int time;

    public EustassRole() {
        super(Arrays.asList(
                new RepelPower(),
                new AssignPower()
        ), 470000000);
    }

    @Override
    public RolesType getRole() {
        return RolesType.EUSTASS;
    }

    @Override
    public void onDistribute(Player player) {
        Mugiwara.knowsRole(player, RolesType.LAW);
    }

    @Override
    public void onSecond(Player player) {


        if (time % 600 == 0) {
            knowEmpereur(player);
        }

        time++;

        if (isInWater(player)) {
            this.inWater++;
        } else {
            this.inWater = 0;
        }

        if (this.inWater >= 5) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 6 * 20, 0, false, false));
            //Messages.WATER.send(player);
            this.inWater = 0;
        }

    }

    public void knowEmpereur(Player player) {
        int result = (new Random()).nextInt(4);

        if (result == 1) {
            Mugiwara.knowsRole(player, RolesType.KAIDO);
        } else if (result == 2) {
            Mugiwara.knowsRole(player, RolesType.BIG_MOM);
        } else if (result == 3) {
            Mugiwara.knowsRole(player, RolesType.TEACH);
        } else {
            Mugiwara.knowsRole(player, RolesType.LUFFY);
        }

    }

    public boolean isInWater(Player player) {
        return player.getLocation().clone().getBlock().getType().name().contains("WATER") || player.getLocation().clone().add(0, -1, 0).getBlock().getType().name().contains("WATER");
    }
}

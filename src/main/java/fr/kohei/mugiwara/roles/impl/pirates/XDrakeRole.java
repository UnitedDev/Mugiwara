package fr.kohei.mugiwara.roles.impl.pirates;

import fr.kohei.mugiwara.config.Messages;
import fr.kohei.mugiwara.power.Power;
import fr.kohei.mugiwara.power.impl.AllosaurusPower;
import fr.kohei.mugiwara.power.impl.MorsurePower;
import fr.kohei.mugiwara.roles.RolesType;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.List;

/**
 * @author Salers
 * made on fr.kohei.mugiwara.roles.impl.pirates
 */
public class XDrakeRole extends RolesType.MURole {

    private int inWater;

    public XDrakeRole() {
        super(Arrays.asList(
                new AllosaurusPower(),
                new MorsurePower()
        ));
    }

    @Override
    public RolesType getRole() {
        return RolesType.DRAKE;
    }

    @Override
    public ItemStack getItem() {
        return null;
    }

    @Override
    public void onSecond(Player player) {

        final Block block = player.getLocation().getBlock();

        if (block.getType() == Material.STATIONARY_WATER || block.getType() == Material.WATER) this.inWater += 1;
        else this.inWater = 0;

        if (this.inWater >= 5) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 6 * 20, 2, false, false));
            Messages.WATER.send(player);
            this.inWater = 0;
        }




    }

    @Override
    public void onDistribute(Player player) {
    }

    @Override
    public String[] getDescription() {
        return new String[0];
    }
}

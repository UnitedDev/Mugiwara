package fr.kohei.mugiwara.roles.marine;

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

public class XDrakeRole extends RolesType.MURole {

    private int inWater;

    public XDrakeRole() {
        super(Arrays.asList(
                new AllosaurusPower(),
                new MorsurePower()
        ), 222000000L);
    }

    @Override
    public RolesType getRole() {
        return RolesType.DRAKE;
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(Material.ROTTEN_FLESH);
    }

    @Override
    public void onDistribute(Player player) {
        player.setWalkSpeed(0.21F);
    }

    @Override
    public void onSecond(Player player) {

        final Block block = player.getLocation().getBlock();

        if (block.getType() == Material.STATIONARY_WATER || block.getType() == Material.WATER) this.inWater++;
        else this.inWater = 0;

        if (this.inWater >= 5) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 6 * 20, 2, false, false));
            //Messages.WATER.send(player);
            this.inWater = 0;
        }
    }

    @Override
    public double getStrengthBuffer() {
        return 1.05F;
    }

    @Override
    public double getResistanceBuffer() {
        return 0.95F;
    }
}

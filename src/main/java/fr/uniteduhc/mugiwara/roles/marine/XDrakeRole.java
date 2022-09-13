package fr.uniteduhc.mugiwara.roles.marine;

import fr.uniteduhc.mugiwara.power.impl.AllosaurusPower;
import fr.uniteduhc.mugiwara.power.impl.MorsurePower;
import fr.uniteduhc.mugiwara.roles.RolesType;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;

public class XDrakeRole extends RolesType.MURole {

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
    public boolean hasFruit() {
        return true;
    }

    @Override
    public void onSecond(Player player) {
        super.onSecond(player);
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

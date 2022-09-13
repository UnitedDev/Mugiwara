package fr.uniteduhc.mugiwara.roles.mugiwara;

import fr.uniteduhc.mugiwara.power.impl.FlairePower;
import fr.uniteduhc.mugiwara.power.impl.FormePower;
import fr.uniteduhc.mugiwara.roles.RolesType;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;

@Setter
@Getter
public class ChopperRole extends RolesType.MURole {

    private FormePower.FormeTypes type;

    public ChopperRole() {
        super(Arrays.asList(
                new FlairePower(),
                new FormePower()
        ), 100L);
    }

    @Override
    public RolesType getRole() {
        return RolesType.CHOPPER;
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(Material.LEATHER);
    }

    @Override
    public void onDistribute(Player player) {
        player.setMaxHealth(player.getMaxHealth() + 4);
        player.getInventory().addItem(new ItemStack(Material.POTION, 2, (short) 16421));
        player.getInventory().addItem(new ItemStack(Material.POTION, 1, (short) 8193));

        player.setWalkSpeed(0.21F);

        int random = (int) (Math.random() * 3);
        if (random == 1) ZoroRole.randomRole(player);
    }

    @Override
    public double getStrengthBuffer() {
        return 1.05F;
    }

    @Override
    public double getResistanceBuffer() {
        return 0.95F;
    }

    @Override
    public boolean hasFruit() {
        return true;
    }

    @Override
    public void onSecond(Player player) {
        super.onSecond(player);
    }
}

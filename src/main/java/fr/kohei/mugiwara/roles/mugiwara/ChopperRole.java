package fr.kohei.mugiwara.roles.mugiwara;

import fr.kohei.mugiwara.power.impl.FlairePower;
import fr.kohei.mugiwara.power.impl.FormePower;
import fr.kohei.mugiwara.roles.RolesType;
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
    private int inWater = 0;
    private FormePower.FormeTypes type;

    public ChopperRole() {
        super(Arrays.asList(
                new FlairePower(),
                new FormePower()
        ));
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
    public void onSecond(Player player) {
        Block block = player.getLocation().getBlock();

        if (block.getType() == Material.STATIONARY_WATER || block.getType() == Material.WATER) this.inWater++;
        else this.inWater = 0;

        if (this.inWater >= 5) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 6 * 20, 2, false, false));
            //Messages.WATER.send(player);
            this.inWater = 0;
        }
    }
}

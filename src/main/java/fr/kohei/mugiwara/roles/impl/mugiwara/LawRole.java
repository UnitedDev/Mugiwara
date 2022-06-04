package fr.kohei.mugiwara.roles.impl.mugiwara;

import fr.kohei.mugiwara.Mugiwara;
import fr.kohei.mugiwara.config.Messages;
import fr.kohei.mugiwara.roles.RolesType;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;

public class LawRole extends RolesType.MURole {
    private int inWater = 0;

    public LawRole() {
        super(Arrays.asList());
    }

    @Override
    public RolesType getRole() {
        return RolesType.LAW;
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(Material.SHEARS);
    }

    @Override
    public String[] getDescription() {
        return new String[0];
    }

    @Override
    public void onDistribute(Player player) {
        Mugiwara.knowsRole(player, RolesType.LUFFY);

        player.getInventory().addItem(new ItemStack(Material.POTION, 2, (short) 16460));
        player.getInventory().addItem(new ItemStack(Material.POTION, 1, (short) 8193));
    }

    @Override
    public void onSecond(Player player) {
        Block block = player.getLocation().getBlock();

        if (block.getType() == Material.STATIONARY_WATER || block.getType() == Material.WATER) this.inWater += 1;
        else this.inWater = 0;

        if (this.inWater >= 5) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 6 * 20, 2, false, false));
            Messages.WATER.send(player);
            this.inWater = 0;
        }
    }
}

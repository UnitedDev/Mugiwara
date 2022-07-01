package fr.kohei.mugiwara.roles.impl.marine;

import fr.kohei.mugiwara.power.Power;
import fr.kohei.mugiwara.power.impl.MeteoritePower;
import fr.kohei.mugiwara.power.impl.MokoPower;
import fr.kohei.mugiwara.roles.RolesType;
import fr.kohei.mugiwara.utils.config.Messages;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.List;

public class FujitoraRole extends RolesType.MURole {
    private int inWater = 0;

    public FujitoraRole() {
        super(Arrays.asList(
                new MokoPower(),
                new MeteoritePower()
        ));
    }

    @Override
    public RolesType getRole() {
        return RolesType.FUJITORA;
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(Material.STAINED_GLASS, 1, (short) 11);
    }

    @Override
    public void onDistribute(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));
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

    @Override
    public String[] getDescription() {
        return new String[0];
    }
}

package fr.kohei.mugiwara.power.impl;

import fr.kohei.mugiwara.Mugiwara;
import fr.kohei.mugiwara.config.Messages;
import fr.kohei.mugiwara.power.RightClickPower;
import fr.kohei.mugiwara.utils.Utils;
import fr.kohei.utils.Cuboid;
import fr.kohei.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashSet;

public class FiolePower extends RightClickPower {
    @Override
    public ItemStack getItem() {

        // item stack with an empty potion
        return new ItemBuilder(Material.GLASS_BOTTLE).setName(Utils.itemFormat("Fiole")).toItemStack();
    }

    @Override
    public String getName() {
        return "Fiole";
    }

    @Override
    public Integer getCooldownAmount() {
        // 7 minute cooldown
        return 7 * 60;
    }

    @Override
    public boolean onEnable(Player player, boolean rightClick) {
        // if the player is facing a water block, if not return false
        Block block = player.getTargetBlock((HashSet<Byte>) null, 4);
        if (block == null || block.getType() != Material.WATER) {
            return false;
        }

        // run a task 5 ticks after the player right clicked
        player.getServer().getScheduler().runTaskLater(player.getServer().getPluginManager().getPlugin("Mugiwara"), () -> {
            // replace all potion in his inventory by glass bottles with a for loop
            for (int i = 0; i < player.getInventory().getSize(); i++) {
                ItemStack item = player.getInventory().getItem(i);
                if (item != null && item.getType() == Material.POTION) {
                    player.getInventory().setItem(i, new ItemStack(Material.GLASS_BOTTLE));
                }
            }
        }, 5);

        // add strength to the player for 1m30s
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 90 * 20, 0, false, false));

        // send the fiole use message to the player
        Messages.AKAINU_FIOLE_USE.send(player);

        // send the fiole end message to the player after 1m30s
        player.getServer().getScheduler().runTaskLater(Mugiwara.getInstance(), () -> {
            Messages.AKAINU_FIOLE_END.send(player);
        }, 90 * 20);

        // remove all the water in a 3 block radius around the player using Cuboid class
        Cuboid cuboid = new Cuboid(player.getLocation().clone().add(-1, -1, -1), player.getLocation().clone().add(1, 1, 1));
        cuboid.getBlockList().forEach(block1 -> {
            if (block1.getType() == Material.WATER) {
                block1.setType(Material.AIR);
            }
        });

        return true;
    }
}

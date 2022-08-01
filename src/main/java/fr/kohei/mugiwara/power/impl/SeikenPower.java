package fr.kohei.mugiwara.power.impl;

import fr.kohei.mugiwara.utils.config.Messages;
import fr.kohei.mugiwara.utils.config.Replacement;
import fr.kohei.mugiwara.power.DamagePlayerPower;
import fr.kohei.mugiwara.utils.utils.Utils;
import fr.kohei.utils.ItemBuilder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class SeikenPower extends DamagePlayerPower {
    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.RAW_FISH).addEnchant(Enchantment.DAMAGE_ALL, 1).hideItemFlags()
                .setName(Utils.notClickItem("&9&lGosenmaigawara Seiken")).toItemStack();
    }

    @Override
    public boolean onEnable(Player player, Player target, EntityDamageByEntityEvent eevnt) {
        Messages.JIMBE_SEIKEN_USE.send(player, new Replacement("<name>", target.getName()));
        Messages.JIMBE_SEIKEN_ONME.send(target);
        final Location loc = target.getLocation().clone();
        for (int x = -1; x <= 1; ++x) {
            for (int z = -1; z <= 1; ++z) {
                for (int y = 0; y <= 10; ++y) {
                    final Block block = target.getWorld().getBlockAt(loc.getBlockX() + x, loc.getBlockY() + y, loc.getBlockZ() + z);
                    if (block != null && block.getType() != null && block.getType() != Material.AIR && block.getType() != Material.REDSTONE_BLOCK) {
                        block.setType(Material.AIR);
                    }
                }
            }
        }
        target.teleport(loc.add(0.0, 10.0, 0.0));
        target.setHealth(target.getHealth() - 4);
        return true;
    }

    @Override
    public String getName() {
        return "Gosenmaigawara Seiken";
    }

    @Override
    public Integer getCooldownAmount() {
        return 3 * 60;
    }
}

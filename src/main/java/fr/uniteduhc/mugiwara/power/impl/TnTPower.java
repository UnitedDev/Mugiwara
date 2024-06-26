package fr.uniteduhc.mugiwara.power.impl;

import fr.uniteduhc.mugiwara.Mugiwara;
import fr.uniteduhc.mugiwara.utils.config.Messages;
import fr.uniteduhc.mugiwara.power.BlockPlacePower;
import fr.uniteduhc.mugiwara.utils.utils.packets.Damage;
import fr.uniteduhc.mugiwara.utils.utils.Utils;
import fr.uniteduhc.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class TnTPower extends BlockPlacePower {
    @Override
    public boolean onEnable(Player player, Location location) {
        Utils.getNearPlayers(player, 6).forEach(player1 -> {
            Damage.addTempNoDamage(player1, EntityDamageEvent.DamageCause.ENTITY_EXPLOSION, 10);
            if(player1.getHealth() - 6 <= 0) player.setHealth(1);
            else player1.setHealth(player1.getHealth() - 6);
        });

        Messages.GARP_TNT_USE.send(player);
        location.getWorld().createExplosion(location, 4f);
        final UUID uuid = player.getUniqueId();
        new BukkitRunnable() {
            @Override
            public void run() {
                Player player = Bukkit.getPlayer(uuid);
                if (player == null) return;

                ItemBuilder item = new ItemBuilder(getItem());
                item.setAmount(1);
                player.getInventory().addItem(item.toItemStack());
            }
        }.runTaskLater(Mugiwara.getInstance(), 3 * 20 * 60);
        return true;
    }

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.TNT, 2).setName(Utils.notClickItem("&7&lCanon")).toItemStack();
    }

    @Override
    public String getName() {
        return "Canon";
    }

    @Override
    public Integer getCooldownAmount() {
        return 3 * 60;
    }
}

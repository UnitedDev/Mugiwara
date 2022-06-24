package fr.kohei.mugiwara.power.impl;

import fr.kohei.mugiwara.utils.config.Messages;
import fr.kohei.mugiwara.power.DamagePlayerPower;
import fr.kohei.mugiwara.utils.utils.Utils;
import fr.kohei.utils.ChatUtil;
import fr.kohei.utils.ItemBuilder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class VoyagePower extends DamagePlayerPower {

    private int maxUses = 2;

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.PRISMARINE_SHARD).setName(Utils.itemFormat("Voyage")).toItemStack();
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public Integer getCooldownAmount() {
        return null;
    }

    @Override
    public boolean onEnable(Player player, Player target, EntityDamageByEntityEvent event) {
        if(maxUses < 2) {
            player.sendMessage(ChatUtil.prefix("&cVous avez atteint la limite d'utilisation pour ce pouvoir !"));
            return false;
        }

        maxUses++;
        Messages.KUMA_VOYAGE_PLAYER_USE.send(player);

        int x = (int) (target.getWorld().getWorldBorder().getSize() * Math.random());
        int z = (int) (target.getWorld().getWorldBorder().getSize() * Math.random());
        int y = target.getWorld().getHighestBlockAt(x, z).getY();
        Location location = new Location(target.getWorld(), x, y, z);
        target.teleport(location);
        Messages.KUMA_VOYAGE_TARGET_TELEPORTED.send(target);
        player.sendMessage(ChatUtil.prefix("Vous venez de téléporter &c" + target.getName() + " aux coordonnées X&7: &a" + location.getBlockX() + " Y&7: &a" + location.getBlockY() + " Z&7: &a" + location.getBlockZ()));

        return true;
    }
}

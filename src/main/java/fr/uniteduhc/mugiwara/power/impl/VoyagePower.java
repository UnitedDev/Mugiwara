package fr.uniteduhc.mugiwara.power.impl;

import fr.uniteduhc.mugiwara.utils.config.Messages;
import fr.uniteduhc.mugiwara.power.DamagePlayerPower;
import fr.uniteduhc.mugiwara.utils.utils.Utils;
import fr.uniteduhc.utils.ChatUtil;
import fr.uniteduhc.utils.ItemBuilder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class VoyagePower extends DamagePlayerPower {

    private int maxUses = 2;

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.PRISMARINE_SHARD).setName(Utils.itemFormat("&3&lVoyage")).toItemStack();
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
        if(maxUses >= 2) {
            player.sendMessage(ChatUtil.prefix("&cVous avez atteint la limite d'utilisation pour ce pouvoir !"));
            return false;
        }

        maxUses++;
        Messages.KUMA_VOYAGE_PLAYER_USE.send(player);

        int x = (int) ((target.getWorld().getWorldBorder().getSize() / 2) * Math.random());
        int z = (int) ((target.getWorld().getWorldBorder().getSize() / 2) * Math.random());
        int y = target.getWorld().getHighestBlockAt(x, z).getY();
        Location location = new Location(target.getWorld(), x, y, z);
        target.teleport(location);
        Messages.KUMA_VOYAGE_TARGET_TELEPORTED.send(target);
        player.sendMessage(ChatUtil.prefix("Vous venez de téléporter &c" + target.getName() + " aux coordonnées X&7: &a" + location.getBlockX() + " Y&7: &a" + location.getBlockY() + " Z&7: &a" + location.getBlockZ()));
        return true;
    }
}

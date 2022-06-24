package fr.kohei.mugiwara.power.impl;

import fr.kohei.mugiwara.game.player.MUPlayer;
import fr.kohei.mugiwara.power.RightClickPlayerPower;
import fr.kohei.mugiwara.power.RightClickPower;
import fr.kohei.mugiwara.utils.utils.Utils;
import fr.kohei.utils.ChatUtil;
import fr.kohei.utils.ItemBuilder;
import fr.kohei.utils.ReflectionUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ShamblesRightPower extends RightClickPower {
    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.GHAST_TEAR).setName(Utils.itemFormat("&6&lShambles")).toItemStack();
    }

    @Override
    public String getName() {
        return "Shambles (Right)";
    }

    @Override
    public Integer getCooldownAmount() {
        return 5;
    }

    @Override
    public boolean onEnable(Player player, boolean rightClick) {
        Player target = Bukkit.getOnlinePlayers().stream()
                .filter(p -> p.getLocation().distance(player.getLocation()) <= 5)
                .filter(p -> ReflectionUtils.getLookingAt(player, p))
                .findFirst().orElse(null);
        if(target == null) return false;

        RoomPower roomPower = MUPlayer.get(player).getRole().getPowers().stream()
                .filter(p -> p instanceof RoomPower)
                .map(p -> (RoomPower) p)
                .findFirst().orElse(null);
        if(roomPower == null) return false;
        if(roomPower.getCenter() == null) {
            player.sendMessage(ChatUtil.prefix("&cVous n'êtes pas en train d'utiliser votre pouvoir de room."));
            return false;
        }

        Location playerLocation = player.getLocation();
        Location targetLocation = target.getLocation();

        player.teleport(targetLocation);
        target.teleport(playerLocation);

        return true;
    }
}

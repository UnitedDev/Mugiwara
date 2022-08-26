package fr.uniteduhc.mugiwara.power.impl;

import fr.uniteduhc.mugiwara.game.player.MUPlayer;
import fr.uniteduhc.mugiwara.power.RightClickPower;
import fr.uniteduhc.mugiwara.utils.utils.Utils;
import fr.uniteduhc.utils.ItemBuilder;
import fr.uniteduhc.utils.ReflectionUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ShamblesRightPower extends RightClickPower {
    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.BRICK).setName(Utils.itemFormat("&6&lShambles")).toItemStack();
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
        Player target = Utils.getPlayers().stream()
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
            return false;
        }

        Location playerLocation = player.getLocation();
        Location targetLocation = target.getLocation();

        player.teleport(targetLocation);
        target.teleport(playerLocation);

        return true;
    }

    @Override
    public boolean isGive() {
        return false;
    }
}

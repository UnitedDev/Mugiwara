package fr.uniteduhc.mugiwara.power.impl;

import fr.uniteduhc.mugiwara.utils.config.Messages;
import fr.uniteduhc.mugiwara.game.player.MUPlayer;
import fr.uniteduhc.mugiwara.game.menu.KumaBibleMenu;
import fr.uniteduhc.mugiwara.power.RightClickPower;
import fr.uniteduhc.mugiwara.roles.marine.BartholomewKumaRole;
import fr.uniteduhc.mugiwara.utils.utils.Utils;
import fr.uniteduhc.utils.ChatUtil;
import fr.uniteduhc.utils.ItemBuilder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BiblePower extends RightClickPower {

    private boolean isBreaking;

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.ENCHANTED_BOOK).setName(Utils.itemFormat("&d&lBible")).toItemStack();
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
    public boolean onEnable(Player player, boolean rightClick) {

        if(isBreaking) {
            player.sendMessage(ChatUtil.prefix("&cVous avez déjà utilisé votre pouvoir !"));
            return false;
        }

        Messages.KUMA_BIBLE_PLAYER_USE.send(player);

        for (Player nearPlayer : Utils.getNearPlayers(player, 10)) {
            int x = (int) (player.getWorld().getWorldBorder().getSize() * Math.random());
            int z = (int) (player.getWorld().getWorldBorder().getSize() * Math.random());
            int y = player.getWorld().getHighestBlockAt(x, z).getY();
            Location location = new Location(player.getWorld(), x, y, z);
            nearPlayer.teleport(location);
            Messages.KUMA_BIBLE_TELEPORT_TARGET_USE.send(nearPlayer);
            BartholomewKumaRole role = (BartholomewKumaRole) MUPlayer.get(player).getRole();
            role.getTeleportedPlayers().add(nearPlayer.getUniqueId());
        }

        isBreaking = true;

        new KumaBibleMenu().openMenu(player);

        return true;
    }
}

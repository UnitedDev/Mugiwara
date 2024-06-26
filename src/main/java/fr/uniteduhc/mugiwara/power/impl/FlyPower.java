package fr.uniteduhc.mugiwara.power.impl;

import fr.uniteduhc.mugiwara.Mugiwara;
import fr.uniteduhc.mugiwara.game.player.MUPlayer;
import fr.uniteduhc.mugiwara.power.RightClickPower;
import fr.uniteduhc.mugiwara.roles.alliance.KingRole;
import fr.uniteduhc.mugiwara.utils.config.Messages;
import fr.uniteduhc.mugiwara.utils.utils.Utils;
import fr.uniteduhc.utils.ChatUtil;
import fr.uniteduhc.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class FlyPower extends RightClickPower {
    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.FEATHER).addEnchant(Enchantment.DAMAGE_ALL, 1).hideItemFlags()
                .setName(Utils.itemFormat("&b&lFly")).toItemStack();
    }

    @Override
    public String getName() {
        return "Fly";
    }

    @Override
    public Integer getCooldownAmount() {
        return 25;
    }

    @Override
    public boolean onEnable(Player player, boolean rightClick) {
        KingRole kingRole = (KingRole) MUPlayer.get(player).getRole();
        PteranodonPower power = (PteranodonPower) kingRole.getPowers().stream()
                .filter(p -> p instanceof PteranodonPower).findFirst().orElse(null);
        if (power == null) return false;

        if(!power.isUsing()) {
            player.sendMessage(ChatUtil.prefix("&cVous n'êtes pas en train d'utiliser votre pouvoir Ptéranodon"));
            return false;
        }

        Messages.KING_FLY_USE.send(player);
        player.setAllowFlight(true);
        player.setFlying(true);

        Bukkit.getScheduler().runTaskLater(Mugiwara.getInstance(), () -> {
            player.setFlying(false);
            player.setAllowFlight(false);
        }, 5 * 20);
        return true;
    }
}

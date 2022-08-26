package fr.uniteduhc.mugiwara.power.impl;

import fr.uniteduhc.mugiwara.game.player.MUPlayer;
import fr.uniteduhc.utils.ChatUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.uniteduhc.mugiwara.roles.alliance.KaidoRole;
import fr.uniteduhc.mugiwara.power.RightClickPower;
import fr.uniteduhc.mugiwara.utils.config.Messages;
import fr.uniteduhc.mugiwara.utils.utils.Utils;
import fr.uniteduhc.utils.ItemBuilder;

public class BoroPower extends RightClickPower {
    @Override
    public boolean onEnable(Player player, boolean rightClick) {
        KaidoRole kaidoRole = (KaidoRole) MUPlayer.get(player).getRole();
        UoPower uoPower = (UoPower) kaidoRole.getPowers().stream().filter(power -> power instanceof UoPower).findFirst().orElse(null);
        if (uoPower == null) return false;

        if(!uoPower.isUsing()) {
            player.sendMessage(ChatUtil.prefix("&cVous n'êtes pas transformé."));
            return false;
        }

        final Location loc = player.getEyeLocation().toVector().add(player.getLocation().getDirection().multiply(2)).toLocation(player.getWorld(), player.getLocation().getYaw(), player.getLocation().getPitch());
        final Fireball fireball = player.getWorld().spawn(loc, Fireball.class);
        fireball.setYield(2);
        fireball.setShooter(player);

        Messages.KAIDO_SBIRE_BORO_USED.send(player);

        if(player.getItemInHand().getAmount() == 1) player.setItemInHand(new ItemStack(Material.AIR));
        else player.setItemInHand(new ItemBuilder(player.getItemInHand()).setAmount(player.getItemInHand().getAmount() - 1).toItemStack());
        return true;
    }

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.MAGMA_CREAM).setName(Utils.itemFormat("&b&lBoro")).toItemStack();
    }

    @Override
    public String getName() {
        return "Boro";
    }

    @Override
    public Integer getCooldownAmount() {
        return 8*60;
    }   
}
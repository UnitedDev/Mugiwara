package fr.kohei.mugiwara.power.impl;

import fr.kohei.mugiwara.game.player.MUPlayer;
import fr.kohei.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.kohei.mugiwara.roles.impl.alliance.KaidoRole;
import fr.kohei.mugiwara.Mugiwara;
import fr.kohei.mugiwara.power.RightClickPower;
import fr.kohei.mugiwara.utils.config.Messages;
import fr.kohei.mugiwara.utils.utils.Utils;
import fr.kohei.uhc.game.player.UPlayer;
import fr.kohei.utils.ItemBuilder;

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
        fireball.setYield(10);
        fireball.setShooter(player);

        Messages.KAIDO_SBIRE_BORO_USED.send(player);

        if(player.getItemInHand().getAmount() == 1) player.setItemInHand(new ItemStack(Material.AIR));
        else player.setItemInHand(new ItemBuilder(player.getItemInHand()).setAmount(player.getItemInHand().getAmount() - 1).toItemStack());
        return true;
    }

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.MAGMA_CREAM).setName(Utils.itemFormat("&b&oBoro")).toItemStack();
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
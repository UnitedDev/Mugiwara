package fr.uniteduhc.mugiwara.power.impl;

import fr.uniteduhc.utils.ChatUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.uniteduhc.mugiwara.roles.alliance.KaidoRole;
import fr.uniteduhc.mugiwara.Mugiwara;
import fr.uniteduhc.mugiwara.power.RightClickPower;
import fr.uniteduhc.mugiwara.utils.utils.Utils;
import fr.uniteduhc.uhc.game.player.UPlayer;
import fr.uniteduhc.utils.ItemBuilder;

@Getter
public class UoPower extends RightClickPower {
    private boolean using;
    public int use = 0;

    @Override
    public boolean onEnable(Player player, boolean rightClick) {
        if (use >= 2) {
            player.sendMessage(ChatUtil.prefix("&cVous avez déjà utilisé ce pouvoir 2 fois."));
            return false;
        }

        using = true;
        use++;
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 60 * 2, 1, false, false));
        final UPlayer uPlayer = UPlayer.get(player);
        final KaidoRole kaidoRole = ((KaidoRole) uPlayer.getRole());

        player.getInventory().addItem(new ItemBuilder(Material.MAGMA_CREAM).setName(Utils.itemFormat("&b&oBoro")).toItemStack());

        kaidoRole.nofall = true;
        Bukkit.getScheduler().runTaskLater(Mugiwara.getInstance(), () -> {
            player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
            kaidoRole.nofall = false;
        }, 20 * 60 * 2);

        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 10, 1, false, false));
        Bukkit.getScheduler().runTaskLater(Mugiwara.getInstance(), () -> {
            player.removePotionEffect(PotionEffectType.SPEED);
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, (60 + 50) * 20, 0, false, false));
            kaidoRole.nauseaToPlayer = false;
            player.setFlying(false);
        }, 20 * 10);
        kaidoRole.nauseaToPlayer = true;
        player.setFlying(true);

        for (final Entity entity : player.getWorld().getNearbyEntities(player.getLocation(), 25, 25, 25)) {
            if (entity.getType() != EntityType.PLAYER) {
                continue;
            }

            entity.getWorld().strikeLightning(player.getLocation());
        }

        Bukkit.getScheduler().runTaskLater(Mugiwara.getInstance(), () -> {
            using = false;
        }, 2 * 20 * 60);

        return true;
    }

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.NETHER_STAR).setName(Utils.itemFormat("&b&lUo")).toItemStack();
    }

    @Override
    public String getName() {
        return "Uo";
    }

    @Override
    public Integer getCooldownAmount() {
        return 8 * 60;
    }
}
package fr.uniteduhc.mugiwara.power.impl;

import fr.uniteduhc.mugiwara.Mugiwara;
import fr.uniteduhc.mugiwara.utils.config.Messages;
import fr.uniteduhc.mugiwara.power.RightClickPower;
import fr.uniteduhc.mugiwara.roles.mugiwara.FrankyRole;
import fr.uniteduhc.mugiwara.utils.utils.Utils;
import fr.uniteduhc.utils.ItemBuilder;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

@Getter
public class GeneralFrankyPower extends RightClickPower {
    private boolean using;
    private FrankyRole.GeneralPowers power;

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.IRON_INGOT).setName(Utils.itemFormat("&6&lGénéral Franky")).toItemStack();
    }

    @Override
    public String getName() {
        return "Général Franky";
    }

    @Override
    public Integer getCooldownAmount() {
        return Utils.getTimeBeforeEpisode();
    }

    @Override
    public boolean onEnable(Player player, boolean rightClick) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 2 * 60 * 20, 1, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 2 * 60 * 20, 0, false, false));
        Messages.FRANKY_GENERAL_USE.send(player);
        using = true;

        int random = (int) (Math.random() * 3);
        power = FrankyRole.GeneralPowers.values()[random];

        player.getInventory().setItem(
                player.getInventory().first(getItem()),
                power.getToReplace().toItemStack()
        );
        power.getMessages().send(player);

        final UUID uuid = player.getUniqueId();
        new BukkitRunnable() {
            @Override
            public void run() {
                Player player = Bukkit.getPlayer(uuid);
                if (player == null) return;

                if (player.getInventory().contains(power.getToReplace().toItemStack())) {
                    player.getInventory().setItem(
                            player.getInventory().first(power.getToReplace().toItemStack()),
                            getItem()
                    );
                }
                Messages.FRANKY_GENERAL_END.send(player);
                using = false;
            }
        }.runTaskLater(Mugiwara.getInstance(), 120 * 20);
        return true;
    }
}

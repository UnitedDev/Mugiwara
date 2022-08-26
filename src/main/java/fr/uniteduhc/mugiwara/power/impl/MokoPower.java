package fr.uniteduhc.mugiwara.power.impl;

import fr.uniteduhc.mugiwara.Mugiwara;
import fr.uniteduhc.mugiwara.power.RightClickPower;
import fr.uniteduhc.mugiwara.utils.config.Messages;
import fr.uniteduhc.mugiwara.utils.utils.Utils;
import fr.uniteduhc.utils.ChatUtil;
import fr.uniteduhc.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MokoPower extends RightClickPower {
    private int used;

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.FIREWORK).setName(Utils.itemFormat("&5&lMoko")).toItemStack();
    }

    @Override
    public String getName() {
        return "Moko";
    }

    @Override
    public Integer getCooldownAmount() {
        return 5 * 60;
    }

    @Override
    public boolean onEnable(Player player, boolean rightClick) {
        if(used >= 3) {
            player.sendMessage(ChatUtil.prefix("&cVous avez déjà utilisé 3 fois ce pouvoir."));
            return false;
        }

        Messages.FUJITORA_MOKO_USE.send(player);

        used++;

        new BukkitRunnable() {
            int seconds = 0;

            List<UUID> touched = new ArrayList<>();
            @Override
            public void run() {
                if(seconds > 30) {
                    cancel();
                    return;
                }

                Utils.getNearPlayers(player, 15).forEach(nearPlayer -> {
                    if(seconds <= 10) {
                        nearPlayer.removePotionEffect(PotionEffectType.SLOW);
                        nearPlayer.removePotionEffect(PotionEffectType.SLOW_DIGGING);
                        nearPlayer.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 10 * 20, 1, false, false));
                        nearPlayer.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 10 * 20, 1, false, false));
                    } else if(seconds <= 20) {
                        nearPlayer.removePotionEffect(PotionEffectType.SLOW);
                        nearPlayer.removePotionEffect(PotionEffectType.SLOW_DIGGING);
                        nearPlayer.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 10 * 20, 2, false, false));
                        nearPlayer.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 10 * 20, 2, false, false));
                    } else {
                        nearPlayer.removePotionEffect(PotionEffectType.SLOW);
                        nearPlayer.removePotionEffect(PotionEffectType.SLOW_DIGGING);
                        nearPlayer.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 10 * 20, 3, false, false));
                        nearPlayer.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 10 * 20, 3, false, false));
                    }
                    if(!touched.contains(nearPlayer.getUniqueId())) {
                        Messages.FUJITORA_MOKO_TARGET.send(nearPlayer);
                        touched.add(nearPlayer.getUniqueId());
                    }
                });


                seconds++;
            }
        }.runTaskTimer(Mugiwara.getInstance(), 0, 20);

        return true;
    }
}

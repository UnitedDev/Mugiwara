package fr.kohei.mugiwara.power.impl;

import fr.kohei.mugiwara.Mugiwara;
import fr.kohei.mugiwara.power.RightClickPower;
import fr.kohei.mugiwara.utils.config.Messages;
import fr.kohei.mugiwara.utils.utils.Utils;
import fr.kohei.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class MokoPower extends RightClickPower {
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
        Messages.FUJITORA_MOKO_USE.send(player);

        new BukkitRunnable() {
            int seconds = 0;

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
                    Messages.FUJITORA_MOKO_TARGET.send(nearPlayer);
                });


                seconds++;
            }
        }.runTaskTimer(Mugiwara.getInstance(), 0, 20);

        return true;
    }
}

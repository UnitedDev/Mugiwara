package fr.kohei.mugiwara.power.impl;

import fr.kohei.mugiwara.Mugiwara;
import fr.kohei.mugiwara.game.player.MUPlayer;
import fr.kohei.mugiwara.power.RightClickPower;
import fr.kohei.mugiwara.roles.marine.SengokuRole;
import fr.kohei.mugiwara.utils.utils.Utils;
import fr.kohei.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class HitoHitoNoMiPower extends RightClickPower {

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.GOLD_NUGGET)
                .setName(Utils.itemFormat("&6&lHito Hito no Mi"))
                .toItemStack();
    }

    @Override
    public String getName() {
        return "Hito Hito no Mi";
    }

    @Override
    public Integer getCooldownAmount() {
        return 480;
    }

    @Override
    public boolean onEnable(Player player, boolean rightClick) {

        MUPlayer muPlayer = MUPlayer.get(player);
        SengokuRole sengokuRole = (SengokuRole) muPlayer.getRole();

        player.setMaxHealth(player.getMaxHealth() + 10);
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 10 * 20, 2, false, false));

        new BukkitRunnable() {
            int timer = 20;
            int count = 6;
            @Override
            public void run() {

                timer--;

                if(timer != 0) return;

                if(count == 0){
                    player.setMaxHealth(player.getMaxHealth() - 14);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, getCooldown().getSeconds(), 0, false, false));
                    cancel();
                    return;
                }

                sengokuRole.setExplose(true);
                count--;
                timer = 20;

            }

        }.runTaskTimer(Mugiwara.getInstance(), 0L, 20L);

        return true;
    }
}

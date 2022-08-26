package fr.uniteduhc.mugiwara.power.impl;

import fr.uniteduhc.mugiwara.Mugiwara;
import fr.uniteduhc.mugiwara.utils.config.Messages;
import fr.uniteduhc.mugiwara.power.RightClickPower;
import fr.uniteduhc.mugiwara.utils.utils.packets.MathUtil;
import fr.uniteduhc.mugiwara.utils.utils.packets.PlayerUtils;
import fr.uniteduhc.mugiwara.utils.utils.Utils;
import fr.uniteduhc.utils.ChatUtil;
import fr.uniteduhc.utils.ItemBuilder;
import net.minecraft.server.v1_8_R3.EnumParticle;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class WhiteSparksPower extends RightClickPower {
    private int uses;

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.INK_SACK).setName(Utils.itemFormat("White Sparks")).setDurability(0).toItemStack();
    }

    @Override
    public String getName() {
        return "White Sparks";
    }

    @Override
    public Integer getCooldownAmount() {
        return (8 * 60) + 30;
    }

    @Override
    public boolean onEnable(Player player, boolean rightClick) {
        // if the uses is 2 or more then return and send a message to the player
        if (uses >= 2) {
            player.sendMessage(ChatUtil.prefix("&cVous avez déjà utilisé ce pouvoir 2 fois."));
            return false;
        }

        // send the smoker white sparks use message
        Messages.SMOKER_WHITESPARKS_USE.send(player);

        // add one to the uses
        uses++;
        PlayerUtils.giveInvisible(player, 30);


        // get all 15 blocks near players
        Utils.getNearPlayers(player, 15).forEach(player1 -> {
            // add to the player1 blindness and weakness for 30 seconds
            player1.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 30, 0, false, false));
            player1.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 30, 0, false, false));

            // send the smoker white sparks target message to the player1
            Messages.SMOKER_WHITESPARKS_TARGET.send(player1);
        });

        // run a task 30 seconds after
        player.getServer().getScheduler().runTaskLater(Mugiwara.getInstance(), () -> {
            // send the smoker white sparks end message
            Messages.SMOKER_WHITESPARKS_END.send(player);
        }, 30L * 20L);

        // new task every seconds for 30 seconds using bukkit runnable
        new BukkitRunnable() {
            private int timer = 30;

            @Override
            public void run() {
                // if the timer is 0 then cancel the task
                if (timer == 0) {
                    this.cancel();
                    return;
                }

                // spawn particles around the player, 1.8.8
                MathUtil.sendCircleParticle(EnumParticle.CLOUD, player.getLocation(), 2.0, 10);

                timer--;
            }
        }.runTaskTimer(Mugiwara.getInstance(), 0, 20);

        return true;
    }
}

package fr.kohei.mugiwara.power.impl;

import fr.kohei.mugiwara.Mugiwara;
import fr.kohei.mugiwara.config.Messages;
import fr.kohei.mugiwara.power.RightClickPower;
import fr.kohei.mugiwara.utils.MathUtil;
import fr.kohei.mugiwara.utils.Utils;
import fr.kohei.utils.ChatUtil;
import fr.kohei.utils.Cuboid;
import fr.kohei.utils.ItemBuilder;
import net.minecraft.server.v1_8_R3.EnumParticle;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class BlazePower extends RightClickPower {
    private boolean used;

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.BLAZE_ROD).setName(Utils.itemFormat("&4&lDai Fukai")).toItemStack();
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
        // if used, return false and send the message
        if (used) {
            player.sendMessage(ChatUtil.prefix("&cVous avez déjà utilisé ce pouvoir."));
            return false;
        }

        // send the dai fukai use message to the player
        Messages.KIZARU_DAI_USE.send(player);

        // send the dai fukai use message to the player 30s after
        player.getServer().getScheduler().runTaskLater(Mugiwara.getInstance(), () -> {
            // send the dai fukai end message to the player
            Messages.KIZARU_DAI_END.send(player);
        }, 30 * 20);

        // new task every seconds for 30 seconds
        new BukkitRunnable() {
            int seconds = 30;

            @Override
            public void run() {
                // if the seconds is 0, stop the task
                if (seconds == 0) {
                    this.cancel();
                    return;
                }

                // spawn fire particles around the player
                MathUtil.sendCircleParticle(EnumParticle.FLAME, player.getLocation(), 15, 10);

                // set fire to all nearby players with a radius of 15
                Utils.getNearPlayers(player, 15).forEach(nearPlayer -> nearPlayer.setFireTicks(20 * 20));

                // remove all the water in a 15 block radius around the player using Cuboid class
                Cuboid cuboid = new Cuboid(player.getLocation().clone().add(-15, -15, -15), player.getLocation().clone().add(15, 15, 15));
                cuboid.getBlockList().forEach(block1 -> {
                    if (block1.getType() == Material.WATER) {
                        block1.setType(Material.AIR);
                    }
                });

                seconds--;
            }
        }.runTaskTimer(Mugiwara.getInstance(), 0, 20);

        return true;
    }
}

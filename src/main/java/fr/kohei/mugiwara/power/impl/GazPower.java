package fr.kohei.mugiwara.power.impl;

import fr.kohei.mugiwara.Mugiwara;
import fr.kohei.mugiwara.config.Messages;
import fr.kohei.mugiwara.config.Replacement;
import fr.kohei.mugiwara.power.CommandPower;
import fr.kohei.mugiwara.utils.MathUtil;
import fr.kohei.utils.ChatUtil;
import net.minecraft.server.v1_8_R3.EnumParticle;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class GazPower extends CommandPower {
    private int uses = 0;

    @Override
    public String getArgument() {
        return "gaz";
    }

    @Override
    public boolean onEnable(Player player, String[] args) {
        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            player.sendMessage(ChatUtil.prefix("&cCe joueur n'est pas connecté."));
            return false;
        }

        // if the uses is 2 or more then return and send a message to the player
        if (uses >= 2) {
            player.sendMessage(ChatUtil.prefix("&cVous avez déjà utilisé ce pouvoir 2 fois."));
            return false;
        }

        // if the distance between the player and the target is more than 25 then return and send a message to the player
        if (player.getLocation().distance(target.getLocation()) > 25) {
            player.sendMessage(ChatUtil.prefix("&cVous devez être à moins de 25 blocs de distance de votre cible."));
            return false;
        }

        // send the smoker gaz use power message to the player
        Messages.SMOKER_GAZ_USE.send(player, new Replacement("<name>", target.getName()));

        // add blindess 10 and weakness 1 for 15 seconds to the target
        target.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.BLINDNESS, 15 * 20, 10));

        // send the smoker gaz target power message to the target
        Messages.SMOKER_GAZ_TARGET.send(target);

        // new task every seconds for 15 seconds
        new BukkitRunnable() {
            int timer = 15;

            @Override
            public void run() {
                // if the timer is 0 then cancel the task
                if (timer == 0) {
                    this.cancel();
                    return;
                }

                MathUtil.sendCircleParticle(EnumParticle.CLOUD, target.getLocation(), 2.0, 10);

                timer--;
            }
        }.runTaskTimer(Mugiwara.getInstance(), 0, 20);

        return true;
    }

    @Override
    public String getName() {
        return "Gaz";
    }

    @Override
    public Integer getCooldownAmount() {
        return 30;
    }
}

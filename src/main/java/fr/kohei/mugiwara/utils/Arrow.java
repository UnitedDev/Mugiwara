package fr.kohei.mugiwara.utils;

import fr.kohei.mugiwara.Mugiwara;
import fr.kohei.uhc.utils.LocationUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class Arrow {
    public static void setArrow(Player player, Player target, int seconds, boolean hideName) {
        final UUID playerUUID = player.getUniqueId();
        final UUID targetUUID = target.getUniqueId();

        new BukkitRunnable() {
            private int timer = seconds;

            @Override
            public void run() {
                timer--;
                Player player = Bukkit.getPlayer(playerUUID);
                Player target = Bukkit.getPlayer(targetUUID);
                if (timer <= 0) {
                    Mugiwara.getInstance().removeActionBar(player, "arrow");
                    cancel();
                    return;
                }


                if (player == null || target == null) return;

                double distance = player.getLocation().distance(target.getLocation());
                String arrow = LocationUtils.getArrow(player.getLocation(), target.getLocation());

                String targetName = hideName ? "&k" + target.getName() + "&r" : target.getName();
                String display = "&6" + targetName +" " + arrow + " " + distance;
                Mugiwara.getInstance().addActionBar(player, display, "arrow");
            }
        }.runTaskTimer(Mugiwara.getInstance(), 0, 20);
    }

    public static void setArrow(Player player, Player target, int seconds) {
        setArrow(player, target, seconds, false);
    }
}

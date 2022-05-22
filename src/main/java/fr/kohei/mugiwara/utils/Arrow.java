package fr.kohei.mugiwara.utils;

import fr.kohei.mugiwara.Mugiwara;
import fr.kohei.uhc.utils.LocationUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class Arrow {
    public static final HashMap<UUID, String> ARROWS = new HashMap<>();

    public static void setArrow(Player player, Player target, int seconds) {
        final UUID playerUUID = player.getUniqueId();
        final UUID targetUUID = target.getUniqueId();

        new BukkitRunnable() {
            private int timer = seconds;

            @Override
            public void run() {
                timer--;
                if (timer <= 0) {
                    cancel();
                    return;
                }

                Player player = Bukkit.getPlayer(playerUUID);
                Player target = Bukkit.getPlayer(targetUUID);

                if (player == null || target == null) return;

                double distance = player.getLocation().distance(target.getLocation());
                String arrow = LocationUtils.getArrow(player.getLocation(), target.getLocation());

                String display = "&6" + target.getName() + " " + arrow + " " + distance;
                ARROWS.put(playerUUID, display);
            }
        }.runTaskTimer(Mugiwara.getInstance(), 0, 20);
    }
}

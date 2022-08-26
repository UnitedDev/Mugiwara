package fr.uniteduhc.mugiwara.utils.utils.arrow;

import fr.uniteduhc.mugiwara.Mugiwara;
import lombok.Getter;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class BowAimbot implements Listener {
    private static final List<UUID> trackers = new ArrayList<>();

    public BowAimbot(Plugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public static void addTracker(Player player, int seconds) {
        trackers.add(player.getUniqueId());
        new BukkitRunnable() {
            @Override
            public void run() {
                trackers.remove(player.getUniqueId());
            }
        }.runTaskLater(Mugiwara.getInstance(), seconds * 20L);
    }

    @EventHandler
    public void onEntityShootBow(final EntityShootBowEvent event) {
        if (event.getEntity() instanceof Player && event.getProjectile() instanceof Arrow) {
            final Player player = (Player) event.getEntity();
            if(!trackers.contains(player.getUniqueId())) return;
            double minAng = 6.28;
            Entity minEntity = null;
            for (final Entity entity : player.getNearbyEntities(64.0, 64.0, 64.0)) {
                if (player.hasLineOfSight(entity) && entity instanceof LivingEntity) {
                    final Vector to = entity.getLocation().toVector().clone().subtract(player.getLocation().toVector());
                    final double angle = event.getProjectile().getVelocity().angle(to);
                    if (angle >= minAng) {
                        continue;
                    }
                    minAng = angle;
                    minEntity = entity;
                }
            }
            if (minEntity != null) {
                new Tracking((Arrow) event.getProjectile(), (LivingEntity) minEntity, Mugiwara.getInstance());
            }
        }
    }
}

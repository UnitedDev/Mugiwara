package fr.kohei.mugiwara.utils.particle;

import fr.kohei.mugiwara.Mugiwara;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

import static java.lang.Math.*;

@Getter
public class SpiralParticle extends BukkitRunnable {

    private final UUID uuid;
    private int duration;
    private double t;
    private double r;

    public SpiralParticle(UUID uuid, int seconds) {
        this.uuid = uuid;
        this.duration = seconds * 20;
        this.t = 0;
        this.r = 1;

        this.runTaskTimer(Mugiwara.getInstance(), 0, 1);
    }

    @Override
    public void run() {
        if (duration <= 0) {
            cancel();
            return;
        }

        Player player = Bukkit.getPlayer(uuid);
        if (player == null) return;

        t = t + PI / 50;

        double x = r * cos(t) - 0.25;
        double y = t;
        double z = r * sin(t) - 0.25;

        Location location = player.getLocation().clone().add(x, y, z);

        ParticleEffect.OrdinaryColor color = new ParticleEffect.OrdinaryColor((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255));
        for (int i = 0; i <= 5; i++) {
            ParticleEffect.REDSTONE.display(color, location, 100);
        }

        location.subtract(x, y, z);
        duration--;

        if (t > PI / 2) {
            this.t = 0;
            this.r = 1;
        }
    }
}

package fr.kohei.mugiwara.utils.utils.arrow;

import org.bukkit.scheduler.*;
import org.bukkit.plugin.*;
import org.bukkit.util.*;
import org.bukkit.entity.*;
import org.bukkit.*;

public class Tracking extends BukkitRunnable
{
    public Arrow arrow;
    public LivingEntity target;
    
    public Tracking(final Arrow arrow, final LivingEntity target, final Plugin plugin) {
        this.arrow = arrow;
        this.target = target;
        this.runTaskTimer(plugin, 1L, 1L);
    }
    
    public void run() {
        final double speed = this.arrow.getVelocity().length();
        if (this.arrow.isOnGround() || this.arrow.isDead() || this.target.isDead()) {
            this.cancel();
            return;
        }
        final Vector to = this.target.getLocation().clone().add(new Vector(0.0, 0.5, 0.0)).subtract(this.arrow.getLocation()).toVector();
        final Vector dirVel = this.arrow.getVelocity().clone().normalize();
        final Vector dirTarget = to.clone().normalize();
        final double ang = dirVel.angle(dirTarget);
        double speed_ = 0.9 * speed + 0.13999999999999999;
        if (this.target instanceof Player && this.arrow.getLocation().distance(this.target.getLocation()) < 8.0) {
            final Player player = (Player)this.target;
            if (player.isBlocking()) {
                speed_ = speed * 0.6;
            }
        }
        Vector newVel;
        if (ang < 0.12) {
            newVel = dirVel.clone().multiply(speed_);
        }
        else {
            final Vector newDir = dirVel.clone().multiply((ang - 0.12) / ang).add(dirTarget.clone().multiply(0.12 / ang));
            newDir.normalize();
            newVel = newDir.clone().multiply(speed_);
        }
        this.arrow.setVelocity(newVel.add(new Vector(0.0, 0.03, 0.0)));
        this.arrow.getWorld().playEffect(this.arrow.getLocation(), Effect.SMOKE, 0);
    }
}
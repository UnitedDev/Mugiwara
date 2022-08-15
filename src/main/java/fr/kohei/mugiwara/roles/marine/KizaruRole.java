package fr.kohei.mugiwara.roles.marine;

import fr.kohei.mugiwara.camp.impl.MarineCamp;
import fr.kohei.mugiwara.game.player.MUPlayer;
import fr.kohei.mugiwara.power.impl.YasakaniPower;
import fr.kohei.mugiwara.power.impl.YataPower;
import fr.kohei.mugiwara.roles.RolesType;
import fr.kohei.uhc.game.player.UPlayer;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;

public class KizaruRole extends RolesType.MURole implements Listener {
    private float speed = 0.2F;

    private int inWater = 0;

    public KizaruRole() {
        super(Arrays.asList(
                new YataPower(),
                new YasakaniPower()
        ), 0L);
    }

    @Override
    public RolesType getRole() {
        return RolesType.KIZARU;
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(Material.GLOWSTONE_DUST);
    }

    @Override
    public void onDistribute(Player player) {
        // give speed potion effect permanently to player
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false));
    }

    // cancel the damage if role and the damage cause is fall
    @EventHandler
    public void onFallDamage(EntityDamageEvent event) {
        // entity instance of player, if not return
        if (!(event.getEntity() instanceof Player)) return;

        // the player
        Player player = (Player) event.getEntity();

        // if the player is not role (isRole) return
        if (!isRole(player)) return;

        if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            event.setCancelled(true);
        }
    }

    @Override
    public void onSecond(Player player) {
        Block block = player.getLocation().getBlock();

        if (block.getType() == Material.STATIONARY_WATER || block.getType() == Material.WATER) this.inWater++;
        else this.inWater = 0;

        if (this.inWater >= 5) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 6 * 20, 2, false, false));
            //Messages.WATER.send(player);
            this.inWater = 0;
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        // if the event is not a player return
        if (!(event.getEntity() instanceof Player)) return;

        // variable player casted
        Player player = (Player) event.getEntity();

        // if is not role return
        if (!this.isRole(player)) return;

        // if the damager is not an arrow return
        if (!(event.getDamager() instanceof Arrow)) return;

        // variable arrow casted
        Arrow arrow = (Arrow) event.getDamager();

        boolean ussop = false;

        // if the shooter is not null and the shooter is a player
        if (arrow.getShooter() != null && arrow.getShooter() instanceof Player) {
            // variable shooter casted
            Player shooter = (Player) arrow.getShooter();

            // if the shooter is the player return
            if (shooter.equals(player)) return;

            // get the role of the shooter
            RolesType role = MUPlayer.get(shooter).getRole().getRole();

            // if the role is ussop enable ussop
            ussop = role == RolesType.USSOP;
        }

        if (ussop) return;

        // there is one out of two chance the damage is 0
        if (Math.random() < 0.5) {
            event.setDamage(0);
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        // if the camp of the entity is marine
        if (UPlayer.get(event.getEntity()).getCamp() instanceof MarineCamp) {
            speed = speed * 125 / 100;
            // if getplayer is null return
            if (getPlayer() == null) return;

            // set walk speed of getPlayer (from superclass) to speed
            getPlayer().setWalkSpeed(speed);
        }
    }
}

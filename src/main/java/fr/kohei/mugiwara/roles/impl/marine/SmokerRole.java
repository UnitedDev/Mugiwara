package fr.kohei.mugiwara.roles.impl.marine;

import fr.kohei.mugiwara.Mugiwara;
import fr.kohei.mugiwara.utils.config.Messages;
import fr.kohei.mugiwara.game.player.MUPlayer;
import fr.kohei.mugiwara.power.impl.GazPower;
import fr.kohei.mugiwara.power.impl.WhiteSparksPower;
import fr.kohei.mugiwara.roles.RolesType;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;

public class SmokerRole extends RolesType.MURole implements Listener {
    private int inWater = 0;

    public SmokerRole() {
        super(Arrays.asList(
                new WhiteSparksPower(),
                new GazPower()
        ));
    }

    @Override
    public RolesType getRole() {
        return RolesType.SMOKER;
    }

    @Override
    public ItemStack getItem() {
        // item stack ink sack yellow
        return new ItemStack(Material.INK_SACK, 1, (short) 0);
    }

    @Override
    public String[] getDescription() {
        return new String[0];
    }

    @Override
    public void onDay(Player player) {
        // remove potion effect speed
        player.removePotionEffect(PotionEffectType.SPEED);
    }

    @Override
    public void onNight(Player player) {
        // add potion effect speed
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false));
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        // if the entity is not a player return
        if (!(event.getEntity() instanceof Player)) return;

        // variable player casted
        Player player = (Player) event.getEntity();

        // if is not role return
        if (!this.isRole(player)) return;

        // if the cause is fall cancel the event
        if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            event.setCancelled(true);
        }
    }

    @Override
    public void onSecond(Player player) {
        Block block = player.getLocation().getBlock();

        if (block.getType() == Material.STATIONARY_WATER || block.getType() == Material.WATER) this.inWater += 1;
        else this.inWater = 0;

        if (this.inWater >= 5) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 6 * 20, 2, false, false));
            Messages.WATER.send(player);
            this.inWater = 0;
        }
    }

    // when a player removes an item of his armor check if he has an armor, if he has no armor kill him
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        // if the event is not a player return
        if (!(event.getWhoClicked() instanceof Player)) return;

        // variable player casted
        Player player = (Player) event.getWhoClicked();

        // if is not role return
        if (!this.isRole(player)) return;

        // if the event is not a armor return
        if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) return;

        // create a task 1 tick later to check if the player has an armor
        new BukkitRunnable() {
            @Override
            public void run() {
                checkArmor(player);
            }
        }.runTaskLater(Mugiwara.getInstance(), 1);
    }

    // when a player removes an item of his armor check if he has an armor, if he has no armor kill him
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        // if the event is not a player return

        // variable player casted
        Player player = event.getPlayer();

        // if is not role return
        if (!this.isRole(player)) return;

        // if the event is not a armor return
        if (event.getItem() == null || event.getItem().getType() == Material.AIR) return;

        // create a task 1 tick later to check if the player has an armor
        new BukkitRunnable() {
            @Override
            public void run() {
                checkArmor(player);
            }
        }.runTaskLater(Mugiwara.getInstance(), 1);
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

    private void checkArmor(Player player) {
        // if the player has no armor add him invisibility potion effect and speed 2 potion effect
        if (hasArmor(player)) {
            player.removePotionEffect(PotionEffectType.INVISIBILITY);
            player.removePotionEffect(PotionEffectType.SPEED);
        } else {
            player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0, false, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1, false, false));
        }
    }

    public boolean hasArmor(Player player) {
        return player.getInventory().getHelmet() == null && player.getInventory().getChestplate() == null && player.getInventory().getLeggings() == null && player.getInventory().getBoots() == null;
    }
}

package fr.kohei.mugiwara.roles.marine;

import fr.kohei.mugiwara.power.impl.BlazePower;
import fr.kohei.mugiwara.power.impl.FiolePower;
import fr.kohei.mugiwara.power.impl.FirePower;
import fr.kohei.mugiwara.roles.RolesType;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;

@Getter
@Setter
public class AkainuRole extends RolesType.MURole implements Listener {
    private int inWater = 0;
    private int seconds = 0;
    private boolean fire = false;

    public AkainuRole() {
        super(Arrays.asList(
                new FiolePower(),
                new BlazePower(),
                new FirePower()
        ));
    }

    @Override
    public RolesType getRole() {
        return RolesType.KIZARU;
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(Material.LAVA_BUCKET);
    }

    @Override
    public void onDistribute(Player player) {
        // give resistance and fire resistance to player potion effect
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
    }

    @Override
    public void onSecond(Player player) {
        // if the player has poison effect remove it
        if (player.hasPotionEffect(PotionEffectType.POISON)) {
            player.removePotionEffect(PotionEffectType.POISON);
        }

        // if the player has the slowness effect remove it
        if (player.hasPotionEffect(PotionEffectType.SLOW)) {
            player.removePotionEffect(PotionEffectType.SLOW);
        }

        seconds++;

        // if seconds is a multiple of 5 and the player fire ticks is greater than 0 add him half a heart
        if (seconds % 5 == 0 && player.getFireTicks() > 0) {
            player.setHealth(player.getHealth() + 1);
        }

        // in water
        Block block = player.getLocation().getBlock();

        if (block.getType() == Material.STATIONARY_WATER || block.getType() == Material.WATER) this.inWater++;
        else this.inWater = 0;

        if (this.inWater >= 5) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 6 * 20, 2, false, false));
            //Messages.WATER.send(player);
            this.inWater = 0;
        }
    }


    // if the player hits a player with a sword, the hit player gets on fire
    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            Player player = (Player) event.getDamager();
            Player hitPlayer = (Player) event.getEntity();

            if (!isRole(player)) return;

            // if fire power is disabled return
            if(!fire) return;

            // if the player has the sword (iron or diamond) in his hand
            if (player.getInventory().getItemInHand().getType() == Material.DIAMOND_SWORD
                    || player.getInventory().getItemInHand().getType() == Material.IRON_SWORD) {
                hitPlayer.setFireTicks(20 * 5);
            }
        }
    }

    // if the player shoots someone, the hit player gets on fire
    @EventHandler
    public void onShoot(EntityDamageByEntityEvent event) {
        // verify if the damager is an arrow, if not return
        if (!(event.getDamager() instanceof Arrow)) return;

        // verify if the entity is a player, if not return
        if (!(event.getEntity() instanceof Player)) return;

        // the arrow
        Arrow arrow = (Arrow) event.getDamager();

        // verify that the shooter is a player
        if (!(arrow.getShooter() instanceof Player)) return;

        // the player shooter
        Player shooter = (Player) arrow.getShooter();

        // if the player is not the role return
        if (!isRole(shooter)) return;

        // if fire power is disabled return
        if(!fire) return;

        // make the entity on fire
        event.getEntity().setFireTicks(20 * 5);

    }
}

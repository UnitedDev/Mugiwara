package fr.kohei.mugiwara.roles.impl.alliance;

import fr.kohei.mugiwara.Mugiwara;
import fr.kohei.mugiwara.power.impl.FlyPower;
import fr.kohei.mugiwara.power.impl.PteranodonPower;
import fr.kohei.mugiwara.roles.RolesType;
import fr.kohei.mugiwara.utils.config.Messages;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;

public class KingRole extends RolesType.MURole implements Listener {
    private int inWater = 0;

    public KingRole() {
        super(Arrays.asList(
                new PteranodonPower(),
                new FlyPower()
        ));
    }

    @Override
    public double getStrengthBuffer() {
        return 1.05F;
    }

    @Override
    public double getResistanceBuffer() {
        return 0.95F;
    }

    @Override
    public RolesType getRole() {
        return RolesType.KING;
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(Material.FEATHER);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();

        if (!isRole(player)) return;

        if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (!(event.getDamager() instanceof Player)) return;

        Player player = (Player) event.getEntity();
        Player damager = (Player) event.getDamager();

        if (!isRole(damager)) return;

        PteranodonPower power = (PteranodonPower) getPowers().stream()
                .filter(p -> p instanceof PteranodonPower)
                .findFirst()
                .orElse(null);
        if (power == null) return;

        if (power.isUsing()) {
            player.setFireTicks(50);
        }
    }

    @Override
    public void onSecond(Player player) {
        final Block block = player.getLocation().getBlock();

        if (block.getType() == Material.STATIONARY_WATER || block.getType() == Material.WATER) this.inWater += 1;
        else this.inWater = 0;

        if (this.inWater >= 5) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 6 * 20, 2, false, false));
            Messages.WATER.send(player);
            this.inWater = 0;
        }
    }

    @Override
    public void onDistribute(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));

        player.setWalkSpeed(0.21F);

        Mugiwara.knowsRole(player, RolesType.KAIDO);
        Mugiwara.knowsRole(player, RolesType.JACK);
        Mugiwara.knowsRole(player, RolesType.QUEEN);
    }

    @Override
    public String[] getDescription() {
        return new String[0];
    }
}

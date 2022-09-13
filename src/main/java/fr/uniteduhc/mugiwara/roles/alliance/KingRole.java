package fr.uniteduhc.mugiwara.roles.alliance;

import fr.uniteduhc.mugiwara.Mugiwara;
import fr.uniteduhc.mugiwara.power.impl.FlyPower;
import fr.uniteduhc.mugiwara.power.impl.PteranodonPower;
import fr.uniteduhc.mugiwara.roles.RolesType;
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

    public KingRole() {
        super(Arrays.asList(
                new PteranodonPower(),
                new FlyPower()
        ), 1390000000L);
    }

    @Override
    public boolean hasFruit() {
        return true;
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
        super.onSecond(player);
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
}

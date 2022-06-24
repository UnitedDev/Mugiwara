package fr.kohei.mugiwara.roles.impl.marine;

import fr.kohei.mugiwara.utils.config.Messages;
import fr.kohei.mugiwara.utils.config.Replacement;
import fr.kohei.mugiwara.game.player.MUPlayer;
import fr.kohei.mugiwara.power.impl.TnTPower;
import fr.kohei.mugiwara.roles.RolesType;
import fr.kohei.mugiwara.utils.utils.packets.Damage;
import org.bukkit.Material;
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

public class GarpRole extends RolesType.MURole implements Listener {
    public GarpRole() {
        super(Arrays.asList(
                new TnTPower()
        ));
    }

    @Override
    public RolesType getRole() {
        return RolesType.GARP;
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(Material.TNT);
    }

    @Override
    public String[] getDescription() {
        return new String[0];
    }

    @Override
    public void onDistribute(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));

        Damage.NO_DAMAGE_CAUSE.put(player.getUniqueId(), EntityDamageEvent.DamageCause.ENTITY_EXPLOSION);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        if (getPlayer() == null) return;

        if (MUPlayer.get(player).getRole().getRole() == RolesType.SENGOKU) {
            if (player.getKiller() == null) {
                Messages.GARP_DEATH_PVE.send(getPlayer());
                return;
            }

            Messages.GARP_DEATH_PLAYER.send(getPlayer(), new Replacement("<name>", player.getKiller().getName()));
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (!(event.getDamager() instanceof Player)) return;

        Player player = (Player) event.getEntity();
        Player damager = (Player) event.getDamager();

        if (!isRole(damager)) return;

        if (random(damager)) {
            player.getWorld().createExplosion(player.getLocation(), 3f);
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 3 * 20, 4, false, false));
        }
    }

    private boolean random(Player player) {
        int random = (int) (Math.random() * 20);
        double health = player.getHealth();

        if (health >= 16 && random == 1) return true;
        else if (health >= 10 && random <= 3) return true;
        else if (health >= 6 && random <= 6) return true;
        else if (health >= 2 && random <= 10) return true;
        else return health < 2;
    }

    @Override
    public double getResistanceBuffer() {
        Player player = getPlayer();
        double health = player.getHealth();

        if (health >= 16) return 1.0;
        else if (health >= 10) return 1.1;
        else if (health >= 6) return 1.2;
        else if (health >= 2) return 1.3;
        else return 1.3;
    }
}

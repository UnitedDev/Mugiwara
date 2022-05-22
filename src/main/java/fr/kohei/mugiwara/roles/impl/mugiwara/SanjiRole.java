package fr.kohei.mugiwara.roles.impl.mugiwara;

import fr.kohei.mugiwara.Mugiwara;
import fr.kohei.mugiwara.config.Messages;
import fr.kohei.mugiwara.game.MUPlayer;
import fr.kohei.mugiwara.power.impl.DiableJambePower;
import fr.kohei.mugiwara.roles.RolesType;
import fr.kohei.uhc.UHC;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;
import java.util.stream.Collectors;

public class SanjiRole extends RolesType.MURole implements Listener {
    private final HashMap<UUID, Integer> next = new HashMap<>();
    private boolean foundZoro = false;

    public SanjiRole() {
        super(Arrays.asList(
                new DiableJambePower()
        ));
    }

    @Override
    public RolesType getRole() {
        return RolesType.SANJI;
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(Material.DIAMOND_BOOTS);
    }

    @Override
    public String[] getDescription() {
        return new String[0];
    }

    @Override
    public void onDistribute(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false));
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;

        if (!isRole((Player) event.getEntity())) return;

        if (event.getCause() == EntityDamageEvent.DamageCause.FALL) event.setCancelled(true);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if(!(event.getDamager() instanceof Player)) return;
        if(!(event.getEntity() instanceof Player)) return;

        Player damager = (Player) event.getDamager();
        if(!isRole(damager)) return;


    }

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        if (!isRole(player)) return;

        if (event.getItem().getType() == Material.GOLDEN_APPLE) {
            Bukkit.getScheduler().runTaskLater(Mugiwara.getInstance(), () -> {
                player.removePotionEffect(PotionEffectType.REGENERATION);
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 7 * 20, 1, false, true));
            }, 1);

            int random = (int) (Math.random() * 3);
            if (random == 1) ZoroRole.randomRole(player);
            Mugiwara.knowsRole(player, RolesType.LUFFY);
        }
    }

    @Override
    public void onKill(Player death, Player killer) {
        RolesType.MURole role = (RolesType.MURole) MUPlayer.get(death).getRole();

        if (isFemale(role)) {
            Messages.SANJI_KILLEDFEMALE.send(killer);
            killer.setMaxHealth(killer.getMaxHealth() - 6);
        }
    }

    private boolean isFemale(RolesType.MURole role) {
        return role.getRole() == RolesType.NAMI || role.getRole() == RolesType.ROBIN || role.getRole() == RolesType.KATAKURI
                || role.getRole() == RolesType.HANCOCK;
    }

    @Override
    public void onSecond(Player player) {
        for (Player player1 : player.getNearbyEntities(15, 15, 15).stream()
                .filter(entity -> entity instanceof Player)
                .map(entity -> (Player) entity)
                .filter(player1 -> UHC.getGameManager().getPlayers().contains(player1.getUniqueId()))
                .filter(player1 -> isFemale((RolesType.MURole) MUPlayer.get(player1).getRole()))
                .collect(Collectors.toList())) {
            if (player1.getLocation().distance(player.getLocation()) <= 15) {
                next.put(player1.getUniqueId(), next.getOrDefault(player1.getUniqueId(), 0) + 1);

                if (next.get(player1.getUniqueId()) == 60) {
                    player.setHealth(player.getHealth() - 2);
                    Messages.SANJI_STAYED1MINUTE.send(player);
                }
            } else if (player1.getLocation().distance(player.getLocation()) <= 25) {
                if (!foundZoro && ((RolesType.MURole) MUPlayer.get(player1).getRole()).getRole() == RolesType.ZORO) {
                    Mugiwara.knowsRole(player, RolesType.ZORO);
                    foundZoro = true;
                }
            }
        }
    }
}

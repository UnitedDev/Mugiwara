package fr.kohei.mugiwara.game;

import fr.kohei.mugiwara.Mugiwara;
import fr.kohei.mugiwara.power.*;
import fr.kohei.mugiwara.roles.RolesType;
import fr.kohei.mugiwara.utils.Damage;
import fr.kohei.mugiwara.utils.Spectator;
import fr.kohei.uhc.game.player.UPlayer;
import fr.kohei.uhc.module.manager.Role;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

@RequiredArgsConstructor
public class MUListener implements Listener {

    private final Mugiwara mu;

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDeath(PlayerDeathEvent event) {

        Player player = event.getEntity();
        UPlayer uPlayer = UPlayer.get(player);
        RolesType.MURole role = (RolesType.MURole) uPlayer.getRole();

        if (role == null || !uPlayer.isAlive()) return;

        for (Power rolePower : role.getPowers()) {
            if (!(rolePower instanceof ClickPower)) continue;

            ClickPower clickPower = (ClickPower) rolePower;

            if (clickPower.isDropAtDeath()) continue;

            player.getInventory().remove(clickPower.getItem());
        }

    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInteractAtEntity(PlayerInteractAtEntityEvent event) {
        if (!(event.getRightClicked() instanceof Player)) return;

        Player target = (Player) event.getRightClicked();

        ItemStack item = event.getPlayer().getItemInHand();

        if (item == null) return;

        Player player = event.getPlayer();
        UPlayer uPlayer = UPlayer.get(player);
        RolesType.MURole role = (RolesType.MURole) uPlayer.getRole();

        UPlayer uTarget = UPlayer.get(target);
        RolesType.MURole targetRole = (RolesType.MURole) uTarget.getRole();

        if (role == null || !uPlayer.isAlive()) return;
        if (targetRole == null || !uTarget.isAlive()) return;

        RightClickPlayerPower rightClickPlayerPower = RightClickPlayerPower.findPower(role, item);

        if (rightClickPlayerPower == null) return;

        if (rightClickPlayerPower.getCooldown() != null) {
            if (rightClickPlayerPower.getCooldown().isCooldown(player)) return;
            rightClickPlayerPower.getCooldown().setCooldown(rightClickPlayerPower.getCooldownAmount());
        }

        rightClickPlayerPower.onEnable(player, target);
        Power.onUse(player);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInteract(PlayerInteractEvent event) {
        ItemStack item = event.getPlayer().getItemInHand();

        if (item == null) return;

        Player player = event.getPlayer();
        UPlayer uPlayer = UPlayer.get(player);
        RolesType.MURole role = (RolesType.MURole) uPlayer.getRole();

        if (role == null || !uPlayer.isAlive()) return;

        for (RightClickPower rightClickPower : RightClickPower.findPowers(role, item)) {
            if (rightClickPower == null) continue;
            event.setCancelled(true);

            if (rightClickPower.rightClick() && event.getAction().name().contains("LEFT")) continue;
            if (!rightClickPower.rightClick() && !event.getAction().name().contains("LEFT")) continue;

            if (rightClickPower.getCooldown() != null && rightClickPower.getCooldown().isCooldown(player)) continue;
            boolean right = event.getAction().name().contains("RIGHT");
            boolean success = rightClickPower.onEnable(player, right);
            if (success && rightClickPower.getCooldown() != null) {
                rightClickPower.getCooldown().setCooldown(rightClickPower.getCooldownAmount());
            }
            if (success)
                Power.onUse(player);
        }

        if (role.getRole() == RolesType.KATAKURI && item.hasItemMeta() && item.getType() == Material.SNOW_BALL) {
            if (item.getItemMeta().getDisplayName().contains("Mochi")) {
                //give damage resistance potion effect to player
                player.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.DAMAGE_RESISTANCE, 20 * 30, 0));
                player.addPotionEffect(new org.bukkit.potion.PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 30, 0));

            }

        }

    }

    @EventHandler
    public void onArmorStand(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof ArmorStand)) return;

        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (!(event.getDamager() instanceof Player)) return;

        Player player = (Player) event.getEntity();
        Player damager = (Player) event.getDamager();

        UPlayer uPlayer = UPlayer.get(player);
        RolesType.MURole role = (RolesType.MURole) uPlayer.getRole();

        UPlayer uDamager = UPlayer.get(damager);
        RolesType.MURole damagerRole = (RolesType.MURole) uDamager.getRole();

        if (Damage.NO_DAMAGE.contains(damager.getUniqueId())) event.setCancelled(true);

        if (role == null || !uPlayer.isAlive()) return;
        if (damagerRole == null || !uDamager.isAlive()) return;

        double buffer = damagerRole.getStrengthBuffer();
        double newDamage = event.getDamage() * buffer;

        event.setDamage(newDamage);

        DamagePlayerPower damagePower = DamagePlayerPower.findPower(damagerRole, damager.getItemInHand());

        if (damagePower == null) return;

        if (damagePower.getCooldown() != null)
            if (damagePower.getCooldown().isCooldown(damager)) return;

        boolean success = damagePower.onEnable(damager, player);
        if (success && damagePower.getCooldown() != null)
            damagePower.getCooldown().setCooldown(damagePower.getCooldownAmount());

        if (role.getRole() == RolesType.KATAKURI && event.getCause() == EntityDamageEvent.DamageCause.PROJECTILE) {
            event.setCancelled(damagerRole.getRole() != RolesType.USSOP);
        }

        if (event.getCause() == EntityDamageEvent.DamageCause.PROJECTILE && damagerRole.getRole() == RolesType.KATAKURI && event.getDamage() < 0.5D) {
            player.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.SLOW, 20 * 6, 4));
        }

        if (success) Power.onUse(player);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();

        Damage.NO_DAMAGE_CAUSE.keySet().stream()
                .filter(uuid -> event.getEntity().getUniqueId().equals(uuid))
                .filter(uuid -> event.getCause() == Damage.NO_DAMAGE_CAUSE.get(uuid))
                .forEach(uuid -> event.setCancelled(true));

        if (Damage.NO_DAMAGE.contains(player.getUniqueId())) event.setCancelled(true);

        UPlayer uPlayer = UPlayer.get(player);
        Role role = uPlayer.getRole();

        if (role == null || !uPlayer.isAlive()) return;

        if (role instanceof RolesType.MURole) {
            double buffer = ((RolesType.MURole) role).getResistanceBuffer();
            double newDamage = event.getDamage() * buffer;

            event.setDamage(newDamage);
        }
    }


    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        if (event.getCause().equals(PlayerTeleportEvent.TeleportCause.SPECTATE)) {

            if (Spectator.CANNOT_SPECTATE.contains(event.getPlayer().getUniqueId())) {
                event.setCancelled(true);
            }
        }
    }


    @EventHandler(ignoreCancelled = true)
    public void onPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItemInHand();

        BlockPlacePower blockPower = BlockPlacePower.findPower(MUPlayer.get(player).getRole(), item);

        if (blockPower == null) return;

        if (blockPower.getCooldown() != null && blockPower.getCooldown().isCooldown(player)) {
            event.setCancelled(true);
            return;
        }
        boolean success = blockPower.onEnable(player, event.getBlock().getLocation());
        if (success) {
            if (blockPower.getCooldown() != null)
                blockPower.getCooldown().setCooldown(blockPower.getCooldownAmount());
            Power.onUse(player);
            event.getBlock().setType(Material.AIR);
        } else {
            event.setCancelled(true);
        }
    }
}

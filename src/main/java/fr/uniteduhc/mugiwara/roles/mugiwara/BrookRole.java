package fr.uniteduhc.mugiwara.roles.mugiwara;

import fr.uniteduhc.mugiwara.Mugiwara;
import fr.uniteduhc.mugiwara.utils.config.Messages;
import fr.uniteduhc.mugiwara.utils.config.Replacement;
import fr.uniteduhc.mugiwara.power.impl.AmePower;
import fr.uniteduhc.mugiwara.power.impl.SlowPower;
import fr.uniteduhc.mugiwara.roles.RolesType;
import fr.uniteduhc.mugiwara.utils.utils.Utils;
import fr.uniteduhc.mugiwara.utils.utils.packets.Damage;
import fr.uniteduhc.mumble.api.LinkAPI;
import fr.uniteduhc.mumble.api.mumble.IUser;
import fr.uniteduhc.uhc.UHC;
import fr.uniteduhc.utils.TimeUtil;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.UUID;

@Setter
public class BrookRole extends RolesType.MURole implements Listener {
    private UUID slowTarget = null;

    public BrookRole() {
        super(Arrays.asList(
                new AmePower(),
                new SlowPower()
        ), 83000000L);
    }

    @Override
    public RolesType getRole() {
        return RolesType.BROOK;
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(Material.BONE);
    }

    @Override
    public void onDistribute(Player player) {
        Mugiwara.knowsRole(player, RolesType.LUFFY);
        int random = (int) (Math.random() * 3);
        if (random == 1) ZoroRole.randomRole(player);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        if (!isRole(player)) return;

        if (event.getCause() == EntityDamageEvent.DamageCause.FALL) event.setCancelled(true);

        if (event.getDamage() >= player.getHealth()) {
            event.setCancelled(true);
            this.handleDeath(player);
        }
    }

    private void handleDeath(Player player) {
        player.setGameMode(GameMode.SPECTATOR);
        Messages.BROOK_DEATH.send(player);

        IUser user = LinkAPI.getApi().getMumbleManager().getUserFromName(player.getName());
        if(user != null) user.muteUser();

        final Location death = player.getLocation();
        final UUID uuid = player.getUniqueId();
        new BukkitRunnable() {
            private int timer = 5 * 60;

            @Override
            public void run() {
                timer--;
                Player player = Bukkit.getPlayer(uuid);

                if (player == null) return;

                if (timer == 0) {
                    handleRespawn(player);
                    cancel();
                    return;
                }

                Mugiwara.getInstance().addActionBar(player, "&cRespawn &8» &f" + TimeUtil.getReallyNiceTime2(timer * 1000L), "respawn");
                if (player.getLocation().distance(death) > 50) {
                    player.teleport(death);
                }
                Utils.getPlayers().forEach(player1 -> player1.hidePlayer(player));
            }
        }.runTaskTimer(Mugiwara.getInstance(), 0, 20L);
    }

    private void handleRespawn(Player player) {
        Utils.getPlayers().forEach(player1 -> player1.showPlayer(player));
        Mugiwara.getInstance().removeActionBar(player, "respawn");

        IUser user = LinkAPI.getApi().getMumbleManager().getUserFromName(player.getName());
        if(user != null) user.unmuteUser();

        int x = (int) (Math.random() * UHC.getInstance().getGameManager().getUhcWorld().getWorldBorder().getSize() / 2);
        int z = (int) (Math.random() * UHC.getInstance().getGameManager().getUhcWorld().getWorldBorder().getSize() / 2);
        int y = UHC.getInstance().getGameManager().getUhcWorld().getHighestBlockYAt(x, z) + 1;

        Bukkit.getOnlinePlayers().forEach(player1 -> player1.showPlayer(player));
        player.setGameMode(GameMode.SURVIVAL);
        player.teleport(new Location(UHC.getInstance().getGameManager().getUhcWorld(), x, y, z));
        Messages.BROOK_RESPAWN.send(player);
    }

    @EventHandler
    public void onInteract(PlayerInteractAtEntityEvent event) {
        if (!isRole(event.getPlayer())) return;

        AmePower amePower = (AmePower) getPowers().stream().filter(power -> power instanceof AmePower).findFirst().orElse(null);
        if (amePower == null) return;
        if (amePower.getEntity() == null || amePower.getEntity().isDead()) return;

        if (event.getRightClicked() instanceof Player) {
            Player right = (Player) event.getRightClicked();
            right.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 100, false, false));
            right.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 100, false, false));
            right.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 100, false, false));
            Messages.BROOK_AME_CANNOTMOVE.send(right);

            final float oldSpeedWalk = right.getWalkSpeed();
            right.setWalkSpeed(0.0F);
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (getPlayer() == null) return;
                    Player player = getPlayer();

                    if (player.getSpectatorTarget() == null) {
                        right.removePotionEffect(PotionEffectType.JUMP);
                        right.removePotionEffect(PotionEffectType.SLOW);
                        right.removePotionEffect(PotionEffectType.BLINDNESS);
                        right.setWalkSpeed(oldSpeedWalk);
                        cancel();
                    }
                }
            }.runTaskTimer(Mugiwara.getInstance(), 0, 20L);
        }

        if (amePower.getEntity().getUniqueId().equals(event.getRightClicked().getUniqueId())) {
            amePower.stop(event.getPlayer());
        }
    }

    @EventHandler
    public void onDeath(EntityDeathEvent event) {
        if (getPlayer() == null) return;
        AmePower amePower = (AmePower) getPowers().stream().filter(power -> power instanceof AmePower).findFirst().orElse(null);
        if (amePower == null) return;
        if (amePower.getEntity() == null || amePower.getEntity().isDead()) return;

        if (amePower.getEntity().getUniqueId().equals(event.getEntity().getUniqueId())) {
            Messages.BROOK_AME_DEATH.send(getPlayer());
            getPlayer().teleport(event.getEntity());
            getPlayer().damage(100000000000L);
        }
    }

    @EventHandler
    public void onAmeDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();

        if (getPlayer() == null) return;
        if (getPlayer().getSpectatorTarget() != null && getPlayer().getSpectatorTarget().getUniqueId().equals(player.getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;
        if (!(event.getEntity() instanceof Player)) return;
        Player damager = (Player) event.getDamager();
        Player player = (Player) event.getEntity();

        if (getPlayer() == null) return;

        if (getPlayer().getSpectatorTarget() != null && getPlayer().getSpectatorTarget().getUniqueId().equals(damager.getUniqueId())) {
            event.setCancelled(true);
        }

        if (!isRole(damager)) return;

        if (!slowTarget.equals(player.getUniqueId())) return;

        this.slowTarget = null;
        Messages.BROOK_SLOW_ATTACK.send(damager, new Replacement("<name>", player.getName()));
        Messages.BROOK_SLOW_ATTACKONME.send(player);
        Damage.addNoDamage(player, 45);
        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 45 * 20, 4, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 45 * 20, 4, false, false));

        final Location location = damager.getLocation();
        final UUID uuid = player.getUniqueId();
        new BukkitRunnable() {
            int timer = 45;

            public void run() {
                Player player = Bukkit.getPlayer(uuid);
                if (timer == 0) {
                    Messages.BROOK_SLOW_ATTACKONME_END.send(player);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 2 * 60 * 20, 1, false, false));
                    player.setHealth(player.getHealth() / 2L);
                    cancel();
                    return;
                }
                timer--;

                if (timer % 4 == 0) {
                    player.teleport(location);
                }

                player.setVelocity(new Vector(0, 1, 0));
            }
        }.runTaskTimer(Mugiwara.getInstance(), 20L, 20L);
    }
}

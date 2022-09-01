package fr.uniteduhc.mugiwara.game.events.haki.impl;

import fr.uniteduhc.mugiwara.Mugiwara;
import fr.uniteduhc.mugiwara.game.events.haki.AbstractHaki;
import fr.uniteduhc.mugiwara.game.events.haki.HakiType;
import fr.uniteduhc.mugiwara.utils.config.Messages;
import fr.uniteduhc.mugiwara.utils.config.Replacement;
import fr.uniteduhc.mugiwara.utils.utils.Cooldown;
import fr.uniteduhc.mugiwara.utils.utils.Utils;
import fr.uniteduhc.mugiwara.utils.utils.packets.MathUtil;
import fr.uniteduhc.utils.ChatUtil;
import fr.uniteduhc.utils.TimeUtil;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_8_R3.EnumParticle;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class RoyalHaki extends AbstractHaki implements Listener {
    private final Cooldown cooldown = new Cooldown("Haki des Rois");
    private boolean used = false;
    private final List<UUID> stuned = new ArrayList<>();
    private boolean using;
    private boolean another;
    private Location center;
    private boolean contreRoyal;
    private int timer = 15 * 2;

    public RoyalHaki() {
    }

    @Override
    public void onRightClick(Player player) {
        if (used) {
            player.sendMessage(ChatUtil.prefix("&cVous avez déjà utilisé ce pouvoir."));
            return;
        }
        if (cooldown.isCooldown(player)) return;


        used = true;
        using = true;
        cooldown.setCooldown(25 * 60);
        cooldown.task();
        center = player.getLocation();
        for (Player nearPlayer : Utils.getNearPlayers(player, 30)) {
            AbstractHaki haki = Mugiwara.getInstance().getHakiManager().getHaki(nearPlayer);
            if (haki instanceof RoyalHaki && ((RoyalHaki) haki).isUsing()) continue;

            stuned.add(nearPlayer.getUniqueId());
            nearPlayer.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 100, false, false));
            nearPlayer.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 200, false, false));
            nearPlayer.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 100, false, false));
        }

        Messages.HAKI_POWER_USE.send(player, new Replacement("<haki>", this.getHakiType().getDisplayName()));
        for (Player player1 : Utils.getPlayers()) {
            AbstractHaki haki = Mugiwara.getInstance().getHakiManager().getHaki(player1);
            if (haki == null) continue;
            if (!(haki instanceof RoyalHaki)) continue;
            if (player1.getUniqueId().equals(player.getUniqueId())) continue;

            RoyalHaki royalHaki = (RoyalHaki) haki;
            if (!royalHaki.isUsing()) continue;

            if (royalHaki.getCenter() != null && royalHaki.getCenter().distance(player.getLocation()) <= 25) {
                setCenter(royalHaki.getCenter());
            }

            if (royalHaki.getStuned().contains(player.getUniqueId())) {
                royalHaki.getStuned().remove(player.getUniqueId());
                stopStun(player);
            }
            if (royalHaki.getTimer() < 25 * 2) {
                royalHaki.setTimer(royalHaki.getTimer() + 45 * 2);
                timer = royalHaki.getTimer();
            }
            Messages.HAKI_FLUIDE_CONTRE.send(player1);
            royalHaki.setAnother(true);
        }


        if (timer < 15 * 2)
            timer = 15 * 2;
        new BukkitRunnable() {
            @Override
            public void run() {
                if (timer <= 0) {
                    cancel();
                    disablePower(player);
                    return;
                }

                Mugiwara.getInstance().addActionBar(player, "&cFluide &8» &f" + TimeUtil.getReallyNiceTime2(timer * 500L), "fluide_timer");
                if (contreRoyal)
                    for (int i = 0; i <= 3; i++)
                        MathUtil.sendCircleParticle(EnumParticle.FLAME, getCenter().clone().add(0, i, 0), 25, 50);
                timer--;
            }
        }.runTaskTimer(Mugiwara.getInstance(), 0, 10);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (!(event.getDamager() instanceof Player)) return;

        Player damager = (Player) event.getDamager();
        Player player = (Player) event.getEntity();

        if (stuned.contains(damager.getUniqueId())) event.setCancelled(true);
        if (!hasHaki(player)) return;
        if (!stuned.contains(player.getUniqueId())) return;
        if (!using) return;

        disablePower(damager);
    }

    private void disablePower(Player player) {
        this.using = false;
        Messages.HAKI_FLUIDE_DISABLED.send(player);

        this.stuned.clear();
        Mugiwara.getInstance().removeActionBar(player, "fluide_timer");
        if (another) return;
        this.stuned.stream().filter(uuid -> Bukkit.getPlayer(uuid) != null).map(Bukkit::getPlayer).forEach(this::stopStun);
    }

    private void stopStun(Player player) {
        player.removePotionEffect(PotionEffectType.SLOW);
        player.removePotionEffect(PotionEffectType.BLINDNESS);
        player.removePotionEffect(PotionEffectType.JUMP);
        Messages.HAKI_FLUIDE_UNSTUNED.send(player);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();

        if (stuned.contains(player.getUniqueId())) event.setCancelled(true);
    }

    @EventHandler
    public void onKill(PlayerDeathEvent event) {
        if (event.getEntity().getKiller() == null) return;

        Player player = event.getEntity();
        Player killer = player.getKiller();

        if (!hasHaki(killer)) return;

        AbstractHaki hakiTarget = Mugiwara.getInstance().getHakiManager().getHaki(player);
        if (!(hakiTarget instanceof RoyalHaki)) return;

        used = false;
    }

    @Override
    public HakiType getHakiType() {
        return HakiType.ROYAL;
    }
}

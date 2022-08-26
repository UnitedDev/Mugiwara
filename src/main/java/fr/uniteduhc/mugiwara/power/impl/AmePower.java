package fr.uniteduhc.mugiwara.power.impl;

import fr.uniteduhc.mugiwara.Mugiwara;
import fr.uniteduhc.mugiwara.utils.config.Messages;
import fr.uniteduhc.mugiwara.power.CommandPower;
import fr.uniteduhc.mugiwara.utils.utils.packets.Spectator;
import fr.uniteduhc.mugiwara.utils.utils.Utils;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

@Getter
public class AmePower extends CommandPower {
    private Skeleton entity;

    @Override
    public String getArgument() {
        return "ame";
    }

    @Override
    public boolean onEnable(Player player, String[] args) {
        final Location loc = player.getLocation().clone();
        entity = loc.getWorld().spawn(loc, Skeleton.class);
        entity.setSkeletonType(Skeleton.SkeletonType.WITHER);
        entity.setRemoveWhenFarAway(false);
        entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 10, false, false));

        player.setGameMode(GameMode.SPECTATOR);
        player.teleport(loc.add(0.0, 5.0, 0.0));
        Utils.getPlayers().forEach(player1 -> player1.hidePlayer(player));
        Spectator.CANNOT_SPECTATE.add(player.getUniqueId());

        Messages.BROOK_AME_USE.send(player);
        final UUID uuid = player.getUniqueId();
        new BukkitRunnable() {
            private int timer = 60;

            @Override
            public void run() {
                if (entity == null || entity.isDead()) {
                    cancel();
                    return;
                }
                timer--;

                Player player = Bukkit.getPlayer(uuid);
                if (player == null) return;

                Utils.getPlayers().forEach(player1 -> player1.hidePlayer(player));
                if (timer == 0) {
                    stop(player);
                    cancel();
                }
            }
        }.runTaskTimer(Mugiwara.getInstance(), 0, 20);
        return true;
    }

    public void stop(Player player) {
        player.teleport(entity);
        entity.remove();
        entity = null;
        Utils.getPlayers().forEach(player1 -> player1.showPlayer(player));
        player.setGameMode(GameMode.SURVIVAL);
        Spectator.CANNOT_SPECTATE.remove(player.getUniqueId());
        Messages.BROOK_AME_END.send(player);
    }

    @Override
    public String getName() {
        return "Ame";
    }

    @Override
    public Integer getCooldownAmount() {
        return Utils.getTimeBeforeEpisode();
    }
}

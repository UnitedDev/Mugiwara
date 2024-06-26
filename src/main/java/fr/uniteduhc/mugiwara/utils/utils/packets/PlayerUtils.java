package fr.uniteduhc.mugiwara.utils.utils.packets;

import fr.uniteduhc.mugiwara.Mugiwara;
import fr.uniteduhc.mugiwara.utils.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.UUID;

public class PlayerUtils {

    public static void setAbsoHearths(final Player p, final int coeur) {
        ((CraftPlayer) p).getHandle().setAbsorptionHearts((float) coeur);
    }

    public static double getAbsoHearths(final Player p) {
        return ((CraftPlayer) p).getHandle().getAbsorptionHearts();
    }

    public static void makePlayerSeePlayersHealthAboveHead(final Player player) {
        final ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
        final Scoreboard scoreboard = scoreboardManager.getNewScoreboard();
        final Objective objective = (scoreboard.getObjective("health") == null) ? scoreboard.registerNewObjective("health", "health") : scoreboard.getObjective("health");
        objective.setDisplayName(ChatColor.RED + "\u2764");
        objective.setDisplaySlot(DisplaySlot.BELOW_NAME);
        player.setScoreboard(scoreboard);
    }

    public static void stopSeeHealthHead(final Player player) {
        player.getScoreboard().clearSlot(DisplaySlot.BELOW_NAME);
    }

    public static void giveInvisible(final Player player, final int secondes) {
        final UUID uuid = player.getUniqueId();
        new BukkitRunnable() {
            int cooldown = secondes;

            public void run() {
                final Player connected = Bukkit.getPlayer(uuid);

                if (cooldown == 0) {
                    Utils.getPlayers().forEach(p -> p.showPlayer(connected));
                    cancel();
                    return;
                }

                if (connected == null) return;

                Utils.getPlayers().forEach(p -> p.hidePlayer(connected));
                --this.cooldown;
            }
        }.runTaskTimer(Mugiwara.getInstance(), 20L, 20L);
    }
}
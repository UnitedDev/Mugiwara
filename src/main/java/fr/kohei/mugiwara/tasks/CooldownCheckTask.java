package fr.kohei.mugiwara.tasks;

import fr.kohei.mugiwara.Mugiwara;
import fr.kohei.mugiwara.config.Messages;
import fr.kohei.mugiwara.config.Replacement;
import fr.kohei.mugiwara.game.MUPlayer;
import fr.kohei.mugiwara.power.Power;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.scheduler.BukkitRunnable;

public class CooldownCheckTask extends BukkitRunnable {

    public CooldownCheckTask() {
        super.runTaskTimer(Mugiwara.getInstance(), 20, 20);
    }

    @Override
    public void run() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            MUPlayer muPlayer = MUPlayer.get(player);

            if (player.getGameMode() == GameMode.SPECTATOR) return;
            if (muPlayer.getRole() == null) return;

            for (Power power : muPlayer.getRole().getPowers()) {
                if (power.getCooldown() == null) continue;

                if (power.getCooldown().getSeconds() == 1) {
                    Messages.COOLDOWN_EXPIRE.send(player,
                            new Replacement("<name>", power.getCooldown().getName())
                    );
                }
            }
        });
    }
}

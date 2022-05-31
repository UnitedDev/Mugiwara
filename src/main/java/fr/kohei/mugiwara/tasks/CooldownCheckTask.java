package fr.kohei.mugiwara.tasks;

import fr.kohei.mugiwara.Mugiwara;
import fr.kohei.mugiwara.config.Messages;
import fr.kohei.mugiwara.config.Replacement;
import fr.kohei.mugiwara.game.MUPlayer;
import fr.kohei.mugiwara.power.Power;
import fr.kohei.utils.TimeUtil;
import fr.kohei.utils.Title;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;
import java.util.HashMap;

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
                    Mugiwara.getInstance().removeActionBar(player, power.getCooldown().getName());
                } else if (power.getCooldown().getSeconds() > 1) {
                    power.getCooldown().onSecond();
                    Mugiwara.getInstance().addActionBar(
                            player,
                            "&c" + power.getCooldown().getName() + " &8» &f" + TimeUtil.getReallyNiceTime2(power.getCooldown().getSeconds() * 1000L),
                            power.getCooldown().getName()
                    );
                }
            }

            Collection<String> hotbars = Mugiwara.getInstance().getHotbar().getOrDefault(player.getUniqueId(), new HashMap<>()).values();
            if (hotbars.isEmpty()) return;

            StringBuilder builder = new StringBuilder();
            for (String hotbar : hotbars) {
                if (builder.toString().equals("")) {
                    builder.append(hotbar);
                    continue;
                }

                builder.append(" &8┃ ").append(hotbar);
            }
            Title.sendActionBar(player, builder.toString());
        });
    }
}

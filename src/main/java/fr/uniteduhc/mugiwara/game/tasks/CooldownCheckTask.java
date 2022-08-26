package fr.uniteduhc.mugiwara.game.tasks;

import fr.uniteduhc.mugiwara.Mugiwara;
import fr.uniteduhc.mugiwara.utils.config.Messages;
import fr.uniteduhc.mugiwara.utils.config.Replacement;
import fr.uniteduhc.mugiwara.game.player.MUPlayer;
import fr.uniteduhc.mugiwara.power.Power;
import fr.uniteduhc.mugiwara.utils.utils.Utils;
import fr.uniteduhc.utils.TimeUtil;
import fr.uniteduhc.utils.Title;
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
        Utils.getPlayers().forEach(player -> {
            MUPlayer muPlayer = MUPlayer.get(player);

            if (player.getGameMode() == GameMode.SPECTATOR) return;
            if (muPlayer.getRole() == null) return;

            for (Power power : muPlayer.getRole().getPowers()) {
                if (power.getCooldown() == null) continue;

                if(power.getCooldown().getSeconds() > 0) {
                    power.getCooldown().onSecond();
                }

                if (power.getCooldown().getSeconds() == 1) {
                    Messages.COOLDOWN_EXPIRE.send(player,
                            new Replacement("<name>", power.getCooldown().getName())
                    );
                    Mugiwara.getInstance().removeActionBar(player, power.getCooldown().getName());
                } else if (power.getCooldown().getSeconds() > 1) {
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

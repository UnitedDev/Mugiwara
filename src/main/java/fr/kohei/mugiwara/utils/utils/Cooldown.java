package fr.kohei.mugiwara.utils.utils;

import fr.kohei.mugiwara.Mugiwara;
import fr.kohei.utils.ChatUtil;
import fr.kohei.utils.TimeUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

@Getter
@RequiredArgsConstructor
public class Cooldown {

    private final String name;
    private BukkitTask runnable;
    private int seconds;

    public static boolean canUsePower(Player player) {
        if (Mugiwara.getInstance().getPowerBlocked().contains(player.getUniqueId())) {
            player.sendMessage(ChatUtil.prefix("&cVous ne pouvez pas utiliser de pouvoirs."));
            return false;
        }

        return true;
    }

    public void setCooldown(int seconds) {
        this.seconds = seconds;
    }

    public void task() {
        if (runnable != null)
            runnable.cancel();
        runnable = new BukkitRunnable() {
            @Override
            public void run() {
                if (seconds > 0) onSecond();
                else cancel();
            }
        }.runTaskTimer(Mugiwara.getInstance(), 0, 20);
    }

    public void onSecond() {
        this.seconds -= 1;
    }

    public String getCooldownMessage() {
        return ChatUtil.prefix("&cVous ne pouvez pas utiliser cette capacité. Elle sera de nouveau disponible dans " + TimeUtil.getReallyNiceTime2(this.getSeconds() * 1000L));
    }

    public boolean isCooldown(Player player) {
        if (!canUsePower(player)) return true;
        if (this.getSeconds() <= 0) return false;

        player.sendMessage(getCooldownMessage());

        return true;
    }
    public boolean isCooldownNoMessage(Player player) {
        if (!canUsePower(player)) return true;
        if (this.getSeconds() <= 0) return false;

        return true;
    }

}

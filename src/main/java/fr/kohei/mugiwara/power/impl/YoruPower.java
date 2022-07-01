package fr.kohei.mugiwara.power.impl;

import fr.kohei.mugiwara.Mugiwara;
import fr.kohei.mugiwara.power.CommandPower;
import fr.kohei.mugiwara.utils.config.Messages;
import fr.kohei.mugiwara.utils.config.Replacement;
import fr.kohei.mugiwara.utils.utils.Utils;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

@Getter
@Setter
public class YoruPower extends CommandPower {
    private boolean using;
    private int damage;
    private boolean theHit;

    @Override
    public String getArgument() {
        return "yoru";
    }

    @Override
    public boolean onEnable(Player player, String[] args) {
        using = true;
        damage = 0;
        theHit = false;

        Messages.MIHAWK_YORU_USE.send(player);
        // set using to false in 5 seconds
        new BukkitRunnable() {
            @Override
            public void run() {
                using = false;
                Messages.MIHAWK_YORU_CANHIT.send(player,
                        new Replacement("<damage>", getDamage() + "")
                );
                theHit = true;
            }
        }.runTaskLater(Mugiwara.getInstance(), 20 * 5);

        // set the hit to false in 30 seconds
        new BukkitRunnable() {
            @Override
            public void run() {
                theHit = false;
            }
        }.runTaskLater(Mugiwara.getInstance(), 20 * 30);
        return true;
    }

    @Override
    public String getName() {
        return "Yoru";
    }

    @Override
    public Integer getCooldownAmount() {
        return Utils.getTimeBeforeEpisode();
    }
}

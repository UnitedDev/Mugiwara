package fr.kohei.mugiwara.power.impl;

import fr.kohei.mugiwara.Mugiwara;
import fr.kohei.mugiwara.utils.config.Messages;
import fr.kohei.mugiwara.utils.config.Replacement;
import fr.kohei.mugiwara.power.CommandPower;
import fr.kohei.mugiwara.utils.utils.Utils;
import fr.kohei.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class VolPower extends CommandPower {
    @Override
    public String getArgument() {
        return "voler";
    }

    @Override
    public boolean onEnable(Player player, String[] args) {
        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            player.sendMessage(ChatUtil.prefix("&cCe joueur n'est pas connecté."));
            return false;
        }

        if (target.getLocation().distance(player.getLocation()) > 15) {
            player.sendMessage(ChatUtil.prefix("&cVous devez être à moins de 15 blocs du joueur."));
            return false;
        }

        int apples = (int) (Math.random() * 3) + 1;
        int inventoryApples = Utils.getItemAmount(player, Material.GOLDEN_APPLE);
        if (apples > inventoryApples) apples = inventoryApples;

        Utils.removeItem(player, Material.GOLDEN_APPLE, apples);
        Messages.NAMI_VOL_USE.send(player,
                new Replacement("<amount>", "" + apples),
                new Replacement("<name>", target.getName())
        );

        final UUID uuid = target.getUniqueId();
        final int finalApples = apples;
        new BukkitRunnable() {
            @Override
            public void run() {
                Player target = Bukkit.getPlayer(uuid);
                Messages.NAMI_VOL_ONME.send(target,
                        new Replacement("<amount>", "" + finalApples)
                );
            }
        }.runTaskLater(Mugiwara.getInstance(), 60 * 20);
        return true;
    }

    @Override
    public String getName() {
        return "Vol";
    }

    @Override
    public Integer getCooldownAmount() {
        return Utils.getTimeBeforeEpisode();
    }
}

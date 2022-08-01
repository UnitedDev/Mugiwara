package fr.kohei.mugiwara.power.impl;

import fr.kohei.mugiwara.Mugiwara;
import fr.kohei.mugiwara.utils.config.Messages;
import fr.kohei.mugiwara.utils.config.Replacement;
import fr.kohei.mugiwara.game.player.MUPlayer;
import fr.kohei.mugiwara.power.CommandPower;
import fr.kohei.mugiwara.roles.mugiwara.NamiRole;
import fr.kohei.mugiwara.utils.utils.Arrow;
import fr.kohei.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class PisterPower extends CommandPower {
    private int uses = 0;

    @Override
    public String getArgument() {
        return "pister";
    }

    @Override
    public boolean onEnable(Player player, String[] args) {
        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            player.sendMessage(ChatUtil.prefix("&cCe joueur n'est pas connecté."));
            return false;
        }

        NamiRole nami = (NamiRole) MUPlayer.get(player).getRole();
        if (!nami.getSeenPlayers().contains(target.getUniqueId())) {
            Messages.NAMI_PISTER_NOTSEEN.send(player,
                    new Replacement("<name>", target.getName())
            );
            return false;
        }

        uses++;
        if (uses >= 3) {
            player.sendMessage(ChatUtil.prefix("&cVous avez déjà utilisé ce pouvoir 3x."));
            return false;
        }


        Messages.NAMI_PISTER_USE.send(player,
                new Replacement("<name>", target.getName())
        );
        Arrow.setArrow(player, target, 5 * 60);

        final UUID uuid = player.getUniqueId();
        new BukkitRunnable() {
            @Override
            public void run() {
                Player player = Bukkit.getPlayer(uuid);
                if (player == null) {
                    cancel();
                    return;
                }

                Messages.NAMI_PISTER_END.send(player);
            }
        }.runTaskLater(Mugiwara.getInstance(), 5 * 60 * 20);
        return true;
    }

    @Override
    public String getName() {
        return "Pister";
    }

    @Override
    public Integer getCooldownAmount() {
        return 5 * 60;
    }
}

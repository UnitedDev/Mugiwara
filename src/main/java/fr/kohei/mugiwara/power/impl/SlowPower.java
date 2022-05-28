package fr.kohei.mugiwara.power.impl;

import fr.kohei.mugiwara.config.Messages;
import fr.kohei.mugiwara.config.Replacement;
import fr.kohei.mugiwara.game.MUPlayer;
import fr.kohei.mugiwara.power.CommandPower;
import fr.kohei.mugiwara.roles.impl.mugiwara.BrookRole;
import fr.kohei.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class SlowPower extends CommandPower {
    private boolean used = false;

    @Override
    public String getArgument() {
        return "slow";
    }

    @Override
    public boolean onEnable(Player player, String[] args) {
        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            player.sendMessage(ChatUtil.prefix("&cCe joueur n'est pas connecté."));
            return false;
        }

        if (used) {
            player.sendMessage(ChatUtil.prefix("&cVous avez déjà utilisé ce pouvoir."));
            return false;
        }

        BrookRole brookRole = (BrookRole) MUPlayer.get(player).getRole();
        brookRole.setSlowTarget(target.getUniqueId());
        Messages.BROOK_SLOW_USE.send(player, new Replacement("<name>", target.getName()));

        used = true;
        return true;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public Integer getCooldownAmount() {
        return null;
    }
}

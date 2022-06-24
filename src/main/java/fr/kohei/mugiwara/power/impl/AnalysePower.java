package fr.kohei.mugiwara.power.impl;

import fr.kohei.mugiwara.game.player.MUPlayer;
import fr.kohei.mugiwara.power.CommandPower;
import fr.kohei.mugiwara.roles.impl.marine.LieutenantRole;
import fr.kohei.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class AnalysePower extends CommandPower {
    @Override
    public String getArgument() {
        return "analyse";
    }

    @Override
    public boolean onEnable(Player player, String[] args) {
        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            player.sendMessage(ChatUtil.prefix("&cCe joueur n'est pas connecté."));
            return false;
        }

        LieutenantRole lieutenantRole = (LieutenantRole) MUPlayer.get(player).getRole();
        if (!lieutenantRole.isCanAnalyse()) {
            player.sendMessage(ChatUtil.prefix("&cVous ne pouvez pas utiliser ce pouvoir maintenant."));
            return false;
        }

        lieutenantRole.setCanAnalyse(false);
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

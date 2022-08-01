package fr.kohei.mugiwara.power.impl;

import fr.kohei.mugiwara.game.player.MUPlayer;
import fr.kohei.mugiwara.power.CommandPower;
import fr.kohei.mugiwara.roles.RolesType;
import fr.kohei.mugiwara.roles.solo.SaboRole;
import fr.kohei.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class HealLuffyPower extends CommandPower {
    @Override
    public String getArgument() {
        return "tptoluffy";
    }

    @Override
    public boolean onEnable(Player player, String[] args) {
        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            player.sendMessage(ChatUtil.prefix("&cCe joueur n'est pas connecté."));
            return false;
        }

        SaboRole role = (SaboRole) MUPlayer.get(player).getRole();
        if(role.getPacte() == null || role.getPacte() != SaboRole.Pacte.ONE) return false;

        if(MUPlayer.get(player).getRole().getRole() != RolesType.LUFFY) {
            return false;
        }

        if(target.getHealth() > 10) {
            player.sendMessage(ChatUtil.prefix("&cCe joueur n'est pas à moins de 5 coeurs."));
            return false;
        }

        int randomX = (int) (Math.random() * 3);
        int randomZ = (int) (Math.random() * 3);
        player.teleport(target.getLocation().add(randomX, 0, randomZ));

        return true;
    }

    @Override
    public String getName() {
        return "Luffy TP";
    }

    @Override
    public Integer getCooldownAmount() {
        return 15 * 20;
    }
}

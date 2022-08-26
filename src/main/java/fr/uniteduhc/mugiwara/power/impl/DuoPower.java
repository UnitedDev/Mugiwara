package fr.uniteduhc.mugiwara.power.impl;

import fr.uniteduhc.mugiwara.Mugiwara;
import fr.uniteduhc.mugiwara.game.player.MUPlayer;
import fr.uniteduhc.mugiwara.power.CommandPower;
import fr.uniteduhc.mugiwara.roles.RolesType;
import fr.uniteduhc.mugiwara.roles.solo.SaboRole;
import fr.uniteduhc.mugiwara.utils.config.Messages;
import fr.uniteduhc.mugiwara.utils.config.Replacement;
import fr.uniteduhc.utils.ChatUtil;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class DuoPower extends CommandPower {
    @Override
    public String getArgument() {
        return "duo";
    }

    @Override
    public boolean onEnable(Player player, String[] args) {
        StringBuilder text = new StringBuilder();
        Arrays.asList(args).forEach(s -> text.append(s).append(" "));

        String message = text.toString();
        message.replaceFirst(args[0] + " ", "");

        Player kuma = Mugiwara.findRole(RolesType.KUMA);
        Player sabo = Mugiwara.findRole(RolesType.SABO);

        if (sabo == null || kuma == null) {
            player.sendMessage(ChatUtil.prefix("&cMalheuresement votre duo n'est pas dans la partie."));
            return false;
        }

        SaboRole saboRole = (SaboRole) MUPlayer.get(sabo).getRole();
        if (saboRole.getPacte() != SaboRole.Pacte.THREE) {
            player.sendMessage(ChatUtil.prefix("&cVous ne pouvez pas utiliser cette commande si Sabo n'a pas sélectionné le pacte numéro 3."));
            return false;
        }
        if (!saboRole.isPacte3()) {
            player.sendMessage(ChatUtil.prefix("&cVous devez rester plus de 10 minutes à côté de votre duo pour utiliser cette commande."));
            return false;
        }

        String roleName = MUPlayer.get(player).getRole().getName();

        Messages.SABO_DUO_CHAT.send(sabo,
                new Replacement("<role>", roleName),
                new Replacement("<message>", message)
        );
        Messages.SABO_DUO_CHAT.send(kuma,
                new Replacement("<role>", roleName),
                new Replacement("<message>", message)
        );
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

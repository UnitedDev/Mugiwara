package fr.kohei.mugiwara.power.impl;

import fr.kohei.mugiwara.Mugiwara;
import fr.kohei.mugiwara.utils.config.Messages;
import fr.kohei.mugiwara.power.CommandPower;
import fr.kohei.mugiwara.roles.RolesType;
import fr.kohei.mugiwara.utils.utils.Arrow;
import fr.kohei.mugiwara.utils.utils.Utils;
import fr.kohei.uhc.UHC;
import fr.kohei.utils.ChatUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

@Getter
@Setter
public class ChecklistPower extends CommandPower {
    private UUID targetUuid;

    @Override
    public String getArgument() {
        return "checklist";
    }

    @Override
    public boolean onEnable(Player player, String[] args) {
        // get the player target with the args 1
        Player target = Bukkit.getPlayer(args[1]);
        // if the target is null send a message to the player and return false
        if (target == null) {
            player.sendMessage(ChatUtil.prefix("&cCe joueur n'est pas connecté."));
            return false;
        }

        // set the target uuid to the target uuid
        targetUuid = target.getUniqueId();

        // if the GameManager duration is not in 60 minutes or 70 minutes inclusive, send a message to the player and return false
        if (UHC.getGameManager().getDuration() < 60 * 60 || UHC.getGameManager().getDuration() > 70 * 60) {
            player.sendMessage(ChatUtil.prefix("&cVous ne pouvez pas utiliser cette commande dans cette période."));
            return false;
        }

        // send the commandant wanted target message to the target
        Messages.COMMANDANT_WANTED_TARGET.send(target);

        // add an arrow direction to all players with role Fujitora, Akainu and Kizaru) for 5 minutes with the name hided
        Utils.getPlayersWithRole(RolesType.FUJITORA, RolesType.AKAINU, RolesType.KIZARU).forEach(p ->
                Arrow.setArrow(p, target, 5 * 60, true));

        // set the target uuid to null 5 minutes after
        Bukkit.getScheduler().runTaskLater(Mugiwara.getInstance(), () -> targetUuid = null, 5 * 60 * 20);

        return true;
    }

    @Override
    public String getName() {
        return "Checklist";
    }

    @Override
    public Integer getCooldownAmount() {
        return 10 * 60;
    }
}

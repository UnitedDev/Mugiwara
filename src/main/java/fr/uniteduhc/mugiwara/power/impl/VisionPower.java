package fr.uniteduhc.mugiwara.power.impl;

import fr.uniteduhc.mugiwara.utils.config.Messages;
import fr.uniteduhc.mugiwara.utils.config.Replacement;
import fr.uniteduhc.mugiwara.game.player.MUPlayer;
import fr.uniteduhc.mugiwara.power.CommandPower;
import fr.uniteduhc.mugiwara.roles.mugiwara.FrankyRole;
import fr.uniteduhc.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class VisionPower extends CommandPower {
    @Override
    public String getArgument() {
        return "vision";
    }

    @Override
    public boolean onEnable(Player player, String[] args) {
        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            player.sendMessage(ChatUtil.prefix("&cCe joueur n'est pas connecté."));
            return false;
        }

        FrankyRole frankyRole = (FrankyRole) MUPlayer.get(player).getRole();
        if (frankyRole.getVisions().size() >= 3) {
            player.sendMessage(ChatUtil.prefix("&cVous avez déjà utilisé ce pouvoir 3 fois."));
            return false;
        }

        if (frankyRole.getVisions().contains(target.getUniqueId())) {
            player.sendMessage(ChatUtil.prefix("&cVous avez déjà utilisé ce pouvoir sur ce joueur."));
            return false;
        }

        frankyRole.getVisions().add(target.getUniqueId());
        Messages.FRANKY_VISION_USE.send(player, new Replacement("<name>", target.getName()));
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

package fr.kohei.mugiwara.power.impl;

import fr.kohei.mugiwara.Mugiwara;
import fr.kohei.mugiwara.game.player.MUPlayer;
import fr.kohei.mugiwara.power.CommandPower;
import fr.kohei.mugiwara.roles.marine.CobyRole;
import fr.kohei.mugiwara.utils.config.Messages;
import fr.kohei.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class DenDenMushiSeePower extends CommandPower {
    @Override
    public String getArgument() {
        return "seedendenmushi";
    }

    @Override
    public boolean onEnable(Player player, String[] args) {
        Player target = Bukkit.getPlayer(args[1]);
        CobyRole cobyRole = (CobyRole) MUPlayer.get(target).getRole();

        if (!Mugiwara.getInstance().getDenMushiManager().getChat().containsKey(target.getUniqueId())) {
            player.sendMessage(ChatUtil.prefix("&cCe joueur n'a pas de chat en cours."));
            return false;
        }

        if (Math.random() > 0.75) {
            player.sendMessage(ChatUtil.prefix("&cVous n'avez pas eu de chance, vous ne verrez pas les messages."));
            return true;
        }

        Messages.COBY_DENDENMUSHI_WILLSEE.send(player);
        cobyRole.setDendenMushiTarget(target.getUniqueId());
        return true;
    }

    @Override
    public String getName() {
        return "DenDen Mushi (Voir)";
    }

    @Override
    public Integer getCooldownAmount() {
        return 60;
    }
}

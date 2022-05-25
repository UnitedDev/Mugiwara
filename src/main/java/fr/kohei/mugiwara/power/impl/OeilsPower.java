package fr.kohei.mugiwara.power.impl;

import fr.kohei.mugiwara.config.Messages;
import fr.kohei.mugiwara.game.MUPlayer;
import fr.kohei.mugiwara.power.CommandPower;
import fr.kohei.mugiwara.roles.impl.mugiwara.RobinRole;
import fr.kohei.utils.ChatUtil;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class OeilsPower extends CommandPower {
    @Override
    public String getArgument() {
        return "oeil";
    }

    @Override
    public boolean onEnable(Player player, String[] args) {
        RobinRole robinRole = (RobinRole) MUPlayer.get(player).getRole();
        if (robinRole.getBlocks().size() >= 3) {
            player.sendMessage(ChatUtil.prefix("&cVous avez déjà utilisé ce pouvoir 3 fois."));
            return false;
        }

        if(robinRole.getBlocks().stream().anyMatch(block -> block.getLocation().distance(player.getLocation()) <= 25)) {
            player.sendMessage(ChatUtil.prefix("&cVous avez déjà posé un oeil autour d'ici."));
            return false;
        }

        robinRole.getBlocks().add(player.getLocation().getBlock());
        robinRole.getInZone().put(player.getLocation().getBlock(), new ArrayList<>());
        Messages.ROBIN_OEIL_USE.send(player);
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

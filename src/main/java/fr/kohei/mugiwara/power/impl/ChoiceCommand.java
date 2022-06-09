package fr.kohei.mugiwara.power.impl;

import fr.kohei.mugiwara.game.MUPlayer;
import fr.kohei.mugiwara.menu.ChoiceMenu;
import fr.kohei.mugiwara.power.CommandPower;
import fr.kohei.mugiwara.roles.impl.marine.SoldatRole;
import fr.kohei.utils.ChatUtil;
import org.bukkit.entity.Player;

public class ChoiceCommand extends CommandPower {
    @Override
    public String getArgument() {
        return "choice";
    }

    @Override
    public boolean onEnable(Player player, String[] args) {
        SoldatRole role = (SoldatRole) MUPlayer.get(player).getRole();

        // if the choice of the role is not null return and send a message to the player
        if (role.getChoice() != null) {
            player.sendMessage(ChatUtil.prefix("&cVous avez déjà fait un choix..."));
            return false;
        }

        new ChoiceMenu().openMenu(player);

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

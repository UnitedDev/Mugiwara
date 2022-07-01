package fr.kohei.mugiwara.power.impl;

import fr.kohei.mugiwara.game.menu.ChoosePacteMenu;
import fr.kohei.mugiwara.game.player.MUPlayer;
import fr.kohei.mugiwara.power.CommandPower;
import fr.kohei.mugiwara.roles.impl.solo.SaboRole;
import fr.kohei.utils.ChatUtil;
import org.bukkit.entity.Player;

public class ChoosePactePower extends CommandPower {
    @Override
    public String getArgument() {
        return "pacte";
    }

    @Override
    public boolean onEnable(Player player, String[] args) {
        SaboRole saboRole = (SaboRole) MUPlayer.get(player).getRole();
        if (saboRole.getPacte() != null) {
            player.sendMessage(ChatUtil.prefix("&cVous avez déjà choisi un pacte."));
            return false;
        }

        new ChoosePacteMenu().openMenu(player);
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

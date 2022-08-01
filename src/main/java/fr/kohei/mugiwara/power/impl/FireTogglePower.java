package fr.kohei.mugiwara.power.impl;

import fr.kohei.mugiwara.game.player.MUPlayer;
import fr.kohei.mugiwara.power.CommandPower;
import fr.kohei.mugiwara.roles.solo.SaboRole;
import fr.kohei.mugiwara.utils.config.Messages;
import org.bukkit.entity.Player;

public class FireTogglePower extends CommandPower {
    @Override
    public String getArgument() {
        return "fire";
    }

    @Override
    public boolean onEnable(Player player, String[] args) {
        SaboRole saboRole = (SaboRole) MUPlayer.get(player).getRole();
        saboRole.setFire(!saboRole.isFire());
        if (saboRole.isFire()) {
            Messages.SABO_FIRE_ON.send(player);
        } else {
            Messages.SABO_FIRE_OFF.send(player);
        }

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

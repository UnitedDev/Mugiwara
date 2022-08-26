package fr.uniteduhc.mugiwara.power.impl;

import fr.uniteduhc.mugiwara.game.player.MUPlayer;
import fr.uniteduhc.mugiwara.power.CommandPower;
import fr.uniteduhc.mugiwara.roles.marine.AkainuRole;
import fr.uniteduhc.mugiwara.utils.config.Messages;
import org.bukkit.entity.Player;

public class FirePower extends CommandPower {

    @Override
    public String getArgument() {
        return "fire";
    }

    @Override
    public boolean onEnable(Player player, String[] args) {

        if (args.length != 1) {
            Messages.AKAINU_FIRE_SYNTAX.send(player);
            return false;
        }

        MUPlayer muPlayer = MUPlayer.get(player);
        AkainuRole akainuRole = (AkainuRole) muPlayer.getRole();

        if (akainuRole.isFire()) {
            akainuRole.setFire(false);
            Messages.AKAINU_FIRE_DISABLE.send(player);
        } else {
            akainuRole.setFire(true);
            Messages.AKAINU_FIRE_ENABLE.send(player);
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

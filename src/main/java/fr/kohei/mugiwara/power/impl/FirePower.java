package fr.kohei.mugiwara.power.impl;

import fr.kohei.mugiwara.config.Messages;
import fr.kohei.mugiwara.game.MUPlayer;
import fr.kohei.mugiwara.power.CommandPower;
import fr.kohei.mugiwara.roles.impl.marine.KizaruRole;
import org.bukkit.entity.Player;

public class FirePower extends CommandPower {
    @Override
    public String getArgument() {
        return "fire";
    }

    @Override
    public boolean onEnable(Player player, String[] args) {
        KizaruRole kizaruRole = (KizaruRole) MUPlayer.get(player).getRole();

        if (kizaruRole.isFire()) {
            kizaruRole.setFire(false);
            // send the fire enabled message
            Messages.KIZARU_FIRE_DISABLE.send(player);
        } else {
            kizaruRole.setFire(true);
            // send the fire disabled message
            Messages.KIZARU_FIRE_DISABLE.send(player);
        }
        return true;
    }

    @Override
    public String getName() {
        return "Fire";
    }

    @Override
    public Integer getCooldownAmount() {
        return 15;
    }
}

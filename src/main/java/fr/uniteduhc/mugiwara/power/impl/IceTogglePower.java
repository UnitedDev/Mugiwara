package fr.uniteduhc.mugiwara.power.impl;

import fr.uniteduhc.mugiwara.game.player.MUPlayer;
import fr.uniteduhc.mugiwara.power.CommandPower;
import fr.uniteduhc.mugiwara.roles.RolesType;
import fr.uniteduhc.mugiwara.roles.solo.KuzanRole;
import fr.uniteduhc.mugiwara.utils.config.Messages;
import org.bukkit.entity.Player;

public class IcePower extends CommandPower {

    @Override
    public String getArgument() {
        return "ice";
    }

    @Override
    public boolean onEnable(Player player, String[] args) {
        MUPlayer muPlayer = MUPlayer.get(player);

        RolesType.MURole role = muPlayer.getRole();

        if(!(role instanceof KuzanRole)) return false;

        KuzanRole kuzanRole = (KuzanRole) role;

        kuzanRole.setIce(kuzanRole.isIce());

        if(kuzanRole.isIce()){
           Messages.KUZAN_ICE_ON.send(player);
        } else {
            Messages.KUZAN_ICE_OFF.send(player);
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

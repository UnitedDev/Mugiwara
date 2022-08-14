package fr.kohei.mugiwara.power.impl;

import fr.kohei.mugiwara.Mugiwara;
import fr.kohei.mugiwara.game.player.MUPlayer;
import fr.kohei.mugiwara.power.CommandPower;
import fr.kohei.mugiwara.roles.RolesType;
import fr.kohei.mugiwara.roles.marine.AkainuRole;
import fr.kohei.mugiwara.roles.marine.SengokuRole;
import fr.kohei.mugiwara.utils.config.Messages;
import fr.kohei.mugiwara.utils.config.Replacement;
import fr.kohei.mugiwara.utils.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ConvPower extends CommandPower {

    @Override
    public String getArgument() {
        return "conv";
    }

    @Override
    public boolean onEnable(Player player, String[] args) {

        if(args.length == 1){
            Messages.AKAINU_CONV_SYNTAX.send(player);
            return false;
        }

        MUPlayer muPlayer = MUPlayer.get(player);

        StringBuilder stringBuilder = new StringBuilder();
        for(String arg : args){
            stringBuilder.append(arg).append(" ");
        }

        String message = stringBuilder.toString().replaceFirst(args[0], "");

        /**
         * TODO EDIT
         */

        Bukkit.getScheduler().runTaskLater(Mugiwara.getInstance(), () -> {

            if(muPlayer.getRole() instanceof AkainuRole){
                Utils.getPlayersWithRole(RolesType.SENGOKU, RolesType.AKAINU).stream()
                        .forEach(players -> Messages.AKAINU_CONV_MESSAGE.send(players, new Replacement("<message>", message)));
            } else if(muPlayer.getRole() instanceof SengokuRole){
                Utils.getPlayersWithRole(RolesType.AKAINU, RolesType.SENGOKU).stream()
                        .forEach(players -> Messages.SENGOKU_CONV_MESSAGE.send(players, new Replacement("<message>", message)));
            }


        }, 25 * 20);

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

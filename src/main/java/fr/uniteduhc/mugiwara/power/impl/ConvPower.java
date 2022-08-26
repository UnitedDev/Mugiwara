package fr.uniteduhc.mugiwara.power.impl;

import fr.uniteduhc.mugiwara.power.CommandPower;
import fr.uniteduhc.mugiwara.utils.config.Messages;
import org.bukkit.entity.Player;

public class ConvPower extends CommandPower {

    @Override
    public String getArgument() {
        return "conv";
    }

    @Override
    public boolean onEnable(Player player, String[] args) {

        if(args.length == 1){
            Messages.CONV_SYNTAX.send(player);
            return false;
        }

        /**
         * TODO EDIT
         */

        /*MUPlayer muPlayer = MUPlayer.get(player);

        RolesType.MURole muRole = muPlayer.getRole();

        StringBuilder stringBuilder = new StringBuilder();
        for(String arg : args){
            stringBuilder.append(arg).append(" ");
        }

        String message = stringBuilder.toString().replaceFirst(args[0], "");

        Bukkit.getScheduler().runTaskLater(Mugiwara.getInstance(), () -> {

            if(muPlayer.getRole() instanceof AkainuRole){
                Utils.getPlayersWithRole(RolesType.SENGOKU, RolesType.AKAINU).stream()
                        .forEach(players -> Messages.AKAINU_CONV_MESSAGE.send(players, new Replacement("<message>", message)));
            } else if(muPlayer.getRole() instanceof SengokuRole){
                Utils.getPlayersWithRole(RolesType.AKAINU, RolesType.SENGOKU).stream()
                        .forEach(players -> Messages.SENGOKU_CONV_MESSAGE.send(players, new Replacement("<message>", message)));
            }


        }, 25 * 20);*/

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

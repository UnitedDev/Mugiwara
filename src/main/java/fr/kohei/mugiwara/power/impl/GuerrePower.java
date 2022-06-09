package fr.kohei.mugiwara.power.impl;

import fr.kohei.mugiwara.camp.CampType;
import fr.kohei.mugiwara.camp.impl.MugiwaraHeartCamp;
import fr.kohei.mugiwara.config.Messages;
import fr.kohei.mugiwara.config.Replacement;
import fr.kohei.mugiwara.game.MUPlayer;
import fr.kohei.mugiwara.power.CommandPower;
import fr.kohei.mugiwara.roles.RolesType;
import fr.kohei.mugiwara.utils.Utils;
import fr.kohei.uhc.game.player.UPlayer;
import fr.kohei.utils.ChatUtil;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class GuerrePower extends CommandPower {
    private boolean used;

    @Override
    public String getArgument() {
        return "guerre";
    }

    @Override
    public boolean onEnable(Player player, String[] args) {
        // if used send to the player a message "Vous avez déjà utilisé cette commande"
        if (used) {
            player.sendMessage(ChatUtil.prefix("&cVous avez déjà utilisé cette commande"));
            return false;
        }

        // get all players with role Kizaru, fujitora and akainu (by using the class Utils) and send them the commandant guerre coordinates message
        Utils.getPlayersWithRole(RolesType.KIZARU, RolesType.FUJITORA, RolesType.AKAINU).forEach(p -> {
            Messages.COMMANDANT_GUERRE_COORDINATES.send(p,
                    new Replacement("x", player.getLocation().getBlockX()),
                    new Replacement("y", player.getLocation().getBlockY()),
                    new Replacement("z", player.getLocation().getBlockZ())
            );
        });

        // get all near players with the camp MUGIWARA_HEART and add them the potion effect weakness for 1 minute
        Utils.getPlayersInCamp(CampType.MUGIWARA_HEART).stream().filter(player1 -> UPlayer.get(player1).getCamp() instanceof MugiwaraHeartCamp).forEach(player1 -> {
            player1.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 60, 0, false, false));
        });

        used = true;
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

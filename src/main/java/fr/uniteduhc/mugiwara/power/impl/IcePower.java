package fr.uniteduhc.mugiwara.power.impl;

import fr.uniteduhc.mugiwara.game.player.MUPlayer;
import fr.uniteduhc.mugiwara.power.CommandPower;
import fr.uniteduhc.mugiwara.roles.marine.KuzanRole;
import fr.uniteduhc.utils.ChatUtil;
import org.bukkit.entity.Player;

public class IcePower extends CommandPower {
    @Override
    public String getArgument() {
        return "ice";
    }

    @Override
    public boolean onEnable(Player player, String[] args) {
        KuzanRole kuzanRole = (KuzanRole) MUPlayer.get(player).getRole();

        kuzanRole.setIce(!kuzanRole.isIce());
        player.sendMessage(ChatUtil.prefix("&fVous avez " + (kuzanRole.isIce() ? "&aactivé" : "&cdesactivé") + " votre pouvoir de glace."));

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

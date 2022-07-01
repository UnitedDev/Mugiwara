package fr.kohei.mugiwara.power.impl;

import fr.kohei.mugiwara.utils.config.Messages;
import fr.kohei.mugiwara.utils.config.Replacement;
import fr.kohei.mugiwara.game.player.MUPlayer;
import fr.kohei.mugiwara.power.CommandPower;
import fr.kohei.mugiwara.roles.impl.alliance.KaidoRole;
import fr.kohei.mugiwara.roles.impl.mugiwara.FrankyRole;
import fr.kohei.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class SbirePower extends CommandPower {
    public boolean used = false;

    @Override
    public String getArgument() {
        return "sbire";
    }

    @Override
    public boolean onEnable(Player player, String[] args) {
        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            player.sendMessage(ChatUtil.prefix("&cCe joueur n'est pas connecté."));
            return false;
        }

        if (used) {
            player.sendMessage(ChatUtil.prefix("&cVous avez déjà utilisé ce pouvoir."));
            return false;
        }

        KaidoRole kaidoRole = (KaidoRole) MUPlayer.get(player).getRole();
        kaidoRole.sbirePlayer = target;
        kaidoRole.progression = 0;
        Messages.KAIDO_SBIRE_USE.send(player, new Replacement("<name>", target.getName()));

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
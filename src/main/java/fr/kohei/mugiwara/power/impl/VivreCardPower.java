package fr.kohei.mugiwara.power.impl;

import fr.kohei.mugiwara.power.CommandPower;
import fr.kohei.mugiwara.roles.mugiwara.LuffyRole;
import fr.kohei.mugiwara.utils.config.Messages;
import fr.kohei.mugiwara.utils.config.Replacement;
import fr.kohei.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class VivreCardPower extends CommandPower {
    private boolean used;

    @Override
    public String getArgument() {
        return "card";
    }

    @Override
    public boolean onEnable(Player player, String[] args) {
        if(used) {
            player.sendMessage(ChatUtil.prefix("&cVous avez déjà donné votre pouvoir."));
            return false;
        }

        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            player.sendMessage(ChatUtil.prefix("&cCe joueur n'est pas connecté."));
            return false;
        }

        target.getInventory().addItem(LuffyRole.LUFFY_VIVE_CARD.toItemStack());
        Messages.VIVECARD_LUFFY_GIVE.send(player, new Replacement("<name>", target.getName()));
        Messages.VIVECARD_LUFFY_RECEIVE.send(target);

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

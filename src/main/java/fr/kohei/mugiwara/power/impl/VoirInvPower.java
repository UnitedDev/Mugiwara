package fr.kohei.mugiwara.power.impl;

import fr.kohei.mugiwara.menu.ViewInventoryMenu;
import fr.kohei.mugiwara.power.CommandPower;
import fr.kohei.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class VoirInvPower extends CommandPower {
    private int uses;

    @Override
    public String getArgument() {
        return "inv";
    }

    @Override
    public boolean onEnable(Player player, String[] args) {
        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            player.sendMessage(ChatUtil.prefix("&cCe joueur n'est pas connecté."));
            return false;
        }

        if (uses >= 2) {
            player.sendMessage(ChatUtil.prefix("&cVous avez déjà utilisé ce pouvoir 2 fois."));
            return false;
        }

        new ViewInventoryMenu(target).openMenu(player);
        uses++;
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

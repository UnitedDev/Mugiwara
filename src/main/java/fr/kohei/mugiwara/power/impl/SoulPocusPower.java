package fr.kohei.mugiwara.power.impl;

import fr.kohei.mugiwara.game.menu.BigMomSelectMenu;
import fr.kohei.mugiwara.game.player.MUPlayer;
import fr.kohei.mugiwara.power.CommandPower;
import fr.kohei.mugiwara.roles.mugiwara.BrookRole;
import fr.kohei.mugiwara.roles.solo.BigMomRole;
import fr.kohei.mugiwara.utils.config.Messages;
import fr.kohei.mugiwara.utils.utils.packets.Damage;
import fr.kohei.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class SoulPocusPower extends CommandPower {
    private boolean used;

    @Override
    public String getArgument() {
        return "soul";
    }

    @Override
    public boolean onEnable(Player player, String[] args) {
        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            player.sendMessage(ChatUtil.prefix("&cCe joueur n'est pas connecté."));
            return false;
        }

        if(used) {
            player.sendMessage(ChatUtil.prefix("&cVous avez déjà utilisé ce pouvoir."));
            return false;
        }

        if(target.getLocation().distance(player.getLocation()) > 15) {
            player.sendMessage(ChatUtil.prefix("&cCe joueur est trop loin."));
            return false;
        }

        if(MUPlayer.get(target).getRole() instanceof BrookRole) {
            player.sendMessage(ChatUtil.prefix("&cVous ne pouvez pas utiliser ce pouvoir sur Brook."));
            return false;
        }

        used = true;
        BigMomRole bigMomRole = (BigMomRole) MUPlayer.get(player).getRole();
        bigMomRole.setCakeTarget(target.getUniqueId());
        Damage.CANNOT_DAMAGE.add(target.getUniqueId());
        Damage.CANNOT_DAMAGE.add(player.getUniqueId());
        Damage.NO_DAMAGE.add(target.getUniqueId());
        Damage.NO_DAMAGE.add(player.getUniqueId());

        Messages.BIGMOM_SOUL_USE.send(player);
        Messages.BIGMOM_SOUL_TARGET.send(target);

        new BigMomSelectMenu().openMenu(player);
        new BigMomSelectMenu().openMenu(target);
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

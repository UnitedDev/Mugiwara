package fr.kohei.mugiwara.power.impl;

import fr.kohei.mugiwara.camp.impl.MarineCamp;
import fr.kohei.mugiwara.game.player.MUPlayer;
import fr.kohei.mugiwara.power.CommandPower;
import fr.kohei.mugiwara.roles.RolesType;
import fr.kohei.mugiwara.roles.marine.FujitoraRole;
import fr.kohei.mugiwara.roles.marine.KuzanRole;
import fr.kohei.mugiwara.utils.config.Messages;
import fr.kohei.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class HakiPower extends CommandPower {
    @Override
    public String getArgument() {
        return "aura";
    }

    @Override
    public boolean onEnable(Player player, String[] args) {
        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            player.sendMessage(ChatUtil.prefix("&cCe joueur n'est pas connecté."));
            return false;
        }

        FujitoraRole role = (FujitoraRole) MUPlayer.get(player).getRole();
        int avancement = role.getAvancement().getOrDefault(target.getUniqueId(), 0);
        if(avancement < 5 * 60) {
            player.sendMessage(ChatUtil.prefix("&cVous devez rester plus de 5 minutes à côté de ce joueur."));
            return false;
        }

        if (hasAuraRed(target)) {
            Messages.FUJITORA_AURA_ROUGE.send(player);
        } else {
            Messages.FUJITORA_AURA_BLEU.send(player);
        }

        return true;
    }

    private boolean hasAuraRed(final Player target) {
        final RolesType.MURole role = MUPlayer.get(target).getRole();
        if (role instanceof KuzanRole) return false;
        return !(role.getStartCamp() instanceof MarineCamp) || role.getRole() == RolesType.DRAKE || role.getRole() == RolesType.HANCOCK || role.getRole() == RolesType.KUMA || role.getRole() == RolesType.SMOKER || role.getRole() == RolesType.MIHAWK;
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

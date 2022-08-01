package fr.kohei.mugiwara.power.impl;

import fr.kohei.mugiwara.Mugiwara;
import fr.kohei.mugiwara.game.events.DenMushiManager;
import fr.kohei.mugiwara.game.player.MUPlayer;
import fr.kohei.mugiwara.power.CommandPower;
import fr.kohei.mugiwara.roles.RolesType;
import fr.kohei.mugiwara.roles.marine.CobyRole;
import fr.kohei.mugiwara.utils.config.Messages;
import fr.kohei.mugiwara.utils.config.Replacement;
import fr.kohei.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class DenDenMushiChatPower extends CommandPower {
    @Override
    public String getArgument() {
        return "chat";
    }

    @Override
    public boolean onEnable(Player player, String[] args) {
        DenMushiManager denMushiManager = Mugiwara.getInstance().getDenMushiManager();
        if (!denMushiManager.getChat().containsKey(player.getUniqueId())) {
            player.sendMessage(ChatUtil.prefix("&cVous n'êtes en conversation avec personne."));
            return false;
        }

        UUID uuid = denMushiManager.getChat().get(player.getUniqueId());
        Player target = Bukkit.getPlayer(uuid);

        if (target == null) {
            player.sendMessage(ChatUtil.prefix("&cLe joueur n'est plus connecté."));
            return false;
        }

        String message = String.join(" ", args);
        message = message.replaceFirst(args[0] + " ", "");

        Messages.DENDEN_MUSHI_CHAT_FORMAT.send(player,
                new Replacement("<name>", player.getName()),
                new Replacement("<message>", message)
        );
        Messages.DENDEN_MUSHI_CHAT_FORMAT.send(target,
                new Replacement("<name>", player.getName()),
                new Replacement("<message>", message)
        );

        Player coby = Mugiwara.findRole(RolesType.COBY);
        if (coby == null) return true;

        CobyRole cobyRole = (CobyRole) MUPlayer.get(coby).getRole();
        if (cobyRole.getDendenMushiTarget() == null) return true;
        if (!(cobyRole.getDendenMushiTarget().equals(player.getUniqueId())
                || cobyRole.getDendenMushiTarget().equals(target.getUniqueId()))) return true;

        // 25% chance to return
        if (Math.random() > 0.75) {
            Messages.DENDEN_MUSHI_CHAT_FORMAT.send(coby,
                    new Replacement("<name>", "&k" + player.getName()),
                    new Replacement("<message>", "&k" + message)
            );
            return true;
        }

        Messages.DENDEN_MUSHI_CHAT_FORMAT.send(coby,
                new Replacement("<name>", "&k" + player.getName()),
                new Replacement("<message>", message)
        );
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

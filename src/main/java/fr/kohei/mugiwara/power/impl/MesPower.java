package fr.kohei.mugiwara.power.impl;

import fr.kohei.mugiwara.power.CommandPower;
import fr.kohei.mugiwara.utils.config.Messages;
import fr.kohei.mugiwara.utils.config.Replacement;
import fr.kohei.utils.ChatUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

@Setter
@Getter
public class MesPower extends CommandPower {
    private boolean used;
    private UUID target;

    @Override
    public String getArgument() {
        return "mes";
    }

    @Override
    public boolean onEnable(Player player, String[] args) {
        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            player.sendMessage(ChatUtil.prefix("&cCe joueur n'est pas connecté."));
            return false;
        }

        if (used) {
            player.sendMessage(ChatUtil.prefix("&cVous avez déjà utilisé cette commande."));
            return false;
        }

        if (target.getLocation().distance(player.getLocation()) > 10) {
            player.sendMessage(ChatUtil.prefix("&cCe joueur est trop loin (vous devez être à moins de 10 blocks)."));
            return false;
        }

        Messages.LAW_MES_TARGET.send(target);
        Messages.LAW_MES_USE.send(player, new Replacement("<name>", target.getName()));

        this.target = target.getUniqueId();
        target.setMaxHealth(target.getMaxHealth() - 6);
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

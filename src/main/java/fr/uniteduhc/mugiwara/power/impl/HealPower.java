package fr.uniteduhc.mugiwara.power.impl;

import fr.uniteduhc.mugiwara.power.CommandPower;
import fr.uniteduhc.mugiwara.utils.config.Messages;
import fr.uniteduhc.mugiwara.utils.config.Replacement;
import fr.uniteduhc.mugiwara.utils.utils.packets.MathUtil;
import fr.uniteduhc.utils.ChatUtil;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.EnumParticle;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@Getter
public class HealPower extends CommandPower {
    private int uses;

    @Override
    public String getArgument() {
        return "heal";
    }

    @Override
    public boolean onEnable(Player player, String[] args) {
        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            player.sendMessage(ChatUtil.prefix("&cCe joueur n'est pas connecté."));
            return false;
        }

        if (uses >= 2) {
            player.sendMessage(ChatUtil.prefix("&cVous avez déjà utilisé votre pouvoir 2 fois."));
            return false;
        }

        Messages.LAW_HEAL_TARGET.send(target);
        Messages.LAW_HEAL_USE.send(player, new Replacement("<name>", target.getName()));

        MathUtil.sendCircleParticle(EnumParticle.HEART, target.getLocation(), 2, 10);
        target.setHealth(target.getMaxHealth());
        uses++;
        return true;
    }

    @Override
    public String getName() {
        return "Heal";
    }

    @Override
    public Integer getCooldownAmount() {
        return 10;
    }
}

package fr.kohei.mugiwara.power.impl;

import fr.kohei.mugiwara.power.CommandPower;
import fr.kohei.mugiwara.utils.config.Messages;
import fr.kohei.mugiwara.utils.utils.packets.Damage;
import fr.kohei.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class WoshiPower extends CommandPower {
    private boolean used;

    @Override
    public String getArgument() {
        return "woshi";
    }

    @Override
    public boolean onEnable(Player player, String[] args) {
        if(used) {
            player.sendMessage(ChatUtil.prefix("&cVous avez déjà utilisé ce pouvoir."));
            return false;
        }

        used = true;
        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            player.sendMessage(ChatUtil.prefix("&cCe joueur n'est pas connecté."));
            return false;
        }

        Damage.addCantDamageTemp(target, 10);
        target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 10 * 20, 3, false, false));
        Messages.TSURU_WOSHI_HIT.send(target);
        Messages.TSURU_WOSHI_USE.send(player);
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

package fr.kohei.mugiwara.power;

import fr.kohei.mugiwara.roles.RolesType;
import fr.kohei.mugiwara.utils.utils.Cooldown;
import fr.kohei.mugiwara.utils.utils.Utils;
import fr.kohei.uhc.game.player.UPlayer;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@Getter
public abstract class Power {

    private final Cooldown cooldown;

    public Power() {
        if(getName() == null) cooldown = null;
        else this.cooldown = new Cooldown(getName());
    }

    public static void onUse(Player player) {
        for (Player onlinePlayer : Utils.getPlayers()) {
            RolesType.MURole role = (RolesType.MURole) UPlayer.get(onlinePlayer).getRole();

            if (role == null) continue;

            role.onAllUse(onlinePlayer, player);
        }
    }

    public abstract String getName();

    public abstract Integer getCooldownAmount();

}

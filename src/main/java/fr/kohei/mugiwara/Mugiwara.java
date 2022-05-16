package fr.kohei.mugiwara;

import fr.kohei.BukkitAPI;
import fr.kohei.mugiwara.game.MUCommands;
import fr.kohei.mugiwara.game.MUListener;
import fr.kohei.mugiwara.game.MUModule;
import fr.kohei.mugiwara.roles.RolesType;
import fr.kohei.uhc.UHC;
import fr.kohei.uhc.game.player.UPlayer;
import fr.kohei.utils.ChatUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class Mugiwara extends JavaPlugin {

    @Getter
    private static Mugiwara instance;

    private MUModule module;
    private List<UUID> powerBlocked;

    @Override
    public void onEnable() {
        instance = this;

        this.powerBlocked = new ArrayList<>();

        UHC.getModuleManager().setModule(module = new MUModule());

        this.getServer().getPluginManager().registerEvents(new MUListener(this), this);
        BukkitAPI.getCommandHandler().registerClass(MUCommands.class);
    }

    public static Player findRole(RolesType role) {
        Player toReturn = null;

        for (UUID uuid : UHC.getGameManager().getPlayers()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null) continue;
            UPlayer uPlayer = UPlayer.get(player);
            if (uPlayer.getRole() == null || !uPlayer.isAlive()) continue;

            if (uPlayer.getRole().getName().equalsIgnoreCase(role.getName())) {
                toReturn = player;
                break;
            }
        }

        return toReturn;
    }

    public static void knowsRole(Player player, RolesType role) {
        Player target = findRole(role);

        if (target == null) {
            player.sendMessage(ChatUtil.prefix("&c" + role.getName() + " n'est pas dans la partie."));
            return;
        }

        player.sendMessage(ChatUtil.prefix("&fLe &c" + role.getName() + " &fde la partie est &a" + target.getName()));
    }
}

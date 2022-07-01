package fr.kohei.mugiwara.power.impl;

import com.lunarclient.bukkitapi.LunarClientAPI;
import fr.kohei.mugiwara.Mugiwara;
import fr.kohei.mugiwara.game.player.MUPlayer;
import fr.kohei.mugiwara.power.CommandPower;
import fr.kohei.mugiwara.roles.impl.marine.LieutenantRole;
import fr.kohei.mugiwara.utils.config.Messages;
import fr.kohei.mugiwara.utils.config.Replacement;
import fr.kohei.mugiwara.utils.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.stream.Collectors;

public class RecherchePower extends CommandPower {
    private HashMap<UUID, Integer> rechercheTargets;

    @Override
    public String getArgument() {
        return "recherche";
    }

    @Override
    public boolean onEnable(Player player, String[] args) {
        if(rechercheTargets != null) return false;

        List<Player> players = new ArrayList<>();
        rechercheTargets = new HashMap<>();

        Utils.getPlayers().stream().sorted().forEach(player1 -> {
            if (players.size() >= 3) return;
            players.add(player1);
        });

        StringBuilder builder = new StringBuilder();
        for (Player player1 : players) {
            builder.append(player1.getName()).append("&f, &c0");
            LunarClientAPI.getInstance().overrideNametag(player1, Arrays.asList(
                    "§cAvancement: §f0/60",
                    player1.getName()
            ), player);
        }

        Messages.LIEUTENANT_RECHERCHE_TARGETS.send(player,
                new Replacement("<names>", builder)
        );
        players.forEach(player1 -> rechercheTargets.put(player1.getUniqueId(), 0));

        final UUID uuid = player.getUniqueId();
        new BukkitRunnable() {
            @Override
            public void run() {
                Player player = Bukkit.getPlayer(uuid);
                if (player == null) return;
                List<Player> players = rechercheTargets.keySet().stream().filter(uuid -> Bukkit.getPlayer(uuid) != null)
                        .map(Bukkit::getPlayer).collect(Collectors.toList());

                players.forEach(player1 -> {
                    if(player.getLocation().distance(player1.getLocation()) > 5) return;

                    int avancement = rechercheTargets.get(player1.getUniqueId());
                    avancement++;

                    LunarClientAPI.getInstance().overrideNametag(player1, Arrays.asList(
                            "§cAvancement: §f" + avancement + "/60",
                            player1.getName()
                    ), player);
                    rechercheTargets.put(player1.getUniqueId(), avancement);

                    LieutenantRole role = (LieutenantRole) MUPlayer.get(player).getRole();

                    if(avancement == 60) {
                        cancel();

                        if(role.getLastKiller() != null && role.getLastKiller().equals(player1.getUniqueId())) {
                            Messages.LIEUTENANT_RECHERCHE_KILLER.send(player);
                            role.setStrength(player1.getUniqueId());
                        } else {
                            Messages.LIEUTENANT_RECHERCHE_NOTKILLER.send(player);
                        }
                    }
                });
            }
        }.runTaskTimer(Mugiwara.getInstance(), 0, 20);
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

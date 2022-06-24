package fr.kohei.mugiwara.utils.utils;

import fr.kohei.mugiwara.camp.CampType;
import fr.kohei.mugiwara.game.player.MUPlayer;
import fr.kohei.mugiwara.roles.RolesType;
import fr.kohei.uhc.UHC;
import fr.kohei.uhc.game.EpisodeManager;
import fr.kohei.uhc.game.GameManager;
import fr.kohei.uhc.game.player.UPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Utils {
    public static int getTimeBeforeEpisode() {
        GameManager gameManager = UHC.getGameManager();
        EpisodeManager episodeManager = gameManager.getEpisodeManager();

        int episodeTime = (episodeManager.getEpisode() - 1) * 20 * 60;

        if (episodeManager.getEpisode() == 1) {
            return 20 * 60 - gameManager.getDuration();
        }

        return episodeTime - (gameManager.getDuration() - episodeTime);
    }

    public static String itemFormat(String name) {
        return "&8❘ &c&l" + name + " &8(&7Clic-droit&8)";
    }

    public static String notClickItem(String name) {
        return "&8❘ &c&l" + name;
    }

    public static int getItemAmount(Player player, Material material) {
        int toReturn = 0;
        for (ItemStack content : player.getInventory().getContents()) {
            if (content != null && content.getType() == material) {
                toReturn += content.getAmount();
            }
        }

        return toReturn;
    }

    public static void removeItem(Player player, Material material, int remove) {
        if (player.getInventory().getItem(player.getInventory().first(material)).getAmount() <= remove) {
            player.getInventory().removeItem(player.getInventory().getItem(player.getInventory().first(material)));
            return;
        }
        player.getInventory().getItem(player.getInventory().first(material)).setAmount(player.getInventory().getItem(player.getInventory().first(material)).getAmount() - remove);
        if (remove > 64) {
            player.getInventory().getItem(player.getInventory().first(material)).setAmount(player.getInventory().getItem(player.getInventory().first(material)).getAmount() - (remove - 64));
        }
    }

    public static List<Player> getPlayers() {
        return UHC.getGameManager().getPlayers().stream()
                .filter(uuid -> Bukkit.getPlayer(uuid) != null)
                .map(Bukkit::getPlayer)
                .collect(Collectors.toList());

    }

    public static List<Player> getNearPlayers(Player player, int radius) {
        return player.getNearbyEntities(radius, radius, radius).stream()
                .filter(entity -> entity instanceof Player)
                .map(entity -> (Player) entity)
                .filter(player1 -> UHC.getGameManager().getPlayers().contains(player1.getUniqueId()))
                .collect(Collectors.toList());
    }

    public static List<Player> getNearPlayersWithRole(Player player, int radius, RolesType... type) {
        List<RolesType> roles = Arrays.asList(type);
        return player.getNearbyEntities(radius, radius, radius).stream()
                .filter(entity -> entity instanceof Player)
                .map(entity -> (Player) entity)
                .filter(player1 -> UHC.getGameManager().getPlayers().contains(player1.getUniqueId()))
                .filter(player1 -> roles.contains(MUPlayer.get(player1).getRole().getRole()))
                .collect(Collectors.toList());
    }

    public static List<Player> getPlayersWithRole(RolesType... type) {
        List<RolesType> roles = Arrays.asList(type);
        return Bukkit.getOnlinePlayers().stream()
                .filter(Objects::nonNull)
                .map(entity -> (Player) entity)
                .filter(player1 -> UHC.getGameManager().getPlayers().contains(player1.getUniqueId()))
                .filter(player1 -> roles.contains(MUPlayer.get(player1).getRole().getRole()))
                .collect(Collectors.toList());
    }

    public static List<Player> getPlayersInCamp(CampType type) {
        return Bukkit.getOnlinePlayers().stream()
                .filter(player -> UHC.getGameManager().getPlayers().contains(player.getUniqueId()))
                .filter(player -> ((CampType.MUCamp) UPlayer.get(player).getCamp()).getCampType() == type)
                .collect(Collectors.toList());
    }
}

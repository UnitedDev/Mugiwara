package fr.uniteduhc.mugiwara;

import fr.uniteduhc.BukkitAPI;
import fr.uniteduhc.mugiwara.game.events.*;
import fr.uniteduhc.mugiwara.game.player.MUPlayer;
import fr.uniteduhc.mugiwara.utils.config.Messages;
import fr.uniteduhc.mugiwara.game.commands.MUCommands;
import fr.uniteduhc.mugiwara.game.listener.MUListener;
import fr.uniteduhc.mugiwara.game.module.MUModule;
import fr.uniteduhc.mugiwara.game.events.poneglyphe.PoneglypheManager;
import fr.uniteduhc.mugiwara.roles.RolesType;
import fr.uniteduhc.mugiwara.utils.utils.Utils;
import fr.uniteduhc.mugiwara.utils.utils.arrow.BowAimbot;
import fr.uniteduhc.uhc.UHC;
import fr.uniteduhc.uhc.game.player.UPlayer;
import fr.uniteduhc.uhc.module.manager.Camp;
import fr.uniteduhc.utils.ChatUtil;
import fr.uniteduhc.utils.Title;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nullable;
import java.util.*;

@Getter
@Setter
public class Mugiwara extends JavaPlugin {
    @Getter
    private static Mugiwara instance;

    private DenMushiManager denMushiManager;
    private OnePieceManager onePieceManager;
    private PoneglypheManager poneglypheManager;
    private DemonFruitManager fruitDuDemonManager;
    private TresorManager tresorManager;
    private HakiManager hakiManager;
    private MUModule module;

    private List<UUID> powerBlocked;
    private Map<UUID, List<RolesType>> knows;
    private Map<UUID, Map<String, String>> hotbar;

    @Override
    public void onEnable() {
        instance = this;
        this.saveConfig();

        this.powerBlocked = new ArrayList<>();
        this.hotbar = new HashMap<>();
        this.knows = new HashMap<>();

        this.denMushiManager = new DenMushiManager(this);
        this.onePieceManager = new OnePieceManager(this);
        this.poneglypheManager = new PoneglypheManager(this);
        this.fruitDuDemonManager = new DemonFruitManager(this);
        this.tresorManager = new TresorManager(this);
        this.hakiManager = new HakiManager(this);
        UHC.getInstance().getModuleManager().setModule(module = new MUModule());

        Messages.init();
        this.getServer().getPluginManager().registerEvents(new MUListener(this), this);
        BukkitAPI.getCommandHandler().registerCommands(MUCommands.class);
        BukkitAPI.getCommandHandler().registerCommands(DenMushiManager.class);
        new BowAimbot(this);
    }

    @Nullable
    public static Player findRole(RolesType role) {
        Player toReturn = null;

        for (UUID uuid : UHC.getInstance().getGameManager().getPlayers()) {
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
            return;
        }

        List<RolesType> types = Mugiwara.getInstance().getKnows().getOrDefault(player.getUniqueId(), new ArrayList<>());
        if (!types.contains(role)) {
            types.add(role);
            Mugiwara.getInstance().getKnows().put(player.getUniqueId(), types);
        }

        player.sendMessage(ChatUtil.prefix("&fLe &c" + role.getName() + " &fde la partie est &a" + target.getName()));
    }

    private boolean won;

    public void attemptWin() {
        if (won) return;
        HashMap<Camp, Integer> camp = new HashMap<>();
        for (Player player : Utils.getPlayers()) {
            RolesType.MURole role = MUPlayer.get(player).getRole();
            if (role != null && UPlayer.get(player).getCamp() != null) {
                camp.put(UPlayer.get(player).getCamp(), camp.getOrDefault(UPlayer.get(player).getCamp(), 0) + 1);
            }
        }
        if (camp.size() != 1) return;
        Camp winners = null;
        for (Camp camp1 : camp.keySet()) {
            winners = camp1;
            break;
        }
        won = true;
        String victoryTitle = ChatUtil.translate("&fVictoire du camp " + winners.getColor() + winners.getName());
        Bukkit.getOnlinePlayers().forEach(player -> Title.sendTitle(player, 10, 3 * 20, 10, "", victoryTitle));
        Bukkit.broadcastMessage(ChatUtil.translate("&7&m---------------------------"));
        Bukkit.broadcastMessage(ChatUtil.prefix(victoryTitle));
        Bukkit.broadcastMessage(" ");
        Utils.getPlayers().forEach(player -> {
            RolesType.MURole role = MUPlayer.get(player).getRole();
            if (role != null && UPlayer.get(player).getCamp() != null) {
                Bukkit.broadcastMessage(ChatUtil.prefix("&6" + player.getName() + " &f&l» &e" + role.getName()));
            }
        });
        Bukkit.broadcastMessage(ChatUtil.translate("&7&m---------------------------"));
        Bukkit.broadcastMessage(ChatUtil.prefix("&cFermeture du serveur dans 60 secondes..."));

//
//        Bukkit.getScheduler().runTaskLater(Mugiwara.getInstance(), () -> {
//            Bukkit.getOnlinePlayers().forEach(player -> player.kickPlayer("Fermeture du serveur..."));
//            Bukkit.getServer().shutdown();
//        }, 60 * 20);
    }

    public void addActionBar(Player player, String actionBar, String id) {
        Map<String, String> map = this.hotbar.getOrDefault(player.getUniqueId(), new HashMap<>());
        map.remove(id);
        map.put(id, actionBar);
        this.hotbar.put(player.getUniqueId(), map);
    }

    public void removeActionBar(Player player, String id) {
        Map<String, String> map = this.hotbar.getOrDefault(player.getUniqueId(), new HashMap<>());
        map.remove(id);
        this.hotbar.put(player.getUniqueId(), map);
    }

    public Collection<String> getActionBar(Player player) {
        return this.hotbar.getOrDefault(player.getUniqueId(), new HashMap<>()).values();
    }
}

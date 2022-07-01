package fr.kohei.mugiwara.game.onepiece;

import fr.kohei.mugiwara.camp.CampType;
import fr.kohei.mugiwara.game.player.MUPlayer;
import fr.kohei.mugiwara.roles.RolesType;
import fr.kohei.mugiwara.utils.utils.Utils;
import fr.kohei.uhc.UHC;
import fr.kohei.utils.ChatUtil;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
public class OnePieceManager implements Listener {
    private Block onePiece;
    private final HashMap<UUID, List<Location>> coordinates;

    public OnePieceManager(Plugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        coordinates = new HashMap<>();
    }

    public void startOnePiece() {
//        int x = (int) (Math.random() * 200);
//        int z = (int) (Math.random() * 200);
//        int y = (int) (Math.random() * 10) + 40;
//
//        Location location = new Location(UHC.getGameManager().getUhcWorld(), x, y, z);
//        location.getBlock().setType(Material.ENDER_CHEST);
//
//        onePiece = location.getBlock();
    }

    private void onOpen(Player player) {
        this.onePiece.setType(Material.AIR);

        List<Player> canOpen = Utils.getPlayersInCamp(CampType.MARINE);
        canOpen.addAll(Utils.getPlayersWithRole(RolesType.LUFFY, RolesType.BIG_MOM, RolesType.KAIDO, RolesType.SABO, RolesType.TEACH));
        List<UUID> uuids = canOpen.stream().map(Player::getUniqueId).collect(Collectors.toList());

        if(!uuids.contains(player.getUniqueId())) {
            player.sendMessage(ChatUtil.prefix("&cVotre rôle ne vous permet pas d'ouvrir le OnePiece."));
            return;
        }

        RolesType role = MUPlayer.get(player).getRole().getRole();

    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        if(block == null) return;
        Player player = event.getPlayer();

        if (block.getType() == Material.ENDER_CHEST && onePiece != null && block.getLocation() == onePiece.getLocation()) {
            onOpen(player);
        }
    }

}

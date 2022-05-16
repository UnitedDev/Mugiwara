package fr.kohei.mugiwara.game;

import fr.kohei.uhc.game.player.UPlayer;
import fr.kohei.uhc.module.manager.Role;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

@Getter
@Setter
public class MUPlayer {

    public static HashMap<UUID, MUPlayer> players = new HashMap<>();

    private final Player player;

    public MUPlayer(Player player) {
        players.put(player.getUniqueId(), this);

        this.player = player;

    }

    public static MUPlayer get(Player player) {
        if (players.get(player.getUniqueId()) == null) {
            return new MUPlayer(player);
        }

        return players.get(player.getUniqueId());
    }

    public Role getRole() {
        return UPlayer.get(player).getRole();
    }

}

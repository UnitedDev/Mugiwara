package fr.uniteduhc.mugiwara.roles.mugiwara;

import fr.uniteduhc.mugiwara.Mugiwara;
import fr.uniteduhc.mugiwara.game.player.MUPlayer;
import fr.uniteduhc.mugiwara.roles.RolesType;
import fr.uniteduhc.mugiwara.utils.utils.Utils;
import fr.uniteduhc.uhc.UHC;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ZoroRole extends RolesType.MURole implements Listener {
    public ZoroRole() {
        super(Arrays.asList(
        ), 320000000L);
    }

    @Override
    public RolesType getRole() {
        return RolesType.ZORO;
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(Material.IRON_SWORD);
    }

    @Override
    public void onDistribute(Player player) {

    }

    public static void randomRole(Player player) {
        List<RolesType> roles = Arrays.asList(RolesType.SANJI, RolesType.LAW, RolesType.EUSTASS, RolesType.PIRATE);

        List<RolesType> players = Utils.getPlayers().stream()
                .filter(p -> UHC.getInstance().getGameManager().getPlayers().contains(p.getUniqueId()))
                .filter(p -> MUPlayer.get(player).getRole() != null)
                .map(p -> MUPlayer.get(player).getRole().getRole())
                .filter(rolesType -> !roles.contains(rolesType))
                .collect(Collectors.toList());
        if (player.isEmpty()) return;
        Collections.shuffle(players);

        Mugiwara.knowsRole(player, players.get(0));
    }
}

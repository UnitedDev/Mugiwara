package fr.uniteduhc.mugiwara.game.menu;

import fr.uniteduhc.menu.Button;
import fr.uniteduhc.menu.pagination.PaginatedMenu;
import fr.uniteduhc.mugiwara.game.player.MUPlayer;
import fr.uniteduhc.mugiwara.roles.marine.BartholomewKumaRole;
import fr.uniteduhc.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class KumaBibleMenu extends PaginatedMenu {

    @Override
    public String getPrePaginatedTitle(Player player) {
        return "Kuma Bible";
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        BartholomewKumaRole role = (BartholomewKumaRole) MUPlayer.get(player).getRole();
        for (UUID uuid : role.getTeleportedPlayers()) {
            buttons.put(buttons.size(), new TargetButton(uuid));
        }
        return buttons;
    }

    private class TargetButton extends Button {
        private final UUID uuid;
        private Player player;

        public TargetButton(UUID uuid) {
            this.uuid = uuid;
            player = Bukkit.getPlayer(uuid);
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            player.closeInventory();
            player.teleport(this.player);
        }

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.SKULL_ITEM).setDurability(SkullType.PLAYER.ordinal()).setSkullOwner(player.getName()).setName("&c" + player.getName()).toItemStack();
        }
    }
}

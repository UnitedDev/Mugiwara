package fr.kohei.mugiwara.game.menu;

import fr.kohei.menu.Button;
import fr.kohei.menu.pagination.PaginatedMenu;
import fr.kohei.mugiwara.Mugiwara;
import fr.kohei.mugiwara.utils.utils.Utils;
import fr.kohei.utils.ItemBuilder;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class DenDenMushiMenu extends PaginatedMenu {
    @Override
    public String getPrePaginatedTitle(Player player) {
        return "Den Den Mushi";
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        for (Player player1 : Utils.getPlayers()) {
            if (player1.getUniqueId().equals(player.getUniqueId())) continue;
            buttons.put(buttons.size(), new TargetButton(player1));
        }

        return buttons;
    }

    @RequiredArgsConstructor
    private class TargetButton extends Button {
        private final Player target;

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.SKULL_ITEM).setDurability(SkullType.PLAYER.ordinal()).setSkullOwner(target.getName())
                    .setName("&c" + target.getName()).setLore(
                            "",
                            "&f&l» &cCliquez-ici pour y accéder"
                    ).toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            Mugiwara.getInstance().getDenMushiManager().onSelection(player, target);
            player.closeInventory();
        }
    }
}

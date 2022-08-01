package fr.kohei.mugiwara.game.menu;

import fr.kohei.menu.Button;
import fr.kohei.menu.pagination.PaginatedMenu;
import fr.kohei.mugiwara.game.player.MUPlayer;
import fr.kohei.mugiwara.roles.solo.SaboRole;
import fr.kohei.utils.ItemBuilder;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class ChoosePacteMenu extends PaginatedMenu {
    @Override
    public String getPrePaginatedTitle(Player player) {
        return "Pacte";
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        int i = 1;
        for (SaboRole.Pacte pacte : SaboRole.Pacte.values()) {
            buttons.put(i++, new PacteButton(pacte));
        }

        return buttons;
    }

    @RequiredArgsConstructor
    private class PacteButton extends Button {
        private final SaboRole.Pacte pacte;

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(pacte.getMaterial())
                    .setName(pacte.getDisplay())
                    .setLore(pacte.getLore())
                    .toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            SaboRole saboRole = (SaboRole) MUPlayer.get(player).getRole();

            saboRole.setPacte(pacte);
            pacte.getSelect().send(player);
            saboRole.onPacteSelection(player);
        }
    }
}

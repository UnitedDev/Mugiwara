package fr.kohei.mugiwara.game.menu;

import fr.kohei.menu.Button;
import fr.kohei.menu.pagination.PaginatedMenu;
import fr.kohei.mugiwara.game.player.MUPlayer;
import fr.kohei.mugiwara.roles.impl.marine.SoldatRole;
import fr.kohei.utils.ChatUtil;
import fr.kohei.utils.ItemBuilder;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class ChoiceMenu extends PaginatedMenu {
    @Override
    public String getPrePaginatedTitle(Player player) {
        return "Choisir une forme";
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        for (SoldatRole.Choices choice : SoldatRole.Choices.values()) {
            new ChoiceButton(choice);
        }

        return buttons;
    }

    @RequiredArgsConstructor
    private class ChoiceButton extends Button {
        private final SoldatRole.Choices choice;

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(choice.getMaterial()).setName(choice.getDisplay()).setLore(choice.getLore())
                    .addLoreLine(" ")
                    .addLoreLine(ChatUtil.translate("&f&l» &7Cliquez pour choisir cette forme"))
                    .toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            SoldatRole role = (SoldatRole) MUPlayer.get(player).getRole();

            role.setChoice(choice);
            role.onSelect(player, choice);
        }
    }
}

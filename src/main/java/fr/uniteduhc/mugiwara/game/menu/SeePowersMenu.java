package fr.uniteduhc.mugiwara.game.menu;

import fr.uniteduhc.menu.Button;
import fr.uniteduhc.menu.pagination.PaginatedMenu;
import fr.uniteduhc.mugiwara.game.player.MUPlayer;
import fr.uniteduhc.mugiwara.power.ClickPower;
import fr.uniteduhc.mugiwara.power.Power;
import fr.uniteduhc.mugiwara.roles.RolesType;
import fr.uniteduhc.utils.ItemBuilder;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class SeePowersMenu extends PaginatedMenu {
    private final Player target;

    @Override
    public String getPrePaginatedTitle(Player player) {
        return "Pouvoirs " + target.getName();
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        RolesType.MURole role = MUPlayer.get(target).getRole();
        for (Power power : role.getPowers()) {
            if (power instanceof ClickPower)
                buttons.put(buttons.size(), new PowerButton((ClickPower) power));
        }

        return buttons;
    }

    @RequiredArgsConstructor
    private class PowerButton extends Button {
        private final ClickPower power;

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(power.getItem()).setLore(
                    "",
                    "&f&l» &cCliquez-ici pour recevoir l'item"
            ).toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            player.getInventory().addItem(power.getItem());
        }
    }
}

package fr.uniteduhc.mugiwara.game.menu;

import fr.uniteduhc.menu.Button;
import fr.uniteduhc.menu.GlassMenu;
import fr.uniteduhc.menu.buttons.BackButton;
import fr.uniteduhc.mugiwara.Mugiwara;
import fr.uniteduhc.mugiwara.game.events.TresorManager;
import fr.uniteduhc.uhc.menu.options.ManageRolesMenu;
import fr.uniteduhc.utils.ItemBuilder;
import fr.uniteduhc.utils.TimeUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class TresorMenu extends GlassMenu {

    @Override
    public int getGlassColor() {
        return 4;
    }

    @Override
    public Map<Integer, Button> getAllButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(13, new ChangeTresorTime());

        buttons.put(22, new BackButton(new ManageRolesMenu(null)));
        return buttons;
    }

    @Override
    public String getTitle(Player player) {
        return "Tresor";
    }

    private class ChangeTresorTime extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.CHEST).setName("&cTemps du Trésor").setLore(
                    "",
                    "&8┃ &7Temps: &c" + TimeUtil.getReallyNiceTime2(Mugiwara.getInstance().getTresorManager().getTimer() * 1000L),
                    "",
                    "&f&l» &cClic-droit pour ajouter 1 minute",
                    "&f&l» &cClic-gauche pour enlever 1 minute"
            ).toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            TresorManager tresorManager = Mugiwara.getInstance().getTresorManager();

            if(clickType.name().contains("RIGHT")) {
                tresorManager.setTimer(tresorManager.getTimer() + 60);
            } else {
                tresorManager.setTimer(tresorManager.getTimer() - 60);
            }
        }

        @Override
        public boolean shouldUpdate(Player player, int slot, ClickType clickType) {
            return true;
        }
    }
}

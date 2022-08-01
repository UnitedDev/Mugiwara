package fr.kohei.mugiwara.game.events.poneglyphe;

import fr.kohei.menu.Button;
import fr.kohei.menu.GlassMenu;
import fr.kohei.menu.buttons.BackButton;
import fr.kohei.mugiwara.Mugiwara;
import fr.kohei.uhc.menu.options.ManageRolesMenu;
import fr.kohei.utils.ItemBuilder;
import fr.kohei.utils.TimeUtil;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class PoneglypheMenu extends GlassMenu {


    @Override
    public int getGlassColor() {
        return 14;
    }

    @Override
    public Map<Integer, Button> getAllButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(20, new PoneglypheButton(1));
        buttons.put(21, new PoneglypheButton(2));
        buttons.put(22, new PoneglypheButton(3));
        buttons.put(23, new PoneglypheButton(4));

        buttons.put(49, new BackButton(new ManageRolesMenu(null)));

        return buttons;
    }

    @Override
    public String getTitle(Player player) {
        return "Ponéglyphe";
    }

    @RequiredArgsConstructor
    private class PoneglypheButton extends Button {
        private final int id;

        @Override
        public ItemStack getButtonItem(Player player) {
            Poneglyphe poneglyphe;
            if(id == 1) poneglyphe = Mugiwara.getInstance().getPoneglypheManager().getFirstPoneglyphe();
            else if(id == 2) poneglyphe = Mugiwara.getInstance().getPoneglypheManager().getSecondPoneglyphe();
            else if(id == 3) poneglyphe = Mugiwara.getInstance().getPoneglypheManager().getThirdPoneglyphe();
            else poneglyphe = Mugiwara.getInstance().getPoneglypheManager().getFourthPoneglyphe();

            return new ItemBuilder(Material.REDSTONE_BLOCK).setName("&cPonéglyphe " + id).setLore(
                    "",
                    "&8┃ &7Temps: &c" + TimeUtil.getReallyNiceTime2(poneglyphe.getTimer() * 1000),
                    "",
                    "&f&l» &cClic-droit pour ajouter 1 minute",
                    "&f&l» &cClic-gauche pour enlever 1 minute"
            ).toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            Poneglyphe poneglyphe;
            if(id == 1) poneglyphe = Mugiwara.getInstance().getPoneglypheManager().getFirstPoneglyphe();
            else if(id == 2) poneglyphe = Mugiwara.getInstance().getPoneglypheManager().getSecondPoneglyphe();
            else if(id == 3) poneglyphe = Mugiwara.getInstance().getPoneglypheManager().getThirdPoneglyphe();
            else poneglyphe = Mugiwara.getInstance().getPoneglypheManager().getFourthPoneglyphe();

            if(clickType.name().contains("RIGHT")) {
                poneglyphe.setTimer(poneglyphe.getTimer() + 60);
            } else {
                poneglyphe.setTimer(poneglyphe.getTimer() - 60);
            }
        }

        @Override
        public boolean shouldUpdate(Player player, int slot, ClickType clickType) {
            return true;
        }
    }
}

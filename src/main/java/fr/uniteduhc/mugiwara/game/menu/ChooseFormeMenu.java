package fr.uniteduhc.mugiwara.game.menu;

import fr.uniteduhc.menu.Button;
import fr.uniteduhc.menu.Menu;
import fr.uniteduhc.menu.buttons.DisplayButton;
import fr.uniteduhc.mugiwara.utils.config.Messages;
import fr.uniteduhc.mugiwara.utils.config.Replacement;
import fr.uniteduhc.mugiwara.game.player.MUPlayer;
import fr.uniteduhc.mugiwara.power.impl.FormePower;
import fr.uniteduhc.mugiwara.roles.mugiwara.ChopperRole;
import fr.uniteduhc.utils.ItemBuilder;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.HashMap;
import java.util.Map;

public class ChooseFormeMenu extends Menu {

    @Override
    public String getTitle(Player player) {
        return "Hito Hito no Mi";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        for (int i : new int[]{0, 2, 4, 6, 8})
            buttons.put(i, new DisplayButton(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7)));

        buttons.put(1, new FormeButton(FormePower.FormeTypes.ONE));
        buttons.put(3, new FormeButton(FormePower.FormeTypes.TWO));
        buttons.put(5, new FormeButton(FormePower.FormeTypes.THREE));
        buttons.put(7, new FormeButton(FormePower.FormeTypes.FOUR));

        return buttons;
    }

    @RequiredArgsConstructor
    private class FormeButton extends Button {
        private final FormePower.FormeTypes type;

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(type.getMaterial()).setName("&c" + type.getName()).setLore(type.getDescription())
                    .addLoreLine("").addLoreLine("&f&l» &cCliquez-ici pour choisir").toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            ChopperRole chopperRole = (ChopperRole) MUPlayer.get(player).getRole();

            if (chopperRole.getType() == type) return;

            if (chopperRole.getType() != null)
                for (PotionEffect effect : chopperRole.getType().getEffects())
                    player.removePotionEffect(effect.getType());

            chopperRole.setType(type);
            for (PotionEffect effect : type.getEffects())
                player.addPotionEffect(effect);

            Messages.CHOPPER_FORME_USE.send(player, new Replacement("<name>", type.getName()));
            player.closeInventory();
        }
    }
}

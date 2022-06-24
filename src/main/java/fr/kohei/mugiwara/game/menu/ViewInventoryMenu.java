package fr.kohei.mugiwara.game.menu;

import fr.kohei.menu.Button;
import fr.kohei.menu.Menu;
import fr.kohei.menu.buttons.DisplayButton;
import fr.kohei.menu.buttons.Glass;
import fr.kohei.utils.ItemBuilder;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class ViewInventoryMenu extends Menu {
    private final Player target;

    @Override
    public String getTitle(Player paramPlayer) {
        return "Inventaire de " + target.getName();
    }

    @Override
    public Map<Integer, Button> getButtons(Player paramPlayer) {
        Map<Integer, Button> buttons = new HashMap<>();

        for (int i = 0; i < 18; i++) {
            buttons.put(i, new Glass());
        }

        PlayerInventory inv = target.getInventory();

        buttons.put(0, new DisplayButton(inv.getHelmet()));
        buttons.put(1, new DisplayButton(inv.getChestplate()));
        buttons.put(2, new DisplayButton(inv.getLeggings()));
        buttons.put(3, new DisplayButton(inv.getBoots()));

        buttons.put(5, new DisplayButton(new ItemBuilder(Material.COOKED_BEEF).setName("&8» &7Saturation: &c&k" + (int) target.getSaturation()).setLore("&8» &7Nourriture: &c" + target.getFoodLevel()).toItemStack()));
        buttons.put(6, new DisplayButton(new ItemBuilder(Material.GOLDEN_APPLE).setName("&8» &7Vie: &c&k" + (int) target.getHealth() + "&r&c ❤").toItemStack()));

        int i = 18;
        for (ItemStack content : inv.getContents()) {
            if (content == null) {
                buttons.put(i, new DisplayButton(new ItemStack(Material.AIR)));
            } else {
                buttons.put(i, new DisplayButton(content));
            }
            i++;
        }

        return buttons;
    }
}
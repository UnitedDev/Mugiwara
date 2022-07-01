package fr.kohei.mugiwara.game.menu;

import fr.kohei.menu.Button;
import fr.kohei.menu.pagination.PaginatedMenu;
import fr.kohei.mugiwara.game.player.MUPlayer;
import fr.kohei.mugiwara.roles.RolesType;
import fr.kohei.mugiwara.roles.impl.marine.XDrakeRole;
import fr.kohei.mugiwara.utils.utils.Utils;
import fr.kohei.utils.ChatUtil;
import fr.kohei.utils.ItemBuilder;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class AnalysePowerMenu extends PaginatedMenu {
    @Override
    public String getPrePaginatedTitle(Player player) {
        return "Analyse";
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
            String fruit = "&cRien";

            RolesType role = MUPlayer.get(target).getRole().getRole();
            if (role == RolesType.CHOPPER || role == RolesType.DRAKE || role == RolesType.KUMA || role == RolesType.SENGOKU
                    || role == RolesType.KAIDO || role == RolesType.KING) {
                fruit = "&aZoan";
            } else if (role == RolesType.SMOKER || role == RolesType.KIZARU || role == RolesType.AKAINU || role == RolesType.SABO
                    || role == RolesType.TEACH) {
                fruit = "&aLogia";
            } else if (role == RolesType.LUFFY || role == RolesType.ROBIN || role == RolesType.BROOK || role == RolesType.LAW
                    /*|| role == RolesType.TEACH*/ || role == RolesType.FUJITORA || role == RolesType.HANCOCK || role == RolesType.TSURU
                    || role == RolesType.BIG_MOM || role == RolesType.KATAKURI) {
                fruit = "&6Paramécia";
            }

            player.sendMessage(ChatUtil.prefix("&aFruit&f du démon de &c" + target.getName() + "&f: " + fruit));
            player.closeInventory();
        }
    }
}

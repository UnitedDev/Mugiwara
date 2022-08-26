package fr.uniteduhc.mugiwara.game.menu;

import fr.uniteduhc.menu.Button;
import fr.uniteduhc.menu.pagination.PaginatedMenu;
import fr.uniteduhc.mugiwara.Mugiwara;
import fr.uniteduhc.mugiwara.utils.config.Messages;
import fr.uniteduhc.mugiwara.utils.config.Replacement;
import fr.uniteduhc.mugiwara.utils.utils.Utils;
import fr.uniteduhc.mugiwara.utils.utils.packets.MathUtil;
import fr.uniteduhc.utils.ItemBuilder;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RequiredArgsConstructor
public class BusterCallTresorMenu extends PaginatedMenu {
    @Override
    public String getPrePaginatedTitle(Player player) {
        return "Sélectionnez un joueur";
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        for (Player player1 : Utils.getPlayers()) {
            buttons.put(buttons.size(), new SelectPlayerButton(player1));
        }

        return buttons;
    }

    @RequiredArgsConstructor
    private class SelectPlayerButton extends Button {
        private final Player target;

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.SKULL_ITEM).setDurability(SkullType.PLAYER.ordinal())
                    .setSkullOwner(target.getName()).setName("&c" + target.getName()).setLore(
                            "",
                            "&f&l» &eCliquez-ici pour sélectionner"
                    ).toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            player.getInventory().removeItem(Mugiwara.getInstance().getTresorManager().getBusterCallItem());
            Messages.TRESOR_BUSTERCALL_WARNING.send(target);
            Messages.TRESOR_BUSTERCALL_USE.send(player, new Replacement("<name>", target.getName()));

            enableBusterClassPower(player, target);
        }
    }

    private void enableBusterClassPower(Player player, Player target) {
        final int[] repeated = {0};

        new BukkitRunnable() {
            @Override
            public void run() {
                if (repeated[0] > 4) {
                    cancel();
                }

                repeated[0]++;
                tntDeluge(target);
            }
        }.runTaskTimer(Mugiwara.getInstance(), 5 * 20L, 5 * 20L);
    }

    private void tntDeluge(Player target) {
        for (Location location : MathUtil.getCircle(target.getLocation().clone().add(0, 15, 0), 10, false)) {
            if (new Random().nextInt(/*0, */100) < 20) {
                location.getWorld().spawnEntity(location, EntityType.PRIMED_TNT);
            }
        }
    }
}

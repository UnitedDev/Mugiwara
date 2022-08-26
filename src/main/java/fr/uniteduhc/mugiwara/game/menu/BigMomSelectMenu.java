package fr.uniteduhc.mugiwara.game.menu;

import fr.uniteduhc.menu.Button;
import fr.uniteduhc.menu.GlassMenu;
import fr.uniteduhc.mugiwara.Mugiwara;
import fr.uniteduhc.mugiwara.game.player.MUPlayer;
import fr.uniteduhc.mugiwara.roles.RolesType;
import fr.uniteduhc.mugiwara.roles.solo.BigMomRole;
import fr.uniteduhc.mugiwara.utils.config.Messages;
import fr.uniteduhc.mugiwara.utils.utils.packets.Damage;
import fr.uniteduhc.utils.ItemBuilder;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class BigMomSelectMenu extends GlassMenu {
    private boolean closed;

    @Override
    public int getGlassColor() {
        return 3;
    }

    @Override
    public Map<Integer, Button> getAllButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(12, new CakeButton(1));
        buttons.put(13, new CakeButton(2));
        buttons.put(14, new CakeButton(3));

        new BukkitRunnable() {
            @Override
            public void run() {
                BigMomRole bigMomRole = (BigMomRole) MUPlayer.get(Mugiwara.findRole(RolesType.BIG_MOM)).getRole();
                if (bigMomRole.getCakeTarget() != null && bigMomRole.getCakeTarget().equals(player.getUniqueId()) && !closed) {
                    player.setHealth(0);
                } else if (bigMomRole.getCakeTarget() != null && !closed) {
                    Player target = Bukkit.getPlayer(bigMomRole.getCakeTarget());
                    Player bigmom = Mugiwara.findRole(RolesType.BIG_MOM);

                    Damage.CANNOT_DAMAGE.remove(target.getUniqueId());
                    Damage.CANNOT_DAMAGE.remove(player.getUniqueId());
                    Damage.NO_DAMAGE.remove(target.getUniqueId());
                    Damage.NO_DAMAGE.remove(player.getUniqueId());

                    Messages.BIGMOM_SOULEND_BIGMOMDIDNTSELECT.send(target);
                    Messages.BIGMOM_SOULEND_BIGMOMDIDNTSELECT.send(bigmom);
                }
            }
        }.runTaskLater(Mugiwara.getInstance(), 30 * 20);

        return buttons;
    }

    @Override
    public String getTitle(Player player) {
        return "Soul Pocus";
    }

    @RequiredArgsConstructor
    private class CakeButton extends Button {
        private final int number;

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.CAKE).setName("&f&l» &cChoix n°" + number).toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            BigMomRole bigMom = (BigMomRole) MUPlayer.get(Mugiwara.findRole(RolesType.BIG_MOM)).getRole();
            bigMom.cakeEnable(player, number);
        }
    }
}

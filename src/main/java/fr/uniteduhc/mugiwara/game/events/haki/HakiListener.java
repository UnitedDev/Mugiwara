package fr.uniteduhc.mugiwara.game.events.haki;

import fr.uniteduhc.mugiwara.Mugiwara;
import fr.uniteduhc.mugiwara.utils.config.Messages;
import fr.uniteduhc.mugiwara.utils.config.Replacement;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class HakiListener implements Listener {
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item == null) return;
        if (!item.hasItemMeta()) return;
        if (!item.getItemMeta().hasDisplayName()) return;
        if (!item.isSimilar(Mugiwara.getInstance().getHakiManager().getHakiItem())) return;

        if (event.getAction().name().contains("LEFT")) {
            List<HakiType> allHakis = Mugiwara.getInstance().getHakiManager().getAllHakisForPlayer(player);
            if (allHakis == null || allHakis.isEmpty() || allHakis.size() == 1) return;

            AbstractHaki currentHaki = Mugiwara.getInstance().getHakiManager().getHaki(player);
            if (currentHaki == null) return;
            int index = allHakis.indexOf(currentHaki.getHakiType());

            if (allHakis.size() - 1 <= index) {
                index = 0;
            } else {
                index++;
            }

            HakiType newHaki = allHakis.get(index);
            Messages.HAKI_NEW_ONE.send(player, new Replacement("<haki>", newHaki.getDisplayName()));
            Mugiwara.getInstance().getHakiManager().setHaki(player, newHaki);
        } else if (event.getAction().name().contains("RIGHT")) {
            AbstractHaki usedHaki = Mugiwara.getInstance().getHakiManager().getHaki(player);
            if (usedHaki == null) return;

            usedHaki.onRightClick(player);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;
        Player player = (Player) event.getDamager();
        ItemStack item = player.getItemInHand();

        if (item == null) return;
        if (!item.hasItemMeta()) return;
        if (!item.getItemMeta().hasDisplayName()) return;
        if (!item.isSimilar(Mugiwara.getInstance().getHakiManager().getHakiItem())) return;

        AbstractHaki usedHaki = Mugiwara.getInstance().getHakiManager().getHaki(player);
        if (usedHaki == null) return;

        usedHaki.onDamageWithHaki(player);
    }
}

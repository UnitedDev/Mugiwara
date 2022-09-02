package fr.uniteduhc.mugiwara.game.events;

import fr.uniteduhc.mugiwara.Mugiwara;
import fr.uniteduhc.mugiwara.game.events.haki.AbstractHaki;
import fr.uniteduhc.mugiwara.game.events.haki.HakiType;
import fr.uniteduhc.mugiwara.game.player.MUPlayer;
import fr.uniteduhc.mugiwara.roles.RolesType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.List;

public class DemonFruitManager implements Listener {
    public DemonFruitManager(Plugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void onRoles() {

    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (!(event.getDamager() instanceof Player)) return;

        Player player = (Player) event.getEntity();
        Player damager = (Player) event.getDamager();
        RolesType role = MUPlayer.get(player).getRole().getRole();

        if (LOGIA.contains(role)) {
            AbstractHaki damageHaki = Mugiwara.getInstance().getHakiManager().getHaki(damager);
            if (damageHaki != null && damageHaki.getHakiType().equals(HakiType.ARMEMENT)) return;
            if (Math.random() < 0.15) {
                event.setCancelled(true);
            }
        }
    }

    public static final List<RolesType> LOGIA = Arrays.asList(RolesType.SMOKER, RolesType.KIZARU, RolesType.AKAINU, RolesType.SABO,
            RolesType.TEACH);
    public static final List<RolesType> PARAMECIA = Arrays.asList(RolesType.LUFFY, RolesType.ROBIN, RolesType.BROOK, RolesType.LAW,
            RolesType.EUSTASS, RolesType.FUJITORA, RolesType.TSURU, RolesType.HANCOCK, RolesType.KUMA, RolesType.KATAKURI,
            RolesType.TEACH /*TODO AJOUTER MAGELLAN, BRULEE, OVEN*/);
    public static final List<RolesType> ZOAN = Arrays.asList(RolesType.CHOPPER, RolesType.SENGOKU, RolesType.DRAKE, RolesType.KAIDO,
            RolesType.KING, RolesType.QUEEN, RolesType.JACK);
}

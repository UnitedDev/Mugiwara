package fr.kohei.mugiwara.game.events;

import fr.kohei.mugiwara.game.player.MUPlayer;
import fr.kohei.mugiwara.roles.RolesType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.List;

public class FruitDuDemonManager implements Listener {
    public FruitDuDemonManager(Plugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void init() {

    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if(!(event.getEntity() instanceof Player)) return;
        if(!(event.getDamager() instanceof Player)) return;

        Player player = (Player) event.getEntity();
        Player damager = (Player) event.getDamager();

        List<RolesType> logia = Arrays.asList(RolesType.SMOKER, RolesType.KIZARU, RolesType.AKAINU, RolesType.SABO, RolesType.TEACH);
        RolesType role = MUPlayer.get(player).getRole().getRole();

        if(logia.contains(role)) {
            if(Math.random() < 0.15) {
                event.setCancelled(true);
            }
        }
    }
}

package fr.uniteduhc.mugiwara.power.impl;

import fr.uniteduhc.mugiwara.Mugiwara;
import fr.uniteduhc.mugiwara.utils.config.Messages;
import fr.uniteduhc.mugiwara.power.RightClickPower;
import fr.uniteduhc.mugiwara.utils.utils.Utils;
import fr.uniteduhc.utils.ItemBuilder;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

@Getter
@Setter
public class ExterminPowerRight extends RightClickPower {
    private boolean enabled;
    private int timer;

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.BONE).setName(Utils.itemFormat("&d&lExtermin")).toItemStack();
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public Integer getCooldownAmount() {
        return null;
    }

    @Override
    public boolean onEnable(Player player, boolean rightClick) {
        final Location initialLocation = player.getLocation();
        final UUID uuid = player.getUniqueId();

        Messages.JACK_EXTERMIN_USE.send(player);

        new BukkitRunnable() {
            @Override
            public void run() {
                Player player = Bukkit.getPlayer(uuid);
                if (player == null) return;

                if (initialLocation.distance(player.getLocation()) > 1) {
                    enabled = false;
                    Messages.JACK_EXTERMIN_END.send(player);
                    cancel();
                    return;
                }

                if (timer >= 180) return;

                timer++;
                String display = "&cExtermin &8» &f" + timer + "/180";
                Mugiwara.getInstance().addActionBar(player, display, "exterminTimer");

                player.getWorld().spigot().playEffect(player.getEyeLocation().clone().add(0, 1, 0), Effect.LARGE_SMOKE, 0, 0, 0, 0, 0, 0, 1, 10);
            }
        }.runTaskTimer(Mugiwara.getInstance(), 0, 20);

        return true;
    }

}

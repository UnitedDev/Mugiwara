package fr.kohei.mugiwara.power.impl;

import fr.kohei.mugiwara.Mugiwara;
import fr.kohei.mugiwara.utils.config.Messages;
import fr.kohei.mugiwara.utils.config.Replacement;
import fr.kohei.mugiwara.power.RightClickPower;
import fr.kohei.mugiwara.utils.utils.Utils;
import fr.kohei.utils.ChatUtil;
import fr.kohei.utils.ItemBuilder;
import fr.kohei.utils.ReflectionUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class ClutchPower extends RightClickPower {
    private int uses;

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.getMaterial(38), 1, (byte) 1).setName(Utils.itemFormat("&b&lClutch")).toItemStack();
    }

    @Override
    public String getName() {
        return "Clutch";
    }

    @Override
    public Integer getCooldownAmount() {
        return 10;
    }

    @Override
    public boolean onEnable(Player player, boolean rightClick) {
        Player target = Utils.getPlayers().stream()
                .filter(p -> p.getLocation().distance(player.getLocation()) <= 60)
                .filter(p -> ReflectionUtils.getLookingAt(player, p))
                .findFirst().orElse(null);
        if (target == null) return false;

        if(uses >= 3) {
            player.sendMessage(ChatUtil.prefix("&cVous avez déjà utilisé ce pouvoir 3 fois."));
            return false;
        }

        uses++;
        final UUID uuid = target.getUniqueId();
        new BukkitRunnable() {
            int timer = 10;

            @Override
            public void run() {
                timer--;

                if (timer <= 0) cancel();

                Player target = Bukkit.getPlayer(uuid);
                if (target == null) return;

                Location targetLocation = target.getLocation();
                targetLocation.setYaw(targetLocation.getYaw() + 90);
                target.teleport(targetLocation);
            }
        }.runTaskTimer(Mugiwara.getInstance(), 0, 20);

        Messages.ROBIN_CLUTCH_USE.send(player, new Replacement("<name>", target.getName()));
        Messages.ROBIN_CLUTCH_ONME.send(target);
        this.uses++;
        return true;
    }
}

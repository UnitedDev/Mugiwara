package fr.kohei.mugiwara.power.impl;

import fr.kohei.mugiwara.Mugiwara;
import fr.kohei.mugiwara.utils.config.Messages;
import fr.kohei.mugiwara.power.RightClickPower;
import fr.kohei.mugiwara.utils.utils.Utils;
import fr.kohei.utils.ChatUtil;
import fr.kohei.utils.Heads;
import fr.kohei.utils.ItemBuilder;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.UUID;

@Getter
public class ZeusPower extends RightClickPower {
    private boolean usingPower;

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.WOOL).setName(Utils.itemFormat("Zeus")).toItemStack();
    }

    @Override
    public String getName() {
        return "Zeus";
    }

    @Override
    public Integer getCooldownAmount() {
        return Utils.getTimeBeforeEpisode();
    }

    @Override
    public boolean onEnable(Player player, boolean rightClick) {
        Messages.NAMI_ZEUS_USE.send(player);
        this.usingPower = true;

        final UUID uuid = player.getUniqueId();
        final ArmorStand armorStand = (ArmorStand) player.getWorld().spawnEntity(getLogicLocation(player), EntityType.ARMOR_STAND);
        armorStand.setGravity(false);
        armorStand.setBasePlate(false);
        armorStand.setArms(false);
        armorStand.setSmall(true);
        armorStand.setMarker(false);
        armorStand.setCustomName(ChatUtil.prefix("&6???"));
        armorStand.setHelmet(Heads.LUCKY.toItemStack());
        new BukkitRunnable() {
            private int timer = 120 * 20;
            @Override
            public void run() {
                timer--;
                if(timer <= 0) {
                    usingPower = false;
                    armorStand.remove();
                    cancel();
                    return;
                }

                Player player = Bukkit.getPlayer(uuid);
                if(player == null || armorStand.isDead()) return;

                armorStand.teleport(getLogicLocation(player));
                Messages.NAMI_ZEUS_FINISH.send(player);
            }
        }.runTaskTimer(Mugiwara.getInstance(), 1, 1);

        return true;
    }

    private Location getLogicLocation(Player player) {
        double yaw = player.getLocation().getYaw() + 90;
        if (yaw < -180) {
            yaw += 360D;
        }
        if (yaw >= 180) {
            yaw -= 360D;
        }
        double angle = (yaw + 90) * Math.PI / 180.00;
        double x = Math.cos(angle);
        double z = Math.sin(angle);

        return (player.getLocation().clone().add(new Vector(x, 1, z)));
    }
}

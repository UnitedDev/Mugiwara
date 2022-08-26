package fr.uniteduhc.mugiwara.power.impl;

import fr.uniteduhc.mugiwara.Mugiwara;
import fr.uniteduhc.mugiwara.utils.config.Messages;
import fr.uniteduhc.mugiwara.power.RightClickPower;
import fr.uniteduhc.mugiwara.utils.utils.Utils;
import fr.uniteduhc.mugiwara.utils.utils.packets.MathUtil;
import fr.uniteduhc.utils.Cuboid;
import fr.uniteduhc.utils.ItemBuilder;
import fr.uniteduhc.utils.ReflectionUtils;
import net.minecraft.server.v1_8_R3.EnumParticle;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DashPower extends RightClickPower {
    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.NETHER_STAR).setName(Utils.itemFormat("Dash")).toItemStack();
    }

    @Override
    public String getName() {
        return "Dash";
    }

    @Override
    public Integer getCooldownAmount() {
        return 5 * 60;
    }

    @Override
    public boolean onEnable(Player player, boolean rightClick) {
        Player target = Utils.getPlayers().stream()
                .filter(p -> p.getLocation().distance(player.getLocation()) <= 15)
                .filter(p -> ReflectionUtils.getLookingAt(player, p))
                .findFirst().orElse(null);
        if (target == null) return false;

        final UUID targetUUID = target.getUniqueId();
        final UUID playerUUID = player.getUniqueId();
        final ArmorStand armorStand = (ArmorStand) player.getWorld().spawnEntity(
                player.getLocation().clone().add(0, -1, 0),
                EntityType.ARMOR_STAND
        );
        armorStand.setVisible(false);
        List<UUID> damaged = new ArrayList<>();

        target.setHealth(target.getHealth() - 4);

        new BukkitRunnable() {
            @Override
            public void run() {
                Player player = Bukkit.getPlayer(playerUUID);
                Player target = Bukkit.getPlayer(targetUUID);

                if (armorStand.getPassenger() == null) armorStand.setPassenger(player);

                if(armorStand.isDead()) cancel();

                if (player == null || target == null) {
                    armorStand.remove();
                    cancel();
                    return;
                }

                if (player.getLocation().distance(target.getLocation()) <= 1) {
                    cancel();
                    armorStand.remove();
                    return;
                }

                final Vector vector = player.getLocation().toVector().subtract(target.getLocation().toVector()).normalize().multiply(-1);
                armorStand.setVelocity(vector);

                Location first = player.getLocation().clone().add(1, 1, 1);
                Location second = player.getLocation().clone().add(-1, -1, -1);
                Cuboid cuboid = new Cuboid(first, second);

                cuboid.getBlockList().stream()
                        .filter(block -> block.getType() != Material.REDSTONE_BLOCK)
                        .filter(block -> !block.getType().name().contains("CHEST"))
                        .filter(block -> block.getType() != Material.REDSTONE_BLOCK)
                        .forEach(block -> block.setType(Material.AIR));

                GearFourthPower.spawnParticle(player);

                Utils.getPlayers().forEach(player1 -> {
                    if(player1.getUniqueId().equals(playerUUID)) return;
                    if (player1.getLocation().distance(player.getLocation()) <= 4
                            && !damaged.contains(player1.getUniqueId())) {
                        player1.setHealth(player1.getHealth() - 3);
                        Messages.ZORO_DASH_ONWAY.send(player1);
                        damaged.add(player1.getUniqueId());
                    }
                });
            }
        }.runTaskTimer(Mugiwara.getInstance(), 0, 2);
        target.setHealth(target.getHealth() - 4);

        Messages.ZORO_DASH.send(player);
        return true;
    }

    public static void spawnParticle(Player player) {
        MathUtil.sendCircleParticle(EnumParticle.VILLAGER_HAPPY, player.getLocation(), 1, 20);
    }
}

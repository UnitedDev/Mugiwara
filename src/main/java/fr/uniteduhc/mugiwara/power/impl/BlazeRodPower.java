package fr.uniteduhc.mugiwara.power.impl;

import fr.uniteduhc.mugiwara.Mugiwara;
import fr.uniteduhc.mugiwara.game.player.MUPlayer;
import fr.uniteduhc.mugiwara.power.RightClickPower;
import fr.uniteduhc.mugiwara.roles.marine.SmokerRole;
import fr.uniteduhc.mugiwara.utils.config.Messages;
import fr.uniteduhc.mugiwara.utils.utils.Utils;
import fr.uniteduhc.mugiwara.utils.utils.packets.MathUtil;
import fr.uniteduhc.utils.Cuboid;
import fr.uniteduhc.utils.ItemBuilder;
import fr.uniteduhc.utils.ReflectionUtils;
import net.minecraft.server.v1_8_R3.EnumParticle;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.UUID;

public class BlazeRodPower extends RightClickPower {
    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.BLAZE_ROD).setName(Utils.itemFormat("&6&lBlaze Rod")).toItemStack();
    }

    @Override
    public String getName() {
        return "Blaze Rod";
    }

    @Override
    public Integer getCooldownAmount() {
        return 8 * 60;
    }

    @Override
    public boolean onEnable(Player player, boolean rightClick) {
        Player target = Utils.getPlayers().stream()
                .filter(p -> p.getLocation().distance(player.getLocation()) <= 25)
                .filter(p -> ReflectionUtils.getLookingAt(player, p))
                .findFirst().orElse(null);
        if (target == null) return false;

        if (MUPlayer.get(target).getRole() instanceof SmokerRole) {
            Messages.SABO_BLAZE_SMOKER.send(player);
            return false;
        }

        Messages.SABO_BLAZE_USE.send(player);
        Messages.SABO_BLAZE_TARGET.send(target);
        ArmorStand armorStand = player.getWorld().spawn(player.getLocation(), ArmorStand.class);
        armorStand.setVisible(false);
        armorStand.setPassenger(player);

        final UUID playerUUID = player.getUniqueId();
        final UUID targetUUID = target.getUniqueId();
        new BukkitRunnable() {
            int timer = 15 * 10;

            @Override
            public void run() {
                Player player = Bukkit.getPlayer(playerUUID);
                Player target = Bukkit.getPlayer(targetUUID);

                if (player == null || target == null) {
                    this.cancel();
                    return;
                }

                if (timer == 0) cancel();

                timer--;
                target.setFireTicks(10);

                if (player.getLocation().distance(target.getLocation()) <= 1) {
                    armorStand.remove();
                    return;
                }

                Cuboid targetCuboid = new Cuboid(target.getLocation().clone().add(1, 1, 1), target.getLocation().clone().add(-1, -1, -1));
                targetCuboid.getBlockList().stream().filter(block -> block.getType().name().contains("WATER"))
                        .forEach(block -> block.setType(Material.AIR));

                MathUtil.sendCircleParticle(EnumParticle.FLAME, target.getLocation(), 1, 5);

                Cuboid cuboid = new Cuboid(armorStand.getLocation().clone().add(1, 1, 1), armorStand.getLocation().clone().add(-1, -1, -1));
                cuboid.getBlockList().forEach(block -> {
                    if (block.getType() != Material.REDSTONE_BLOCK)
                        block.setType(Material.AIR);
                });
                final Vector vector = player.getLocation().toVector().subtract(target.getLocation().toVector()).normalize().multiply(-1);
                armorStand.setVelocity(vector);
            }
        }.runTaskTimer(Mugiwara.getInstance(), 0, 2);

        return true;
    }
}

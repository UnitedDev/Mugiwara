package fr.kohei.mugiwara.power.impl;

import fr.kohei.mugiwara.Mugiwara;
import fr.kohei.mugiwara.power.RightClickPower;
import fr.kohei.mugiwara.utils.config.Messages;
import fr.kohei.mugiwara.utils.utils.Utils;
import fr.kohei.mugiwara.utils.utils.WorldUtils;
import fr.kohei.mugiwara.utils.utils.packets.MathUtil;
import fr.kohei.utils.ChatUtil;
import fr.kohei.utils.ItemBuilder;
import net.minecraft.server.v1_8_R3.EnumParticle;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Instant;
import java.util.List;

public class MeteoritePower extends RightClickPower {
    private int clicks;
    private long lastClick;
    private boolean used;

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.NETHER_STAR).setName(Utils.itemFormat("&7&lMétéorite")).toItemStack();
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
        if(used) {
            player.sendMessage(ChatUtil.prefix("&cVous avez déjà utilisé ce pouvoir."));
            return false;
        }

        if (System.currentTimeMillis() - lastClick >= 5 * 1000) {
            lastClick = 0;
            clicks = 0;
        }

        lastClick = System.currentTimeMillis();
        clicks++;
        if(clicks < 5) {
            player.sendMessage(ChatUtil.prefix("&fCliquez encore &a" + (5 - clicks) + " &ffois pour activer cet item."));
            return false;
        }

        Messages.FUJITORA_METEORITE_USE.send(player);
        used = true;
        Location initialLocation = player.getLocation();
        new BukkitRunnable() {
            int seconds = 0;

            @Override
            public void run() {
                if (seconds == 4 * 2) {
                    MeteoresGenerator.spawnMeteore(initialLocation.clone().add(0, 100, 0), 3, 100);
                    cancel();
                }

                for (double i = 0; i <= 10; i += 1) {
                    MathUtil.sendCircleParticle(EnumParticle.FLAME, initialLocation.clone().add(0, i, 0), 40, 75);
                }
                seconds++;
            }
        }.runTaskTimer(Mugiwara.getInstance(), 0, 10);

        Bukkit.getScheduler().runTaskLater(Mugiwara.getInstance(), () -> {
        }, 4 * 20);

        return true;
    }

    public static class MeteoresGenerator {

        public static final String METEORE_KEY = "MeteoreFallingBlock";


        public static void spawnMeteore(Location loc, int radius, int distanceSound) {
            for (Player players : Bukkit.getOnlinePlayers()) {
                if (loc.distance(players.getLocation()) <= distanceSound) {
                    players.playSound(players.getLocation(), Sound.WITHER_SPAWN, 1, 1);
                }
            }

            Location center = loc.clone().add(0, 50, 0);

            List<Location> blockLocations = WorldUtils.generateSphere(center, radius, false);

            for (Location blockLocation : blockLocations) {
                FallingBlock fallingBlock = center.getWorld().spawnFallingBlock(blockLocation, Material.COBBLESTONE, (byte) 0);
                fallingBlock.setDropItem(false);
                fallingBlock.setHurtEntities(true);
                fallingBlock.setCustomName(METEORE_KEY);
            }
        }

    }
}

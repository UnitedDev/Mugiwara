package fr.kohei.mugiwara.utils.utils.packets;

import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SuppressWarnings("unused")
public class MathUtil {
    public static Set<Vector> circle;

    public static void sendParticle(final EnumParticle particle, final Location location) {
        sendParticle(particle, location.getX(), location.getY(), location.getZ());
    }

    public static void sendParticle(final EnumParticle particle, final double x, final double y, final double z) {
        final PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(particle, true, (float) x, (float) y, (float) z, 0.0f, 0.0f, 0.0f, 0.0f, 10, (int[]) null);
        for (final Player p : Bukkit.getOnlinePlayers()) {
            ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
        }
    }

    public static void sendParticle(final EnumParticle particle, final double x, final double y, final double z, Player... targets) {
        final PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(particle, true, (float) x, (float) y, (float) z, 0.0f, 0.0f, 0.0f, 0.0f, 10, (int[]) null);
        for (final Player p : targets) {
            ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
        }
    }

    public static void sendParticle(final List<Player> players, final EnumParticle particle, final double x, final double y, final double z) {
        final PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(particle, true, (float) x, (float) y, (float) z, 0.0f, 0.0f, 0.0f, 0.0f, 10, (int[]) null);
        for (final Player p : players) {
            ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
        }
    }

    public static void sendParticle(final List<Player> players, final EnumParticle particle, final double x, final double y, final double z, final int brightness, final int offsetX, final int offsetY, final int offsetZ) {
        final PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(particle, true, (float) x, (float) y, (float) z, 0.0f, (float) brightness, (float) offsetX, (float) offsetY, offsetZ, (int[]) null);
        for (final Player p : players) {
            ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
        }
    }

    public static void sendParticle(final World world, final EnumParticle particle, final double x, final double y, final double z) {
        final PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(particle, true, (float) x, (float) y, (float) z, 0.0f, 0.0f, 0.0f, 0.0f, 10, (int[]) null);
        for (final Player p : Bukkit.getOnlinePlayers()) {
            if (p.getWorld() == world) {
                ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
            }
        }
    }

    public static void sendCircleParticleWorld(final World world, final EnumParticle particle, final Location center, final double radius, final int amount) {
        final double increment = 6.283185307179586 / amount;
        for (int i = 0; i < amount; ++i) {
            final double angle = i * increment;
            final double x = center.getX() + radius * Math.cos(angle);
            final double z = center.getZ() + radius * Math.sin(angle);
            sendParticle(world, particle, x, center.getY() + 0.5, z);
        }
    }

    public static void sendCircleParticle(final EnumParticle particle, final Location center, final double radius, final int amount) {
        final double increment = 6.283185307179586 / amount;
        for (int i = 0; i < amount; ++i) {
            final double angle = i * increment;
            final double x = center.getX() + radius * Math.cos(angle);
            final double z = center.getZ() + radius * Math.sin(angle);
            sendParticle(particle, x, center.getY() + 0.5, z);
        }
    }

    public static void sendCircleParticle(final EnumParticle particle, final Location center, final double radius, final int amount, Player... targets) {
        final double increment = 6.283185307179586 / amount;
        for (int i = 0; i < amount; ++i) {
            final double angle = i * increment;
            final double x = center.getX() + radius * Math.cos(angle);
            final double z = center.getZ() + radius * Math.sin(angle);
            sendParticle(particle, x, center.getY() + 0.5, z, targets);
        }
    }

    public static Set<Vector> getCircle(Double radius, final Integer amount, final Boolean full, final Double space) {
        final Set<Vector> list = new HashSet<>();
        if (amount <= 0) {
            return list;
        }
        final double increment = 6.283185307179586 / amount;
        for (double angle = 0.0; angle <= 6.283185307179586; angle += increment) {
            final double x = radius * Math.cos(angle);
            final double z = radius * Math.sin(angle);
            list.add(new Vector((int) x, 0, (int) z));
        }
        if (full) {
            while (radius > 0.0) {
                radius -= space;
                for (double angle = 0.0; angle <= 6.283185307179586; angle += increment) {
                    final double x = radius * Math.cos(angle);
                    final double z = radius * Math.sin(angle);
                    list.add(new Vector((int) x, 0, (int) z));
                }
            }
        }
        return list;
    }

    public static void sendLineParticle(EnumParticle particle, Location from, Location to, double space) {
        double distance = from.distance(to);
        Vector vFrom = from.toVector();
        Vector vTo = to.toVector();
        Vector vector = vTo.clone().subtract(vFrom).normalize().multiply(space);
        double length = 0;
        for (; length < distance; vFrom.add(vector)) {
            sendParticle(particle, vFrom.getX(), vFrom.getY(), vFrom.getZ());
            length += space;
        }
    }

    public static void sendLineParticle(EnumParticle particle, Location from, Location to, double space, Player... players) {
        double distance = from.distance(to);
        Vector vFrom = from.toVector();
        Vector vTo = to.toVector();
        Vector vector = vTo.clone().subtract(vFrom).normalize().multiply(space);
        double length = 0;
        for (; length < distance; vFrom.add(vector)) {
            sendParticle(particle, vFrom.getX(), vFrom.getY(), vFrom.getZ(), players);
            length += space;
        }
    }

    private static Set<Vector> getSphereAbove(final Double radius, final Integer amount, final Boolean full, final Double space) {
        final Set<Vector> list = new HashSet<>();
        int y = 0;
        for (Double r = radius; r > 0.0; --r, ++y) {
            for (final Vector v : getCircle(r, amount, full, space)) {
                v.setY(v.getY() + y);
                list.add(v);
            }
        }
        return list;
    }

    public static List<Location> getSphere(final Location centerBlock, final int radius, final boolean hollow) {
        final List<Location> circleBlocks = new ArrayList<>();
        final int bX = centerBlock.getBlockX();
        final int bY = centerBlock.getBlockY();
        final int bZ = centerBlock.getBlockZ();
        for (int x = bX - radius; x <= bX + radius; ++x) {
            for (int y = bY - radius; y <= bY + radius; ++y) {
                for (int z = bZ - radius; z <= bZ + radius; ++z) {
                    final double distance = (bX - x) * (bX - x) + (bZ - z) * (bZ - z) + (bY - y) * (bY - y);
                    if (distance < radius * radius && (!hollow || distance >= (radius - 1) * (radius - 1))) {
                        circleBlocks.add(new Location(centerBlock.getWorld(), (double) x, (double) y, (double) z));
                    }
                }
            }
        }
        return circleBlocks;
    }

    public static List<Location> getCircle(final Location centerBlock, final int radius, final boolean hollow) {
        final List<Location> circleBlocks = new ArrayList<Location>();
        final int bX = centerBlock.getBlockX();
        final int bY = centerBlock.getBlockY();
        final int bZ = centerBlock.getBlockZ();
        for (int x = bX - radius; x <= bX + radius; ++x) {
            for (int z = bZ - radius; z <= bZ + radius; ++z) {
                final double distance = (bX - x) * (bX - x) + (bZ - z) * (bZ - z);
                if (distance < radius * radius && (!hollow || distance >= (radius - 1) * (radius - 1))) {
                    circleBlocks.add(new Location(centerBlock.getWorld(), (double) x, (double) bY, (double) z));
                }
            }
        }
        return circleBlocks;
    }

    public static String convertDouble(final double damage) {
        final NumberFormat nf = new DecimalFormat("##.##");
        return nf.format(damage);
    }

    public static double convertPercent(final double health) {
        final double hearts = health / 2.0;
        return hearts * 10.0;
    }

    public static boolean isInt(final String s) {
        try {
            final int i = Integer.parseInt(s);
            return true;
        } catch (NumberFormatException er) {
            return false;
        }
    }

    public static boolean isDouble(final String s) {
        try {
            final double i = Double.parseDouble(s);
            return true;
        } catch (NumberFormatException er) {
            return false;
        }
    }

    public static boolean isBoolean(final String s) {
        try {
            final boolean i = Boolean.parseBoolean(s);
            return true;
        } catch (NumberFormatException er) {
            return false;
        }
    }

    public static Vector getDirectionBetweenLocations(final Location loc1, final Location loc2) {
        final Vector from = loc1.toVector();
        final Vector to = loc2.toVector();
        return to.subtract(from);
    }

    public static boolean getLookingAt(final Player player, final Player target) {
        final Location eye = player.getEyeLocation();
        final Vector toEntity = target.getEyeLocation().toVector().subtract(eye.toVector());
        final double dot = toEntity.normalize().dot(eye.getDirection());
        return dot > 0.99;
    }

    public static boolean getLookingBody(final Player player, final Player target, final int radius) {
        final Set<Material> materials = new HashSet<Material>();
        materials.add(Material.AIR);
        materials.add(Material.WEB);
        final List<Block> blocs = player.getLineOfSight(materials, radius);
        return blocs != null && !blocs.isEmpty() && (blocs.contains(target.getLocation().getBlock()) || blocs.contains(target.getLocation().getBlock().getRelative(BlockFace.UP)));
    }

    static {
        MathUtil.circle = new HashSet<>(getSphereAbove(3.0, 5, true, 1.0));
    }
}
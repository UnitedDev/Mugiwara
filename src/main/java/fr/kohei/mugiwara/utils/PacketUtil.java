package fr.kohei.mugiwara.utils;

import com.google.common.collect.Iterables;
import fr.kohei.mugiwara.Mugiwara;
import lombok.SneakyThrows;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

@SuppressWarnings("unused")
public class PacketUtil {
    public static void sendRespawn(final Player p) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(Mugiwara.getInstance(), () -> ((CraftPlayer) p).getHandle().playerConnection
                .a(new PacketPlayInClientCommand(PacketPlayInClientCommand.EnumClientCommand.PERFORM_RESPAWN)), 10L);
    }

    @SneakyThrows
    public static void sendConnect(final Player p, final String server) {
        final ByteArrayOutputStream b = new ByteArrayOutputStream();
        final DataOutputStream out = new DataOutputStream(b);
        try {
            out.writeUTF("Connect");
            out.writeUTF(server);
        } catch (IOException e) {
            e.printStackTrace();
        }
        p.sendPluginMessage(Mugiwara.getInstance(), "BungeeCord", b.toByteArray());
    }

    public static void broadcastMessage(Player sender, final String message) {
        final ByteArrayOutputStream b = new ByteArrayOutputStream();
        final DataOutputStream out = new DataOutputStream(b);
        try {
            out.writeUTF("Message");
            out.writeUTF("ALL");
            out.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (sender == null) {
            sender = (Player) Iterables.getFirst(Bukkit.getOnlinePlayers(), (Object) null);
        }
        if (sender == null) return;
        sender.sendPluginMessage(Mugiwara.getInstance(), "BungeeCord", b.toByteArray());
    }

    private static void disableCoords(final Player p) {
        try {
            final EntityPlayer cp = ((CraftPlayer) p).getHandle();
            final PacketPlayOutEntityStatus packetC = new PacketPlayOutEntityStatus(cp, (byte) 22);
            cp.playerConnection.sendPacket(packetC);
        } catch (Exception e) {
            System.out.println("[ERROR] Disable Coords: " + e.getMessage());
        }
    }

    private static void enableCoords(final Player p) {
        try {
            final EntityPlayer cp = ((CraftPlayer) p).getHandle();
            final PacketPlayOutEntityStatus packetC = new PacketPlayOutEntityStatus(cp, (byte) 23);
            cp.playerConnection.sendPacket(packetC);
        } catch (Exception e) {
            System.out.println("[ERROR] Enable Coords: " + e.getMessage());
        }
    }

    public static void disableAI(final org.bukkit.entity.Entity entity) {
        final net.minecraft.server.v1_8_R3.Entity nmsEnt = ((CraftEntity) entity).getHandle();
        NBTTagCompound tag = nmsEnt.getNBTTag();
        if (tag == null) {
            tag = new NBTTagCompound();
        }
        nmsEnt.c(tag);
        tag.setInt("NoAI", 1);
        nmsEnt.f(tag);
    }

    public static void hideArmor(final Player player) {
        if (player == null || player.getInventory() == null) {
            return;
        }
        final int id = player.getEntityId();
        final PacketPlayOutEntityEquipment helmet = new PacketPlayOutEntityEquipment(id, 4, null);
        final PacketPlayOutEntityEquipment chestplate = new PacketPlayOutEntityEquipment(id, 3, null);
        final PacketPlayOutEntityEquipment leggings = new PacketPlayOutEntityEquipment(id, 2, null);
        final PacketPlayOutEntityEquipment boots = new PacketPlayOutEntityEquipment(id, 1, null);
        final PacketPlayOutEntityEquipment itemHand = new PacketPlayOutEntityEquipment(id, 0, null);
        for (final Player online : Bukkit.getOnlinePlayers()) {
            if (online == player) {
                continue;
            }
            try {
                ((CraftPlayer) online).getHandle().playerConnection.sendPacket(helmet);
                ((CraftPlayer) online).getHandle().playerConnection.sendPacket(chestplate);
                ((CraftPlayer) online).getHandle().playerConnection.sendPacket(leggings);
                ((CraftPlayer) online).getHandle().playerConnection.sendPacket(boots);
                ((CraftPlayer) online).getHandle().playerConnection.sendPacket(itemHand);
            } catch (Exception e) {
                System.out.println("(PacketUtil) Error with hideArmor(Player)");
                e.printStackTrace();
            }
        }
    }

    public static void showArmor(final Player player) {
        if (player == null || player.getInventory() == null) {
            return;
        }
        final int id = player.getEntityId();
        final PacketPlayOutEntityEquipment helmet = new PacketPlayOutEntityEquipment(id, 4, CraftItemStack.asNMSCopy(player.getInventory().getHelmet()));
        final PacketPlayOutEntityEquipment chestplate = new PacketPlayOutEntityEquipment(id, 3, CraftItemStack.asNMSCopy(player.getInventory().getChestplate()));
        final PacketPlayOutEntityEquipment leggings = new PacketPlayOutEntityEquipment(id, 2, CraftItemStack.asNMSCopy(player.getInventory().getLeggings()));
        final PacketPlayOutEntityEquipment boots = new PacketPlayOutEntityEquipment(id, 1, CraftItemStack.asNMSCopy(player.getInventory().getBoots()));
        final PacketPlayOutEntityEquipment itemHand = new PacketPlayOutEntityEquipment(id, 0, CraftItemStack.asNMSCopy(player.getItemInHand()));
        final UUID uuid = player.getUniqueId();
        for (final Player online : Bukkit.getOnlinePlayers()) {
            if (online.getUniqueId().equals(uuid)) {
                continue;
            }
            try {
                ((CraftPlayer) online).getHandle().playerConnection.sendPacket(helmet);
                ((CraftPlayer) online).getHandle().playerConnection.sendPacket(chestplate);
                ((CraftPlayer) online).getHandle().playerConnection.sendPacket(leggings);
                ((CraftPlayer) online).getHandle().playerConnection.sendPacket(boots);
                ((CraftPlayer) online).getHandle().playerConnection.sendPacket(itemHand);
            } catch (Exception e) {
                System.out.println("(PacketUtil) Error with showArmor(Player)");
                e.printStackTrace();
            }
        }
    }

    public static void showArmor(final Player player, final Player target) {
        if (player == null || player.getInventory() == null) {
            return;
        }
        final int id = player.getEntityId();
        final PacketPlayOutEntityEquipment helmet = new PacketPlayOutEntityEquipment(id, 4, CraftItemStack.asNMSCopy(player.getInventory().getHelmet()));
        final PacketPlayOutEntityEquipment chestplate = new PacketPlayOutEntityEquipment(id, 3, CraftItemStack.asNMSCopy(player.getInventory().getChestplate()));
        final PacketPlayOutEntityEquipment leggings = new PacketPlayOutEntityEquipment(id, 2, CraftItemStack.asNMSCopy(player.getInventory().getLeggings()));
        final PacketPlayOutEntityEquipment boots = new PacketPlayOutEntityEquipment(id, 1, CraftItemStack.asNMSCopy(player.getInventory().getBoots()));
        final PacketPlayOutEntityEquipment itemHand = new PacketPlayOutEntityEquipment(id, 0, CraftItemStack.asNMSCopy(player.getItemInHand()));
        try {
            ((CraftPlayer) target).getHandle().playerConnection.sendPacket(helmet);
            ((CraftPlayer) target).getHandle().playerConnection.sendPacket(chestplate);
            ((CraftPlayer) target).getHandle().playerConnection.sendPacket(leggings);
            ((CraftPlayer) target).getHandle().playerConnection.sendPacket(boots);
            ((CraftPlayer) target).getHandle().playerConnection.sendPacket(itemHand);
        } catch (Exception e) {
            System.out.println("(PacketUtil) Error with showArmor(Player)");
            e.printStackTrace();
        }
    }

    public static void hideItemHand(final Player player) {
        if (player == null || player.getInventory() == null) {
            return;
        }
        final int id = player.getEntityId();
        final PacketPlayOutEntityEquipment itemHand = new PacketPlayOutEntityEquipment(id, 0, null);
        for (final Player online : Bukkit.getOnlinePlayers()) {
            if (online == player) {
                continue;
            }
            try {
                ((CraftPlayer) online).getHandle().playerConnection.sendPacket(itemHand);
            } catch (Exception e) {
                System.out.println("(PacketUtil) Error with hideItemHand(Player)");
                e.printStackTrace();
            }
        }
    }

    public static void showItemHand(final Player player) {
        if (player == null || player.getInventory() == null) {
            return;
        }
        final int id = player.getEntityId();
        final PacketPlayOutEntityEquipment itemHand = new PacketPlayOutEntityEquipment(id, 0, CraftItemStack.asNMSCopy(player.getItemInHand()));
        final UUID uuid = player.getUniqueId();
        for (final Player online : Bukkit.getOnlinePlayers()) {
            if (online.getUniqueId().equals(uuid)) {
                continue;
            }
            try {
                ((CraftPlayer) online).getHandle().playerConnection.sendPacket(itemHand);
            } catch (Exception e) {
                System.out.println("(PacketUtil) Error with showItemHand(Player)");
                e.printStackTrace();
            }
        }
    }
}

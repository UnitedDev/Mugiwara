package fr.uniteduhc.mugiwara.utils.other;

import com.mojang.authlib.GameProfile;
import fr.uniteduhc.mugiwara.Mugiwara;
import fr.uniteduhc.utils.ChatUtil;
import lombok.SneakyThrows;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import net.minecraft.server.v1_8_R3.WorldSettings;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftChatMessage;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Field;
import java.util.List;
import java.util.UUID;

public class ScoreboardLife {

    @SneakyThrows
    public static void setCustomHealth(final Player player) {

        new BukkitRunnable() {
            final UUID uuid = player.getUniqueId();

            @SneakyThrows
            @Override
            public void run() {
                Player player = Bukkit.getPlayer(uuid);

                if (player == null) return;

                PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;

                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {

                    GameProfile gp = new GameProfile(onlinePlayer.getUniqueId(), onlinePlayer.getName());
                    PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.UPDATE_DISPLAY_NAME);
                    PacketPlayOutPlayerInfo.PlayerInfoData data = packet.new PlayerInfoData(
                            gp,
                            0,
                            WorldSettings.EnumGamemode.SURVIVAL,
                            CraftChatMessage.fromString(onlinePlayer.getName() + display(onlinePlayer))[0]
                    );

                    Field field = PacketPlayOutPlayerInfo.class.getDeclaredField("b");
                    field.setAccessible(true);
                    List<PacketPlayOutPlayerInfo.PlayerInfoData> datas = (List<PacketPlayOutPlayerInfo.PlayerInfoData>) field.get(packet);
                    datas.add(data);

                    connection.sendPacket(packet);
                }
            }
        }.runTaskTimer(Mugiwara.getInstance(), 0, 20);
    }

    private static String display(Player player) {
        if (player.getHealth() >= 8) return ChatUtil.translate("  &4❤❤❤❤");
        if (player.getHealth() >= 7) return ChatUtil.translate("  &4❤❤❤&c❤");
        if (player.getHealth() >= 6) return ChatUtil.translate("  &4❤❤❤&7❤");
        if (player.getHealth() >= 5) return ChatUtil.translate("  &4❤❤&c❤&7❤");
        if (player.getHealth() >= 4) return ChatUtil.translate("  &4❤❤&7❤❤");
        if (player.getHealth() >= 3) return ChatUtil.translate("  &4❤&c❤&7❤❤");
        if (player.getHealth() >= 2) return ChatUtil.translate("  &4❤&7❤&7❤❤");
        if (player.getHealth() >= 1) return ChatUtil.translate("  &c❤&7❤❤❤");
        else return ChatUtil.translate("  &7❤❤❤❤");
    }
}
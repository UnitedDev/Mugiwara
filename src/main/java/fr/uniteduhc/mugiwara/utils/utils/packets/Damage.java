package fr.uniteduhc.mugiwara.utils.utils.packets;

import fr.uniteduhc.mugiwara.Mugiwara;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Damage {

    public static final HashMap<UUID, EntityDamageEvent.DamageCause> NO_DAMAGE_CAUSE = new HashMap<>();
    public static final List<UUID> NO_DAMAGE = new ArrayList<>();
    public static final List<UUID> CANNOT_DAMAGE = new ArrayList<>();

    public static void addCantDamageTemp(Player player, int seconds) {
        CANNOT_DAMAGE.add(player.getUniqueId());
        Bukkit.getScheduler().runTaskLater(Mugiwara.getInstance(), () -> CANNOT_DAMAGE.remove(player.getUniqueId()), seconds * 20L);
    }

    public static void addTempNoDamage(UUID var1, EntityDamageEvent.DamageCause var2, int var3) {
        NO_DAMAGE_CAUSE.put(var1, var2);
        Bukkit.getScheduler().runTaskLater(Mugiwara.getInstance(), () -> NO_DAMAGE_CAUSE.remove(var1), var3 * 20L);
    }

    public static void addNoDamage(UUID var1, EntityDamageEvent.DamageCause var2) {
        NO_DAMAGE_CAUSE.put(var1, var2);
    }

    public static void addTempNoDamage(Player var1, EntityDamageEvent.DamageCause var2, int var3) {
        addTempNoDamage(var1.getUniqueId(), var2, var3);
    }

    public static void addNoDamage(Player player, int seconds) {
        NO_DAMAGE.add(player.getUniqueId());
        Bukkit.getScheduler().runTaskLater(Mugiwara.getInstance(), () -> NO_DAMAGE.remove(player.getUniqueId()), seconds * 20L);
    }

}
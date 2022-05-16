package fr.kohei.mugiwara.utils;

import fr.kohei.mugiwara.Mugiwara;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.HashMap;
import java.util.UUID;

public class Damage {

    public static final HashMap<UUID, EntityDamageEvent.DamageCause> noDamage = new HashMap<>();

    public static void addTempNoDamage(UUID var1, EntityDamageEvent.DamageCause var2, int var3) {
        noDamage.put(var1, var2);
        Bukkit.getScheduler().runTaskLater(Mugiwara.getInstance(), () -> noDamage.remove(var1), var3 * 20L);
    }

    public static void addTempNoDamage(Player var1, EntityDamageEvent.DamageCause var2, int var3) {
        addTempNoDamage(var1.getUniqueId(), var2, var3);
    }

}
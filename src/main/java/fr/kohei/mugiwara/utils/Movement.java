package fr.kohei.mugiwara.utils;

import fr.kohei.mugiwara.Mugiwara;
import fr.kohei.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

public class Movement {

    public static void freeze(Player player, int seconds) {
        float walkSpeed = player.getWalkSpeed();
        PotionEffect jumpBoost = player.getActivePotionEffects()
                .stream()
                .filter(potionEffect -> potionEffect.getType() == PotionEffectType.JUMP)
                .findFirst()
                .orElse(null);

        player.setWalkSpeed(0F);
        player.sendMessage(ChatUtil.prefix("&fVous avez été &cimmobilisé &fpendant &c" + seconds + " &fsecondes."));
        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 10, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 200, false, false));

        final UUID uuid = player.getUniqueId();
        Bukkit.getScheduler().runTaskLater(Mugiwara.getInstance(), () -> {
            Player newPlayer = Bukkit.getPlayer(uuid);
            newPlayer.setWalkSpeed(walkSpeed);
            newPlayer.removePotionEffect(PotionEffectType.BLINDNESS);
            newPlayer.removePotionEffect(PotionEffectType.JUMP);
            if (jumpBoost != null) newPlayer.addPotionEffect(jumpBoost);
        }, seconds * 20L);
    }

}

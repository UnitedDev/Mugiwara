package fr.kohei.mugiwara.power.impl;

import fr.kohei.mugiwara.Mugiwara;
import fr.kohei.mugiwara.config.Messages;
import fr.kohei.mugiwara.power.RightClickPower;
import fr.kohei.mugiwara.utils.Utils;
import fr.kohei.utils.ChatUtil;
import fr.kohei.utils.ItemBuilder;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

@Setter
@Getter
public class DiableJambePower extends RightClickPower {
    private int uses = 0;
    private boolean using = false;

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.FEATHER).setName(Utils.itemFormat("&a&lDiable Jump"))
                .addEnchant(Enchantment.DAMAGE_ALL, 1).hideItemFlags().toItemStack();
    }

    @Override
    public String getName() {
        return "Diable Jambe";
    }

    @Override
    public Integer getCooldownAmount() {
        return 10 * 60;
    }

    @Override
    public boolean onEnable(Player player, boolean rightClick) {
        if(uses >= 3) {
            player.sendMessage(ChatUtil.prefix("&cVous avez déjà utilisé ce pouvoir 3 fois."));
            return false;
        }

        Messages.SANJI_DIABLEJAMBE_USE.send(player);
        this.setUsing(true);
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 2 * 20 * 60, 1, false, false));
        uses++;

        final UUID uuid = player.getUniqueId();
        new BukkitRunnable() {
            private int i = 120 * 2;
            @Override
            public void run() {
                i--;
                Player player = Bukkit.getPlayer(uuid);
                if (player == null) return;

                if (i <= 0) {
                    player.removePotionEffect(PotionEffectType.SPEED);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false));
                    Messages.SANJI_DIABLEJAMBE_END.send(player);
                    setUsing(false);
                    cancel();
                    return;
                }

                Location location = player.getLocation();
                for (int degree = 0; degree < 360; degree++) {
                    double radians = Math.toRadians(degree);
                    double x = Math.cos(radians);
                    double z = Math.sin(radians);
                    location.add(x, 0, z);
                    location.getWorld().playEffect(location, Effect.FLAME, 1);
                    location.subtract(x, 0, z);
                }
            }
        }.runTaskTimer(Mugiwara.getInstance(), 0, 10);
        return true;
    }
}

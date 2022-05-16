package fr.kohei.mugiwara.power.impl;

import fr.kohei.mugiwara.Mugiwara;
import fr.kohei.mugiwara.config.Messages;
import fr.kohei.mugiwara.power.RightClickPower;
import fr.kohei.mugiwara.utils.Damage;
import fr.kohei.mugiwara.utils.Utils;
import fr.kohei.utils.ItemBuilder;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

@Setter
@Getter
public class GearFourthPower extends RightClickPower {

    private boolean using = false;
    private int uses = 0;

    @Override
    public String getName() {
        return "Gear Fourth";
    }

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.ROTTEN_FLESH).addEnchant(Enchantment.DAMAGE_ALL, 1).hideItemFlags()
                .setName(Utils.itemFormat("&c&lGear Fourth")).toItemStack();
    }

    @Override
    public Integer getCooldownAmount() {
        if (uses == 1) return 10 * 60 + 90;
        if (uses == 2) return 10 * 60 + 120;
        if (uses == 3) return 10 * 60 + 120;
        else return 10 * 60;
    }

    @Override
    public void onEnable(Player player, boolean rightClick) {
        uses++;

        switch (uses) {
            case 1:
                this.useFirstPower(player);
                break;
            case 2:
                this.useSecondPower(player);
                break;
            case 3:
                this.useThirdPower(player);
                break;
            default:
                break;
        }
    }

    private void useFirstPower(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 90 * 20, 0, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 90 * 20, 2, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 90 * 20, 1, false, false));
        this.usePower(player, 90);

        Messages.LUFFY_FIRSTPOWER.send(player);

        checkStrength(player);
    }

    private void useSecondPower(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 120 * 20, 0, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 120 * 20, 1, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 120 * 20, 1, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 5 * 60 * 20, 0, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 5 * 60 * 20, 0, false, false));
        this.usePower(player, 120);

        Messages.LUFFY_SECONDPOWER.send(player);

        checkStrength(player);
    }

    private void useThirdPower(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 120 * 20, 0, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 120 * 20, 1, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 120 * 20, 1, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 5 * 60 * 20, 1, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 5 * 60 * 20, 0, false, false));
        this.usePower(player, 120);

        Messages.LUFFY_THIRDPOWER.send(player);

        checkStrength(player);
    }

    private void checkStrength(Player player) {
        final UUID uuid = player.getUniqueId();

        Bukkit.getScheduler().runTaskLater(Mugiwara.getInstance(), () -> {
            Player playerUpdated = Bukkit.getPlayer(uuid);
            if (playerUpdated == null) return;

            playerUpdated.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));
        }, 5 * 20 * 60);
    }

    private void usePower(Player player, int seconds) {
        this.setUsing(true);
        this.spawnParticles(player, seconds);
        Damage.addTempNoDamage(player, EntityDamageEvent.DamageCause.FALL, seconds);
        player.setAllowFlight(true);

        Bukkit.getScheduler().runTaskLater(Mugiwara.getInstance(), () -> setUsing(false), seconds * 20L);
    }

    private void spawnParticles(Player player, int seconds) {
        seconds *= 2;
        int finalSeconds = seconds;

        final UUID uuid = player.getUniqueId();
        new BukkitRunnable() {
            int duration = finalSeconds;

            @Override
            public void run() {
                if (duration == 0) cancel();
                duration--;
                Player player = Bukkit.getPlayer(uuid);
                if (player == null) {
                    cancel();
                    return;
                }

                Location location = player.getLocation();

                for (int degree = 0; degree < 360; degree++) {
                    double radians = Math.toRadians(degree);
                    double x = Math.cos(radians);
                    double z = Math.sin(radians);
                    location.add(x, 0, z);
                    location.getWorld().playEffect(location, Effect.PARTICLE_SMOKE, 1);
                    location.subtract(x, 0, z);
                }
            }
        }.runTaskTimer(Mugiwara.getInstance(), 0, 10);
    }
}

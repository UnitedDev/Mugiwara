package fr.uniteduhc.mugiwara.power.impl;

import fr.uniteduhc.mugiwara.Mugiwara;
import fr.uniteduhc.mugiwara.power.RightClickPower;
import fr.uniteduhc.mugiwara.utils.config.Messages;
import fr.uniteduhc.mugiwara.utils.utils.Cooldown;
import fr.uniteduhc.mugiwara.utils.utils.Utils;
import fr.uniteduhc.utils.ChatUtil;
import fr.uniteduhc.utils.Heads;
import fr.uniteduhc.utils.ItemBuilder;
import fr.uniteduhc.utils.ReflectionUtils;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.UUID;

@Getter
public class PrometheePower extends RightClickPower {
    private boolean using;
    private final Cooldown pro = new Cooldown("Promethée");
    private final Cooldown click = new Cooldown("Promethée 2");

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.WOOL).setDurability(14).setName(Utils.itemFormat("Prométhée")).toItemStack();
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
        if (using) {
            if(click.isCooldown(player)) return false;
            Player target = Utils.getPlayers().stream()
                    .filter(p -> p.getLocation().distance(player.getLocation()) <= 60)
                    .filter(p -> ReflectionUtils.getLookingAt(player, p))
                    .findFirst().orElse(null);
            if (target == null) return false;

            click.setCooldown(8);
            click.task();

            target.setFireTicks(40);
            return false;
        }

        if(pro.isCooldown(player)) return false;

        Messages.BIGMOM_PROMETHEE_USE.send(player);
        pro.setCooldown(Utils.getTimeBeforeEpisode());
        pro.task();

        player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 45 * 20, 0, false, false));
        using = true;

        final UUID uuid = player.getUniqueId();
        final ArmorStand armorStand = (ArmorStand) player.getWorld().spawnEntity(getLogicLocation(player), EntityType.ARMOR_STAND);
        armorStand.setGravity(false);
        armorStand.setBasePlate(false);
        armorStand.setArms(false);
        armorStand.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0, false, false));
        armorStand.setSmall(true);
        armorStand.setMarker(false);
        armorStand.setCustomName(ChatUtil.prefix("&6???"));
        armorStand.setHelmet(Heads.LUCKY.toItemStack());
        new BukkitRunnable() {
            private int timer = 45 * 20;

            @Override
            public void run() {
                timer--;
                if (timer <= 0) {
                    using = false;
                    armorStand.remove();
                    Messages.NAMI_ZEUS_FINISH.send(player);
                    cancel();
                    return;
                }

                Player player = Bukkit.getPlayer(uuid);
                if (player == null || armorStand.isDead()) return;

                armorStand.teleport(getLogicLocation(player));
            }
        }.runTaskTimer(Mugiwara.getInstance(), 1, 1);

        return true;
    }

    private Location getLogicLocation(Player player) {
        double yaw = player.getLocation().getYaw() + 90;
        if (yaw < -180) {
            yaw += 360D;
        }
        if (yaw >= 180) {
            yaw -= 360D;
        }
        double angle = (yaw + 90) * Math.PI / 180.00;
        double x = Math.cos(angle);
        double z = Math.sin(angle);

        return (player.getLocation().clone().add(new Vector(x, 1, z)));
    }
}

package fr.kohei.mugiwara.roles.impl.marine;

import fr.kohei.mugiwara.Mugiwara;
import fr.kohei.mugiwara.roles.RolesType;
import fr.kohei.mugiwara.utils.config.Messages;
import fr.kohei.mugiwara.utils.utils.Cooldown;
import fr.kohei.mugiwara.utils.utils.packets.MathUtil;
import fr.kohei.utils.Cuboid;
import fr.kohei.utils.ItemBuilder;
import net.minecraft.server.v1_8_R3.EnumParticle;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class MihawkRole extends RolesType.MURole implements Listener {
    private final Cooldown swordCooldown = new Cooldown("Epee");

    public MihawkRole() {
        super(Arrays.asList());
    }

    @Override
    public RolesType getRole() {
        return RolesType.MIHAWK;
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(Material.DIAMOND_SWORD);
    }

    @Override
    public void onDistribute(Player player) {
        player.getInventory().addItem(new ItemBuilder(Material.DIAMOND_SWORD).setName("&5&lYoru")
                .addEnchant(Enchantment.DAMAGE_ALL, 4).toItemStack());
    }

    @Override
    public String[] getDescription() {
        return new String[0];
    }

    @Override
    public double getStrengthBuffer() {
        return 1.1F;
    }

    @Override
    public void onNight(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));
    }

    @Override
    public void onDay(Player player) {
        player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getItemInHand() == null || !player.getItemInHand().getType().name().contains("SWORD")) return;
        if (!isRole(player)) return;
        if (swordCooldown.isCooldown(player)) return;
        if (event.getAction().name().contains("LEFT")) return;

        final Location initialLocation = player.getLocation();
        final UUID uuid = player.getUniqueId();
        new BukkitRunnable() {
            int timer = 3 * 2;

            @Override
            public void run() {
                Player player = Bukkit.getPlayer(uuid);

                if (player.getItemInHand() == null || !player.getItemInHand().getType().name().contains("SWORD")) {
                    cancel();
                    return;
                }

                if (!player.isBlocking()) {
                    cancel();
                    return;
                }

                if (initialLocation.distance(player.getLocation()) > 1) {
                    cancel();
                    return;
                }

                timer--;

                if (timer == 0) {
                    useSwordPower(player);
                    cancel();
                }
            }
        }.runTaskTimer(Mugiwara.getInstance(), 0, 10L);
    }

    private void useSwordPower(Player player) {
        if (swordCooldown.isCooldownNoMessage(player)) return;
        ArmorStand armorStand = (ArmorStand) player.getWorld().spawnEntity(player.getLocation(), EntityType.ARMOR_STAND);

        armorStand.setVisible(false);

        swordCooldown.setCooldown(5 * 60);
        swordCooldown.task();

        Messages.MIHAWK_SWORD_USE.send(player);
        final List<UUID> damaged = new ArrayList<>();
        final Location initialLocation = player.getLocation();
        Vector direction = player.getLocation().getDirection();
        new BukkitRunnable() {
            int timer = 7 * 10;

            @Override
            public void run() {
                if (armorStand.isDead()) {
                    cancel();
                    return;
                }

                if (timer == 0) {
                    armorStand.remove();
                    cancel();
                    return;
                }

                for (double i = 1; i <= 3; i += 0.1)
                    MathUtil.sendCircleParticle(EnumParticle.CLOUD, armorStand.getLocation(), i, 10);

                armorStand.getNearbyEntities(5, 5, 5).forEach(entity -> {
                    if (!(entity instanceof Player)) return;
                    Player player = (Player) entity;

                    if (damaged.contains(player.getUniqueId())) return;

                    double distance = initialLocation.distance(entity.getLocation());
                    if (distance <= 10) player.setHealth(player.getHealth() - 3);
                    else if (distance <= 20) player.setHealth(player.getHealth() - 5);
                    else if (distance <= 30) player.setHealth(player.getHealth() - 7);
                    else return;

                    Messages.MIHAWK_SWORD_TARGET.send(player);
                    damaged.add(player.getUniqueId());
                });

                Cuboid cuboid = new Cuboid(
                        armorStand.getLocation().clone().add(2, 2, 2),
                        armorStand.getLocation().clone().add(-2, -2, -2)
                );
                cuboid.getBlockList().forEach(block -> block.setType(Material.AIR));

                armorStand.setVelocity(direction.clone().multiply(0.7));
                timer--;
            }
        }.runTaskTimer(Mugiwara.getInstance(), 0, 2);
    }
}

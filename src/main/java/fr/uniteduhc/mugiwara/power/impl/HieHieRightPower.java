package fr.uniteduhc.mugiwara.power.impl;

import fr.uniteduhc.mugiwara.Mugiwara;
import fr.uniteduhc.mugiwara.game.player.MUPlayer;
import fr.uniteduhc.mugiwara.power.RightClickPower;
import fr.uniteduhc.mugiwara.roles.RolesType;
import fr.uniteduhc.mugiwara.roles.solo.KuzanRole;
import fr.uniteduhc.mugiwara.utils.utils.Utils;
import fr.uniteduhc.mugiwara.utils.utils.packets.MathUtil;
import fr.uniteduhc.uhc.game.player.UPlayer;
import fr.uniteduhc.utils.ChatUtil;
import fr.uniteduhc.utils.ItemBuilder;
import net.minecraft.server.v1_8_R3.EnumParticle;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.List;

public class HieHieRightPower extends RightClickPower {
    public boolean isGive() {
        return false;
    }

    public ItemStack getItem() {
        return new ItemBuilder(Material.PACKED_ICE)
                .setName(Utils.itemFormat("&b&lHie Hie"))
                .toItemStack();
    }

    public String getName() {
        return null;
    }

    public Integer getCooldownAmount() {
        return null;
    }

    public boolean onEnable(Player player, boolean rightClick) {
        if (!rightClick)
            return false;
        MUPlayer muPlayer = MUPlayer.get(player);
        RolesType.MURole role = muPlayer.getRole();
        KuzanRole kuzanRole = (KuzanRole) role;
        KuzanRole.HieHiePowerType hieHiePowerType = kuzanRole.getHieHiePowerType();
        if (hieHiePowerType == KuzanRole.HieHiePowerType.ICE_AGE) {
            if (kuzanRole.getEndurence() < 35) {
                player.sendMessage(ChatUtil.prefix("&cVous ne posspas assez d'Endurence."));
                return false;
            }
            kuzanRole.setEndurence(kuzanRole.getEndurence() - 35);
            iceAge(player);
        } else if (hieHiePowerType == KuzanRole.HieHiePowerType.PHEASANT_PEAK) {
            if (kuzanRole.getEndurence() < 35) {
                player.sendMessage(ChatUtil.prefix("&cVous ne posspas assez d'Endurence."));
                return false;
            }
            kuzanRole.setEndurence(kuzanRole.getEndurence() - 35);
            pheasantPeak(player);
        } else if (hieHiePowerType == KuzanRole.HieHiePowerType.ICE_BALL) {
            iceBall(player, null);
        }
        return true;
    }

    public void iceAge(Player player) {
        List<Location> sphere = MathUtil.getSphere(player.getLocation(), 30, false);
        Bukkit.getOnlinePlayers().stream()
                .filter(players -> (players != player))
                .filter(players -> UPlayer.get(players).isAlive())
                .filter(players -> (MUPlayer.get(players).getRole() != null))
                .filter(players -> (players.getLocation().distance(player.getLocation()) <= 30.0D))
                .forEach(players -> {
                    players.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 300, 0, false, false));
                    players.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 300, 0, false, false));
                    players.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 300, 0, false, false));
                    players.getLocation().add(0.0D, -1.0D, 0.0D).getBlock().setType(Material.PACKED_ICE);
                    for (int i = -1; i < 2; i++) {
                        if (i != 0) {
                            players.getLocation().add(0.0D, 0.0D, i).getBlock().setType(Material.PACKED_ICE);
                            players.getLocation().add(i, 0.0D, 0.0D).getBlock().setType(Material.PACKED_ICE);
                            players.getLocation().add(0.0D, 1.0D, i).getBlock().setType(Material.PACKED_ICE);
                            players.getLocation().add(i, 1.0D, 0.0D).getBlock().setType(Material.PACKED_ICE);
                        }
                    }
                    players.getLocation().add(0.0D, 2.0D, 0.0D).getBlock().setType(Material.PACKED_ICE);
                });
        sphere.stream()
                .filter(location -> (location.getBlock().getType() != Material.AIR))
                .forEach(location -> location.getBlock().setType(Material.PACKED_ICE));
    }

    public void pheasantPeak(final Player player) {
        final Entity entity = player.getWorld().spawnEntity(player.getLocation(), EntityType.BAT);
        entity.setPassenger(player);
        (new BukkitRunnable() {
            int time = 0;

            Vector vector = player.getLocation().getDirection();

            public void run() {
                this.time++;
                if (this.time > 11) {
                    cancel();
                    entity.remove();
                    return;
                }

                if (entity.getPassenger() == null) {
                    entity.setPassenger(player);
                }
                entity.setVelocity(this.vector.multiply(1.0D).setY(0.5D));
                MathUtil.sendCircleParticle(EnumParticle.WATER_SPLASH, entity.getLocation(), 1.0D, 15);

                Bukkit.getOnlinePlayers().stream()
                        .filter(players -> (players != player))
                        .filter(players -> UPlayer.get(players).isAlive())
                        .filter(players -> (MUPlayer.get(players).getRole() != null))
                        .filter(players -> (players.getLocation().distance(player.getLocation()) <= 3.0D))
                        .forEach(players -> {
                            players.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 300, 0, false, false));
                            players.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 300, 0, false, false));
                            players.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 300, 0, false, false));
                            players.getLocation().add(0.0D, -1.0D, 0.0D).getBlock().setType(Material.PACKED_ICE);
                            for (int i = -1; i < 2; i++) {
                                if (i != 0) {
                                    players.getLocation().add(0.0D, 0.0D, i).getBlock().setType(Material.PACKED_ICE);
                                    players.getLocation().add(i, 0.0D, 0.0D).getBlock().setType(Material.PACKED_ICE);
                                    players.getLocation().add(0.0D, 1.0D, i).getBlock().setType(Material.PACKED_ICE);
                                    players.getLocation().add(i, 1.0D, 0.0D).getBlock().setType(Material.PACKED_ICE);
                                }
                            }
                            players.getLocation().add(0.0D, 2.0D, 0.0D).getBlock().setType(Material.PACKED_ICE);
                        });
            }
        }).runTaskTimer(Mugiwara.getInstance(), 0L, 5L);
    }

    public void iceBall(Player player, Player target) {

        Location centerLocation = player.getLocation().clone();
        centerLocation.setY((player.getWorld().getHighestBlockYAt(player.getLocation()) - 1));
        List<Location> sphere = MathUtil.getSphere(centerLocation, 15, true);
        MathUtil.getSphere(centerLocation, 15, false).stream()
                .filter(location -> (location.getBlockY() == centerLocation.getBlockY()))
                .forEach(sphere::add);
        sphere.stream()
                .filter(location -> (location.getBlockY() > centerLocation.getBlockY() - 1))
                .forEach(location -> {
                    Block block = location.getBlock();
                    block.setType(Material.PACKED_ICE);
                    block.setMetadata("invincibiliy", (MetadataValue) new FixedMetadataValue(Mugiwara.getInstance(), "invincibiliy"));
                });
        Bukkit.getScheduler().runTaskLater(Mugiwara.getInstance(), () -> sphere.stream().filter(location -> location.getBlock().getType() == Material.PACKED_ICE).forEach(location -> location.getBlock().setType(Material.AIR)), 200L);
    }
}


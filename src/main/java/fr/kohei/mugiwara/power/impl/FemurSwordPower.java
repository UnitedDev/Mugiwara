package fr.kohei.mugiwara.power.impl;

import fr.kohei.mugiwara.Mugiwara;
import fr.kohei.mugiwara.game.player.MUPlayer;
import fr.kohei.mugiwara.power.DamagePlayerPower;
import fr.kohei.mugiwara.roles.impl.mugiwara.LuffyRole;
import fr.kohei.mugiwara.roles.impl.mugiwara.SanjiRole;
import fr.kohei.mugiwara.utils.config.Messages;
import fr.kohei.mugiwara.utils.config.Replacement;
import fr.kohei.mugiwara.utils.utils.Utils;
import fr.kohei.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class FemurSwordPower extends DamagePlayerPower {
    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.DIAMOND_SWORD)
                .setName(Utils.itemFormat("&d&lFemur")).addEnchant(Enchantment.DAMAGE_ALL, 3).toItemStack();
    }

    @Override
    public boolean onEnable(Player player, Player target, EntityDamageByEntityEvent damage) {
        final double attackY = player.getEyeLocation().getY() + player.getEyeLocation().getDirection().getY();

        femur(player, target, attackY);
        return true;
    }

    public static void femur(Player player, Player target, double attackY) {
        final double diff = attackY - target.getLocation().getY();

        if (MUPlayer.get(target).getRole() instanceof LuffyRole) return;

        if (diff < 0.6) {
            target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 15 * 20, 1, false, false));
            Messages.HANCOCK_FEMUR_TETE.send(player, new Replacement("<name>", target.getName()));
        } else if (diff < 1.4) {
            target.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 15 * 20, 1, false, false));
            Messages.HANCOCK_FEMUR_TORSE.send(player, new Replacement("<name>", target.getName()));
        } else {
            target.setHealth(target.getHealth() - 4);
            target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 15 * 20, 1, false, false));
            Messages.HANCOCK_FEMUR_JAMBES.send(player, new Replacement("<name>", target.getName()));
        }
        Messages.HANCOCK_FEMUR_ONME.send(target);

        if (MUPlayer.get(target).getRole() instanceof SanjiRole) {
            target.setHealth(target.getHealth() - 4.0);
            final int time = 15;
            final List<Block> blocks = new ArrayList<>();
            for (int x = -2; x <= 2; ++x) {
                for (int y = -2; y <= 2; ++y) {
                    for (int z = -2; z <= 2; ++z) {
                        final Block block = new Location(target.getWorld(), target.getLocation().getBlockX() + x, target.getLocation().getBlockY() + y, target.getLocation().getBlockZ() + z).getBlock();
                        block.setType(Material.STONE);
                        blocks.add(block);
                    }
                }
            }
            Bukkit.getScheduler().runTaskLater(Mugiwara.getInstance(), () -> {
                for (final Block block : blocks) {
                    block.setType(Material.AIR);
                }
            }, time * 20);
            target.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 15 * 20, 1, false, false));
            Messages.HANCOCK_FEMUR_SANJI_USE.send(player);
            Messages.HANCOCK_FEMUR_SANJI_TARGET.send(target);
        }
    }

    @Override
    public String getName() {
        return "Femur (Sword)";
    }

    @Override
    public Integer getCooldownAmount() {
        return 30;
    }
}

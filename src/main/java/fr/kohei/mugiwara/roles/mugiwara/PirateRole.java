package fr.kohei.mugiwara.roles.mugiwara;

import fr.kohei.mugiwara.Mugiwara;
import fr.kohei.mugiwara.game.player.MUPlayer;
import fr.kohei.mugiwara.roles.RolesType;
import fr.kohei.mugiwara.utils.utils.Utils;
import fr.kohei.mugiwara.utils.utils.packets.Damage;
import fr.kohei.uhc.UHC;
import fr.kohei.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PirateRole extends RolesType.MURole {
    private boolean jimbe;

    public PirateRole() {
        super(Arrays.asList());
    }

    @Override
    public RolesType getRole() {
        return RolesType.PIRATE;
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(Material.BANNER);
    }

    @Override
    public void onDistribute(Player player) {
        List<Player> players = Utils.getPlayers().stream()
                .filter(player1 -> UHC.getInstance().getGameManager().getPlayers().contains(player1.getUniqueId()))
                .filter(this::isSpecialRole)
                .collect(Collectors.toList());
        Collections.shuffle(players);
        if (players.isEmpty()) return;

        Player target = players.get(0);
        RolesType type = MUPlayer.get(target).getRole().getRole();
        Mugiwara.knowsRole(player, type);

        if (type == RolesType.ZORO) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));
            player.getInventory().addItem(new ItemBuilder(Material.DIAMOND_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 4).toItemStack());
        } else if (type == RolesType.USSOP) {
            player.getInventory().addItem(new ItemStack(Material.ARROW, 32));
            player.getInventory().addItem(new ItemBuilder(Material.BOW).addEnchant(Enchantment.ARROW_DAMAGE, 4).toItemStack());
        } else if (type == RolesType.SANJI) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false));
            Damage.addTempNoDamage(player, EntityDamageEvent.DamageCause.FALL, Integer.MAX_VALUE);
        } else if (type == RolesType.FRANKY) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
            player.setMaxHealth(player.getMaxHealth() + 4);
        } else if (type == RolesType.JIMBE) {
            ItemStack is = new ItemStack(Material.ENCHANTED_BOOK);
            EnchantmentStorageMeta im = (EnchantmentStorageMeta) is.getItemMeta();
            im.addEnchant(Enchantment.DEPTH_STRIDER, 3, true);
            is.setItemMeta(im);

            jimbe = true;
            player.getInventory().addItem(is);
        }
    }

    private boolean isSpecialRole(Player player) {
        RolesType type = MUPlayer.get(player).getRole().getRole();

        return type == RolesType.ZORO || type == RolesType.USSOP || type == RolesType.SANJI
                || type == RolesType.FRANKY || type == RolesType.JIMBE;
    }

    @Override
    public void onSecond(Player player) {
        if (!jimbe) return;
        Block block = player.getLocation().getBlock();
        if (block == null) return;
        if (!block.getType().name().contains("WATER")) return;

        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 2 * 20, 1, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 2 * 20, 0, false, false));
    }
}

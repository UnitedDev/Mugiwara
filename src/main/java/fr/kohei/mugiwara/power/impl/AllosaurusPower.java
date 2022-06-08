package fr.kohei.mugiwara.power.impl;

import fr.kohei.mugiwara.Mugiwara;
import fr.kohei.mugiwara.config.Messages;
import fr.kohei.mugiwara.power.RightClickPower;
import fr.kohei.utils.ChatUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.metadata.MetadataValueAdapter;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * @author Salers
 * made on fr.kohei.mugiwara.power.impl
 */

@Getter
public class AllosaurusPower extends RightClickPower {

    private boolean currentlyInUse;

    @Override
    public ItemStack getItem() {
        final ItemStack item = new ItemStack(Material.ROTTEN_FLESH);
        final ItemMeta meta = item.getItemMeta();

        item.addEnchantment(Enchantment.ARROW_DAMAGE, 1);

        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.setDisplayName(ChatUtil.translate("&6Allosaurus"));

        item.setItemMeta(meta);

        return item;
    }

    @Override
    public String getName() {
        return "Allosaurus";
    }

    @Override
    public Integer getCooldownAmount() {
        return 8 * 60;
    }

    @Override
    public boolean onEnable(Player player, boolean rightClick) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 60 * 4, 1));
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 60 * 4, 1));


        currentlyInUse = true;

        Bukkit.getScheduler().runTaskLater(Mugiwara.getInstance(), () -> {
            currentlyInUse = false;


        },20 * 60 * 4);

        Messages.XDRAKE_ALLOSAURUS_ACTIVATE.send(player);


        return false;
    }
}

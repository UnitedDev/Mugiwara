package fr.kohei.mugiwara.roles.impl.marine;

import fr.kohei.mugiwara.Mugiwara;
import fr.kohei.mugiwara.roles.RolesType;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;

public class CobyRole extends RolesType.MURole {
    public CobyRole() {
        super(Arrays.asList());
    }

    @Override
    public RolesType getRole() {
        return RolesType.COBY;
    }

    @Override
    public ItemStack getItem() {
        return null;
    }

    @Override
    public String[] getDescription() {
        return new String[0];
    }

    @Override
    public void onDistribute(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, Integer.MAX_VALUE, 0, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false));
        Mugiwara.knowsRole(player, RolesType.GARP);

        ItemStack is = new ItemStack(Material.ENCHANTED_BOOK);
        EnchantmentStorageMeta im = (EnchantmentStorageMeta) is.getItemMeta();
        im.addEnchant(Enchantment.DEPTH_STRIDER, 3, true);
        is.setItemMeta(im);

        player.getInventory().addItem(is);
    }
}

package fr.kohei.mugiwara.roles.mugiwara;

import fr.kohei.mugiwara.Mugiwara;
import fr.kohei.mugiwara.power.impl.SeikenPower;
import fr.kohei.mugiwara.roles.RolesType;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;

public class JimbeRole extends RolesType.MURole {
    public JimbeRole() {
        super(Arrays.asList(
                new SeikenPower()
        ));
    }

    @Override
    public RolesType getRole() {
        return RolesType.JIMBE;
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(Material.RAW_FISH, 1, (short) 3);
    }

    @Override
    public void onDistribute(Player player) {
        ItemStack is = new ItemStack(Material.ENCHANTED_BOOK);
        EnchantmentStorageMeta im = (EnchantmentStorageMeta) is.getItemMeta();
        im.addEnchant(Enchantment.DEPTH_STRIDER, 3, true);
        is.setItemMeta(im);

        player.getInventory().addItem(is);
        player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, Integer.MAX_VALUE, 3, false, false));
        Mugiwara.knowsRole(player, RolesType.LUFFY);

        int random = (int) (Math.random() * 3);
        if (random == 1) ZoroRole.randomRole(player);
    }

    @Override
    public void onSecond(Player player) {
        Block block = player.getLocation().getBlock();
        if (block == null) return;
        if (!block.getType().name().contains("WATER")) return;

        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 2 * 20, 1, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 2 * 20, 0, false, false));
    }
}

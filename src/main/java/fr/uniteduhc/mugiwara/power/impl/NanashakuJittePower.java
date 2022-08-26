package fr.uniteduhc.mugiwara.power.impl;

import fr.uniteduhc.mugiwara.power.DamagePlayerPower;
import fr.uniteduhc.mugiwara.utils.utils.Utils;
import fr.uniteduhc.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class NanashakuJittePower extends DamagePlayerPower {

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.IRON_SWORD).setName(Utils.itemFormat("&7&lNanashaku Jitte")).addEnchant(Enchantment.DAMAGE_ALL, 4).toItemStack();
    }

    @Override
    public boolean onEnable(Player player, Player target, EntityDamageByEntityEvent damage) {


        return false;
    }

    @Override
    public String getName() {
        return "Nanashaku Jitte";
    }

    @Override
    public Integer getCooldownAmount() {
        return null;
    }
}

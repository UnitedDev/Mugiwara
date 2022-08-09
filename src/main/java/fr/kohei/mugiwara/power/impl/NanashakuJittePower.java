package fr.kohei.mugiwara.power.impl;

import fr.kohei.mugiwara.game.player.MUPlayer;
import fr.kohei.mugiwara.power.DamagePlayerPower;
import fr.kohei.mugiwara.utils.utils.Utils;
import fr.kohei.utils.ItemBuilder;
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

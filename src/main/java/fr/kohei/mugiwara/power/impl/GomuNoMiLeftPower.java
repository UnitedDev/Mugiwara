package fr.kohei.mugiwara.power.impl;

import fr.kohei.mugiwara.utils.config.Messages;
import fr.kohei.mugiwara.power.RightClickPower;
import fr.kohei.mugiwara.utils.utils.packets.Damage;
import fr.kohei.mugiwara.utils.utils.Utils;
import fr.kohei.utils.ItemBuilder;
import fr.kohei.utils.ReflectionUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class GomuNoMiLeftPower extends RightClickPower {
    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.QUARTZ).addEnchant(Enchantment.DAMAGE_ALL, 1).hideItemFlags()
                .setName(Utils.itemFormat("&6&lGomu Gomu no Mi")).toItemStack();
    }

    @Override
    public boolean isGive() {
        return false;
    }

    @Override
    public String getName() {
        return "Gomu Gomu no Mi";
    }

    @Override
    public Integer getCooldownAmount() {
        return 3 * 60;
    }

    @Override
    public boolean onEnable(Player player, boolean rightClick) {
        if (rightClick) return false;

        Damage.addTempNoDamage(player, EntityDamageEvent.DamageCause.FALL, 10);
        Player target = Utils.getPlayers().stream()
                .filter(p -> p.getLocation().distance(player.getLocation()) <= 15)
                .filter(p -> ReflectionUtils.getLookingAt(player, p))
                .findFirst().orElse(null);
        if(target == null) return false;

        final Vector vector = target.getLocation().toVector().subtract(player.getLocation().toVector()).normalize();
        target.setVelocity(vector.multiply(-6));

        Messages.LUFFY_GOMUGOMUNOMI.send(player);
        return true;
    }

    @Override
    public boolean rightClick() {
        return false;
    }
}

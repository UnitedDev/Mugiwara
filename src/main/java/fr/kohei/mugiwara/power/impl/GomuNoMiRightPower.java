package fr.kohei.mugiwara.power.impl;

import fr.kohei.mugiwara.config.Messages;
import fr.kohei.mugiwara.power.RightClickPower;
import fr.kohei.mugiwara.utils.Damage;
import fr.kohei.mugiwara.utils.Utils;
import fr.kohei.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

public class GomuNoMiRightPower extends RightClickPower {
    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.QUARTZ).addEnchant(Enchantment.DAMAGE_ALL, 1).hideItemFlags()
                .setName(Utils.itemFormat("&6&lGomu Gomu no Mi")).toItemStack();
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
    public void onEnable(Player player, boolean rightClick) {
        if (!rightClick) return;

        Damage.addTempNoDamage(player, EntityDamageEvent.DamageCause.FALL, 10);
        player.setVelocity(player.getLocation().getDirection().multiply(3).setY(0.2));

        Messages.LUFFY_GOMUGOMUNOMI.send(player);
    }
}

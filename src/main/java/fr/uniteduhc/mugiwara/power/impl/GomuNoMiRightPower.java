package fr.uniteduhc.mugiwara.power.impl;

import fr.uniteduhc.mugiwara.utils.config.Messages;
import fr.uniteduhc.mugiwara.power.RightClickPower;
import fr.uniteduhc.mugiwara.utils.utils.packets.Damage;
import fr.uniteduhc.mugiwara.utils.utils.Utils;
import fr.uniteduhc.utils.ItemBuilder;
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
    public boolean onEnable(Player player, boolean rightClick) {
        if (!rightClick) return false;

        Damage.addTempNoDamage(player, EntityDamageEvent.DamageCause.FALL, 10);
        player.setVelocity(player.getLocation().getDirection().multiply(3).setY(0.2));

        Messages.LUFFY_GOMUGOMUNOMI.send(player);
        return true;
    }
}

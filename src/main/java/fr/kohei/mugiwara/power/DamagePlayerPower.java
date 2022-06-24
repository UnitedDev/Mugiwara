package fr.kohei.mugiwara.power;

import fr.kohei.mugiwara.roles.RolesType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public abstract class DamagePlayerPower extends ClickPower {

    public static DamagePlayerPower findPower(RolesType.MURole role, ItemStack item) {
        DamagePlayerPower power = null;

        for (Power rolePower : role.getPowers()) {
            if (!(rolePower instanceof DamagePlayerPower)) continue;

            DamagePlayerPower damagePower = (DamagePlayerPower) rolePower;

            ItemStack powerItem = damagePower.getItem();
            if (powerItem.getType() == item.getType() && powerItem.hasItemMeta()
                    && powerItem.getItemMeta().getDisplayName().equals(item.getItemMeta().getDisplayName())) {
                power = damagePower;
            }
        }

        return power;
    }

    public abstract boolean onEnable(Player player, Player target, EntityDamageByEntityEvent damage);

}

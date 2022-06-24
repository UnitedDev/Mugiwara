package fr.kohei.mugiwara.power;

import fr.kohei.mugiwara.roles.RolesType;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class BlockPlacePower extends ClickPower {

    public static BlockPlacePower findPower(RolesType.MURole role, ItemStack item) {
        BlockPlacePower power = null;
        if(role == null) return null;
        if(item == null) return null;

        for (Power rolePower : role.getPowers()) {
            if (!(rolePower instanceof BlockPlacePower)) continue;

            BlockPlacePower damagePower = (BlockPlacePower) rolePower;

            ItemStack powerItem = damagePower.getItem();
            if (powerItem.getType() == item.getType() && powerItem.hasItemMeta()
                    && powerItem.getItemMeta().getDisplayName().equals(item.getItemMeta().getDisplayName())) {
                power = damagePower;
            }
        }

        return power;
    }

    public abstract boolean onEnable(Player player, Location location);

}

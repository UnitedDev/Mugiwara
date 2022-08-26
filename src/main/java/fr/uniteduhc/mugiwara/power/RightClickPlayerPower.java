package fr.uniteduhc.mugiwara.power;

import fr.uniteduhc.mugiwara.roles.RolesType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class RightClickPlayerPower extends ClickPower {

    public static RightClickPlayerPower findPower(RolesType.MURole role, ItemStack item) {
        RightClickPlayerPower power = null;

        for (Power rolePower : role.getPowers()) {
            if (!(rolePower instanceof RightClickPlayerPower)) continue;

            RightClickPlayerPower rightClickPlayerPower = (RightClickPlayerPower) rolePower;

            ItemStack powerItem = rightClickPlayerPower.getItem();
            if (powerItem.getType() == item.getType() && powerItem.hasItemMeta()
                    && powerItem.getItemMeta().getDisplayName().equals(item.getItemMeta().getDisplayName())) {
                power = rightClickPlayerPower;
            }
        }

        return power;
    }

    public abstract void onEnable(Player player, Player target);

}

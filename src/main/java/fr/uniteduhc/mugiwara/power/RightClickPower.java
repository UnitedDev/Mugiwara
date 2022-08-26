package fr.uniteduhc.mugiwara.power;

import fr.uniteduhc.mugiwara.roles.RolesType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public abstract class RightClickPower extends ClickPower {

    public static RightClickPower findPower(RolesType.MURole role, ItemStack item) {
        RightClickPower power = null;

        for (Power rolePower : role.getPowers()) {
            if (!(rolePower instanceof RightClickPower)) continue;

            RightClickPower rightClickPower = (RightClickPower) rolePower;

            ItemStack powerItem = rightClickPower.getItem();
            if (powerItem.getType() == item.getType() && powerItem.hasItemMeta()
                    && powerItem.getItemMeta().getDisplayName().equals(item.getItemMeta().getDisplayName())) {
                power = rightClickPower;
            }
        }

        return power;
    }

    public static List<RightClickPower> findPowers(RolesType.MURole role, ItemStack item) {
        List<RightClickPower> power = new ArrayList<>();

        for (Power rolePower : role.getPowers()) {
            if (!(rolePower instanceof RightClickPower)) continue;

            RightClickPower rightClickPower = (RightClickPower) rolePower;

            ItemStack powerItem = rightClickPower.getItem();
            if (powerItem.getType() == item.getType() && powerItem.hasItemMeta()
                    && powerItem.getItemMeta().getDisplayName().equals(item.getItemMeta().getDisplayName())) {
                power.add(rightClickPower);
            }
        }

        return power;
    }

    public abstract boolean onEnable(Player player, boolean rightClick);
    public boolean rightClick() {
        return true;
    }

}

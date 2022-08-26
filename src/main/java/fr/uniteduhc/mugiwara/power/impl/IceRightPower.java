package fr.uniteduhc.mugiwara.power.impl;

import fr.uniteduhc.mugiwara.game.menu.IceRightPowerMenu;
import fr.uniteduhc.mugiwara.power.RightClickPower;
import fr.uniteduhc.mugiwara.utils.utils.Utils;
import fr.uniteduhc.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class IceRightPower extends RightClickPower {

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.PACKED_ICE).setName(Utils.itemFormat("&b&lHie Hie no Mi")).toItemStack();
    }

    @Override
    public String getName() {
        return "Hie Hie no Mi";
    }

    @Override
    public Integer getCooldownAmount() {
        return null;
    }

    @Override
    public boolean onEnable(Player player, boolean rightClick) {

        new IceRightPowerMenu().openMenu(player);

        return false;
    }
}

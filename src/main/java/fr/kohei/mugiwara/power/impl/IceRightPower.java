package fr.kohei.mugiwara.power.impl;

import fr.kohei.mugiwara.game.menu.IceRightPowerMenu;
import fr.kohei.mugiwara.power.RightClickPower;
import fr.kohei.mugiwara.utils.utils.Utils;
import fr.kohei.utils.ItemBuilder;
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

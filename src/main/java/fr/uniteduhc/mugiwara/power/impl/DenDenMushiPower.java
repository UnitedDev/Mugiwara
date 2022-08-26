package fr.uniteduhc.mugiwara.power.impl;

import fr.uniteduhc.mugiwara.Mugiwara;
import fr.uniteduhc.mugiwara.game.menu.DenDenMushiMenu;
import fr.uniteduhc.mugiwara.power.RightClickPower;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class DenDenMushiPower extends RightClickPower {
    @Override
    public ItemStack getItem() {
        return Mugiwara.getInstance().getDenMushiManager().getDenDenMushi();
    }

    @Override
    public String getName() {
        return "Demande DenDen Mushi";
    }

    @Override
    public Integer getCooldownAmount() {
        return 15;
    }

    @Override
    public boolean onEnable(Player player, boolean rightClick) {
        new DenDenMushiMenu().openMenu(player);

        return true;
    }

    @Override
    public boolean isGive() {
        return true;
    }
}

package fr.kohei.mugiwara.power;

import org.bukkit.inventory.ItemStack;

public abstract class ClickPower extends Power {

    public abstract ItemStack getItem();

    public boolean isGive() {
        return true;
    }

    public boolean isDropAtDeath() {
        return false;
    }

}

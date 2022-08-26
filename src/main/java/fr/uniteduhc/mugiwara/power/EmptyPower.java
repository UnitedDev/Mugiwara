package fr.uniteduhc.mugiwara.power;

import org.bukkit.entity.Player;

public abstract class EmptyPower extends RightClickPower {

    @Override
    public Integer getCooldownAmount() {
        return null;
    }

    @Override
    public boolean onEnable(Player player, boolean rightClick) {
        return false;
    }
}

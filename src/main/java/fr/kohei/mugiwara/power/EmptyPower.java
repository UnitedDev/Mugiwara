package fr.kohei.mugiwara.power;

import org.bukkit.entity.Player;

public abstract class EmptyPower extends RightClickPower {

    @Override
    public Integer getCooldownAmount() {
        return null;
    }

    @Override
    public void onEnable(Player player) {

    }
}

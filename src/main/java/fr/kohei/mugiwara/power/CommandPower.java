package fr.kohei.mugiwara.power;

import org.bukkit.entity.Player;

public abstract class CommandPower extends Power {

    public abstract String getArgument();

    public abstract void onEnable(Player player, String[] args);

}

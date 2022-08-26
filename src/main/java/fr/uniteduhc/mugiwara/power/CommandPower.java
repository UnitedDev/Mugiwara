package fr.uniteduhc.mugiwara.power;

import org.bukkit.entity.Player;

public abstract class CommandPower extends Power {

    public abstract String getArgument();

    public abstract boolean onEnable(Player player, String[] args);

}

package fr.uniteduhc.mugiwara.power.impl;

import fr.uniteduhc.mugiwara.power.CommandPower;
import org.bukkit.entity.Player;

public class PrimePower extends CommandPower {
    @Override
    public String getArgument() {
        return "prime";
    }

    @Override
    public boolean onEnable(Player player, String[] args) {
        return false;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public Integer getCooldownAmount() {
        return null;
    }
}

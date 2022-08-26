package fr.uniteduhc.mugiwara.camp.impl;

import fr.uniteduhc.mugiwara.camp.CampType;
import org.bukkit.ChatColor;

public class
SoloCamp extends CampType.MUCamp {
    @Override
    public String getName() {
        return "Solo";
    }

    @Override
    public boolean isSolo() {
        return true;
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.GOLD;
    }

    @Override
    public CampType getCampType() {
        return CampType.SOLO;
    }
}

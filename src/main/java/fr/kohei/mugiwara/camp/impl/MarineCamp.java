package fr.kohei.mugiwara.camp.impl;

import fr.kohei.mugiwara.camp.CampType;
import org.bukkit.ChatColor;

public class MarineCamp extends CampType.MUCamp {
    @Override
    public String getName() {
        return "Marine";
    }

    @Override
    public boolean isSolo() {
        return false;
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.BLUE;
    }

    @Override
    public CampType getCampType() {
        return CampType.MARINE;
    }
}

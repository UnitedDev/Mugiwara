package fr.uniteduhc.mugiwara.camp.impl;

import fr.uniteduhc.mugiwara.camp.CampType;
import org.bukkit.ChatColor;

public class SaboKumaCamp extends CampType.MUCamp {
    @Override
    public CampType getCampType() {
        return CampType.SABO_KUMA;
    }

    @Override
    public String getName() {
        return "Sabo et Kuma";
    }

    @Override
    public boolean isSolo() {
        return false;
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.LIGHT_PURPLE;
    }
}

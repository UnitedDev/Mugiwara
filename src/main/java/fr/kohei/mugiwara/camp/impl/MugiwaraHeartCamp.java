package fr.kohei.mugiwara.camp.impl;

import fr.kohei.mugiwara.camp.CampType;
import org.bukkit.ChatColor;

public class MugiwaraHeartCamp extends CampType.MUCamp {
    @Override
    public String getName() {
        return "Mugiwara & Heart";
    }

    @Override
    public boolean isSolo() {
        return false;
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.RED;
    }

    @Override
    public CampType getCampType() {
        return CampType.MUGIWARA_HEART;
    }
}

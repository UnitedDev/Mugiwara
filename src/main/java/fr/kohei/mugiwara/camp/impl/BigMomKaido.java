package fr.kohei.mugiwara.camp.impl;

import fr.kohei.mugiwara.camp.CampType;
import org.bukkit.ChatColor;

public class BigMomKaido extends CampType.MUCamp {
    @Override
    public String getName() {
        return "Alliance Big Mom & Kaidö";
    }

    @Override
    public boolean isSolo() {
        return false;
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.LIGHT_PURPLE;
    }

    @Override
    public CampType getCampType() {
        return CampType.BIGMOM_KAIDO;
    }
}

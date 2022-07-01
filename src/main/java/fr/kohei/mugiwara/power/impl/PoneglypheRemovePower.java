package fr.kohei.mugiwara.power.impl;

import fr.kohei.mugiwara.Mugiwara;
import fr.kohei.mugiwara.game.poneglyphe.Poneglyphe;
import fr.kohei.mugiwara.game.poneglyphe.PoneglypheManager;
import fr.kohei.mugiwara.power.CommandPower;
import fr.kohei.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class PoneglypheRemovePower extends CommandPower {
    private boolean used;

    @Override
    public String getArgument() {
        return "hide";
    }

    @Override
    public boolean onEnable(Player player, String[] args) {
        if (used) {
            player.sendMessage(ChatUtil.prefix("&cVous avez déjà utilisé cette commande."));
            return false;
        }

        Poneglyphe poneglyphe = null;
        PoneglypheManager manager = Mugiwara.getInstance().getPoneglypheManager();
        for (Poneglyphe pone : new Poneglyphe[]{
                manager.getFirstPoneglyphe(), manager.getSecondPoneglyphe(), manager.getThirdPoneglyphe(), manager.getFourthPoneglyphe()
        }) {
            if (pone.getInitialLocation() == null) continue;

            if(pone.getInitialLocation().distance(player.getLocation()) <= 15) {
                poneglyphe = pone;
                break;
            }
        }

        if(poneglyphe == null) {
            player.sendMessage(ChatUtil.prefix("&cVous n'êtes pas à côté d'un poneglyphe."));
            return false;
        }

        manager.setRemoved(poneglyphe.getId());
        poneglyphe.getCuboid().getBlockList().forEach(block -> block.setType(Material.AIR));
        used = true;

        Poneglyphe finalPoneglyphe = poneglyphe;
        Bukkit.getScheduler().runTaskLater(Mugiwara.getInstance(), () -> {
            manager.spawn(finalPoneglyphe, false);
            manager.setRemoved(null);
        }, 5 * 20 * 60);
        return true;
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

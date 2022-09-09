package fr.uniteduhc.mugiwara.power.impl;

import fr.uniteduhc.mugiwara.Mugiwara;
import fr.uniteduhc.mugiwara.game.player.MUPlayer;
import fr.uniteduhc.mugiwara.power.CommandPower;
import fr.uniteduhc.mugiwara.roles.RolesType;
import fr.uniteduhc.mugiwara.roles.solo.KuzanRole;
import fr.uniteduhc.utils.TimeUtil;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.TimeUnit;

public class RevengePower extends CommandPower {

    private boolean used;

    @Override
    public String getArgument() {
        return "revenge";
    }

    @Override
    public boolean onEnable(Player player, String[] args) {

        if(used){
            /**
             * TODO MESSAGE ON USE
             */
            return false;
        }

        used = true;

        new BukkitRunnable(){
            int timer = 0;
            @Override
            public void run() {

                timer++;
                Mugiwara.getInstance().addActionBar(player, "&cRevenge &8&f" + TimeUtil.getReallyNiceTime2(timer * 1000L), "revenge");

                if(timer > 60){
                    Mugiwara.getInstance().removeActionBar(player, "revenge");
                    revenge(player);
                    cancel();
                }


            }
        }.runTaskTimer(Mugiwara.getInstance(), 0, 20);

        return false;
    }

    private void revenge(final Player player){
        MUPlayer muPlayer = MUPlayer.get(player);
        RolesType.MURole muRole = muPlayer.getRole();

        if(!(muRole instanceof KuzanRole)) return;

        KuzanRole kuzanRole = (KuzanRole) muRole;

        if(!kuzanRole.allowRevenge()) return;




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

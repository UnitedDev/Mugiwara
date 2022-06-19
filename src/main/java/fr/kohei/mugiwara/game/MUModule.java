package fr.kohei.mugiwara.game;

import fr.kohei.mugiwara.Mugiwara;
import fr.kohei.mugiwara.camp.CampType;
import fr.kohei.mugiwara.power.ClickPower;
import fr.kohei.mugiwara.power.Power;
import fr.kohei.mugiwara.roles.RolesType;
import fr.kohei.mugiwara.tasks.CooldownCheckTask;
import fr.kohei.mugiwara.tasks.PoneglypheTask;
import fr.kohei.uhc.game.player.UPlayer;
import fr.kohei.uhc.module.Module;
import fr.kohei.uhc.module.manager.Camp;
import fr.kohei.uhc.module.manager.Role;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class MUModule extends Module {
    @Override
    public String getCommandPrefix() {
        return "mu";
    }

    @Override
    public String getName() {
        return "Mugiwara";
    }

    @Override
    public HashMap<RoleType, Class<? extends Role>> getRoles() {
        HashMap<RoleType, Class<? extends Role>> roles = new HashMap<>();

        for (RolesType value : RolesType.values()) {
            roles.put(new RoleType(value.getName(), value.getCampType().getCamp()), value.getRoleClass());
        }

        return roles;
    }

    @Override
    public List<Camp> getCamps() {
        List<Camp> camps = new ArrayList<>();

        for (CampType value : CampType.values()) {
            camps.add(value.getCamp());
        }

        return camps;
    }

    @Override
    public void onStart() {
        Mugiwara.getInstance().getPoneglypheManager().onStart();
    }

    @Override
    public void onEpisode() {

    }

    @Override
    public void onDay() {

    }

    @Override
    public void onNight() {

    }

    @Override
    public void onRoles() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    RolesType.MURole role = (RolesType.MURole) UPlayer.get(onlinePlayer).getRole();

                    onlinePlayer.setHealth(onlinePlayer.getMaxHealth());
                    if (role == null) continue;
                    for (Power power : role.getPowers()) {
                        if (power instanceof ClickPower && ((ClickPower) power).isGive()) {
                            onlinePlayer.getInventory().addItem(((ClickPower) power).getItem());
                        }
                    }
                }
            }
        }.runTaskLater(Mugiwara.getInstance(), 50);

        new CooldownCheckTask();
        new PoneglypheTask();
    }

    @Override
    public void onDeath(Player player, Player player1) {

    }

    @Override
    public void onDisconnectDeath(UUID uuid) {

    }

    @Override
    public boolean hasRoles() {
        return true;
    }

    @Override
    public boolean hasCycle() {
        return true;
    }

    @Override
    public boolean isRolesPerTeam() {
        return false;
    }
}

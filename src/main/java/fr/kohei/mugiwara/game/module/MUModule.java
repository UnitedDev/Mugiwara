package fr.kohei.mugiwara.game.module;

import fr.kohei.menu.Menu;
import fr.kohei.mugiwara.Mugiwara;
import fr.kohei.mugiwara.camp.CampType;
import fr.kohei.mugiwara.game.events.poneglyphe.PoneglypheMenu;
import fr.kohei.mugiwara.game.events.SectionManager;
import fr.kohei.mugiwara.game.menu.TresorMenu;
import fr.kohei.mugiwara.power.ClickPower;
import fr.kohei.mugiwara.power.Power;
import fr.kohei.mugiwara.roles.RolesType;
import fr.kohei.mugiwara.game.tasks.CooldownCheckTask;
import fr.kohei.mugiwara.game.tasks.PoneglypheTask;
import fr.kohei.mugiwara.utils.utils.Utils;
import fr.kohei.uhc.game.player.UPlayer;
import fr.kohei.uhc.module.Module;
import fr.kohei.uhc.module.manager.Camp;
import fr.kohei.uhc.module.manager.Role;
import fr.kohei.utils.ItemBuilder;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

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
    @SneakyThrows
    public HashMap<RoleType, Class<? extends Role>> getRoles() {
        HashMap<RoleType, Class<? extends Role>> roles = new HashMap<>();

        for (RolesType value : RolesType.values()) {
            if (value.getRoleClass() == null) continue;
            roles.put(new RoleType(value.getName(), value.getCampType().getCamp(), value.getDisplay()), value.getRoleClass());
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
        Mugiwara.getInstance().getTresorManager().onStart();
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
                for (Player onlinePlayer : Utils.getPlayers()) {
                    RolesType.MURole role = (RolesType.MURole) UPlayer.get(onlinePlayer).getRole();

                    onlinePlayer.setHealth(onlinePlayer.getMaxHealth());
                    if (role == null) continue;
                    for (Power power : role.getPowers()) {
                        if (power instanceof ClickPower && ((ClickPower) power).isGive()) {
                            onlinePlayer.getInventory().addItem(((ClickPower) power).getItem());
                        }
                    }
                }

                Mugiwara.getInstance().getFruitDuDemonManager().init();
            }
        }.runTaskLater(Mugiwara.getInstance(), 50);

        Bukkit.getScheduler().runTaskLater(Mugiwara.getInstance(), () -> {
            new SectionManager().giveBadge();
        }, 2 * 20 * 60);

        new CooldownCheckTask();
        new PoneglypheTask();
    }

    @Override
    public void onDeath(Player player, Player player1) {
        Mugiwara.getInstance().attemptWin();
    }

    @Override
    public void onDisconnectDeath(UUID uuid) {
        Mugiwara.getInstance().attemptWin();
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

    @Override
    public HashMap<ItemStack, Menu> getMenus() {
        HashMap<ItemStack, Menu> menus = new HashMap<>();

        menus.put(
                new ItemBuilder(Material.REDSTONE_BLOCK).setName("&cPonéglyphes").setLore(
                        "&fPermet d'accéder à tous les timers pour",
                        "&fles ponéglyphes.",
                        "",
                        "&f&l» &cCliquez pour y accéder"
                ).toItemStack(), new PoneglypheMenu()
        );
        menus.put(
                new ItemBuilder(Material.CHEST).setName("&cTrésor").setLore(
                        "&fPermet d'accéder à tous les timers pour",
                        "&fles trésors.",
                        "",
                        "&f&l» &cCliquez pour y accéder"
                ).toItemStack(), new TresorMenu()
        );


        return menus;
    }
}
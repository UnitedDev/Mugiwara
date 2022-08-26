package fr.uniteduhc.mugiwara.game.events.poneglyphe;

import fr.uniteduhc.mugiwara.Mugiwara;
import fr.uniteduhc.mugiwara.game.player.MUPlayer;
import fr.uniteduhc.mugiwara.roles.RolesType;
import fr.uniteduhc.mugiwara.utils.config.Messages;
import fr.uniteduhc.mugiwara.utils.utils.Utils;
import fr.uniteduhc.uhc.UHC;
import fr.uniteduhc.utils.ChatUtil;
import fr.uniteduhc.utils.Cuboid;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class PoneglypheManager {
    private Poneglyphe firstPoneglyphe;
    private Poneglyphe secondPoneglyphe;
    private Poneglyphe thirdPoneglyphe;
    private Poneglyphe fourthPoneglyphe;

    private Integer removed;
    private final HashMap<UUID, Integer> captures;
    private final List<Location> cantBreak;

    public PoneglypheManager(Plugin plugin) {
        this.firstPoneglyphe = new Poneglyphe(1, null, null, 35 * 60);
        this.secondPoneglyphe = new Poneglyphe(2, null, null, 45 * 60);
        this.thirdPoneglyphe = new Poneglyphe(3, null, null, 65 * 60);
        this.fourthPoneglyphe = new Poneglyphe(4, null, null, 75 * 60);

        this.removed = null;
        this.captures = new HashMap<>();
        this.cantBreak = new ArrayList<>();
    }

    public void onStart() {

        for (Poneglyphe poneglyphe : new Poneglyphe[]{firstPoneglyphe, secondPoneglyphe, thirdPoneglyphe, fourthPoneglyphe}) {
            startTask(poneglyphe);

            Location location = getRandomLocation(poneglyphe);
            Cuboid cuboid = new Cuboid(location.clone().add(3, 0, 3), location.clone().add(-3, 8, -3));
            poneglyphe.setCuboid(cuboid);
            poneglyphe.setInitialLocation(location);
        }
    }

    private void startTask(Poneglyphe poneglyphe) {
        Bukkit.getScheduler().runTaskLater(Mugiwara.getInstance(), () -> spawn(poneglyphe, true), poneglyphe.getTimer() * 20L);
    }

    public void spawn(Poneglyphe poneglyphe, boolean broadcast) {
        if (broadcast)
            Utils.getPlayers().forEach(Messages.PONEGLYPHE_SPAWN::send);
        System.out.println(poneglyphe.getInitialLocation() + "");

        Cuboid cuboid = poneglyphe.getCuboid();
        cuboid.getBlockList().forEach(block -> {
            cantBreak.add(block.getLocation());
            block.setType(Material.REDSTONE_BLOCK);
            poneglyphe.setSpawned(true);
        });

        ArmorStand armorStand = (ArmorStand) cuboid.getWorld().spawnEntity(cuboid.getCenter().clone().add(0, 5, 0), EntityType.ARMOR_STAND);
        armorStand.setVisible(false);
        armorStand.setCustomName("§c§lRoad Ponéglyphe " + poneglyphe.getId());
        armorStand.setCustomNameVisible(true);
        armorStand.setGravity(false);

        Utils.getPlayers().forEach(player -> {
            RolesType rolesType = MUPlayer.get(player).getRole().getRole();
            if (rolesType == RolesType.TEACH) {
                final UUID uuid = player.getUniqueId();
                Bukkit.getScheduler().runTaskLater(Mugiwara.getInstance(), () -> {
                    Bukkit.getPlayer(uuid).sendMessage(ChatUtil.prefix("&fCoordonnées du &aponéglyphe: " +
                            "&a" + poneglyphe.getInitialLocation().getBlockX() + "&f, " +
                            "&a" + poneglyphe.getInitialLocation().getBlockY() + "&f, " +
                            "&a" + poneglyphe.getInitialLocation().getBlockZ()
                    ));
                }, 4 + (int) (Math.random() * 4) * 60 * 20);
            }
        });
    }

    private Location getRandomLocation(Poneglyphe poneglyphe) {
        int i = poneglyphe.getId();
        World world = UHC.getInstance().getGameManager().getUhcWorld();
        int x;
        int z;

        if (i == 0) {
            x = (int) -((Math.random() * 250) + 125);
            z = (int) -((Math.random() * 250) + 125);
        } else if (i == 1) {
            x = (int) -((Math.random() * 250) + 125);
            z = (int) ((Math.random() * 250) + 125);
        } else if (i == 2) {
            x = (int) ((Math.random() * 250) + 125);
            z = (int) -((Math.random() * 250) + 125);
        } else {
            x = (int) ((Math.random() * 250) + 125);
            z = (int) ((Math.random() * 250) + 125);
        }

        int y = world.getHighestBlockYAt(x, z) - 2;

        return new Location(world, x, y, z);
    }

    public void onAllCapture(Player player) {
        RolesType role = MUPlayer.get(player).getRole().getRole();


    }

}

package fr.kohei.mugiwara.poneglyphe;

import fr.kohei.mugiwara.Mugiwara;
import fr.kohei.mugiwara.config.Messages;
import fr.kohei.uhc.UHC;
import fr.kohei.utils.Cuboid;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;

@Getter
@Setter
public class PoneglypheManager {
    private Poneglyphe firstPoneglyphe;
    private Poneglyphe secondPoneglyphe;
    private Poneglyphe thirdPoneglyphe;
    private Poneglyphe fourthPoneglyphe;

    public PoneglypheManager() {
        this.firstPoneglyphe = new Poneglyphe(1, null, null, 35 * 60);
        this.secondPoneglyphe = new Poneglyphe(2, null, null, 45 * 60);
        this.thirdPoneglyphe = new Poneglyphe(3, null, null, 65 * 60);
        this.fourthPoneglyphe = new Poneglyphe(4, null, null, 75 * 60);
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
        Bukkit.getScheduler().runTaskLater(Mugiwara.getInstance(), () -> spawn(poneglyphe), poneglyphe.getTimer());
    }

    private void spawn(Poneglyphe poneglyphe) {
        Bukkit.getOnlinePlayers().forEach(Messages.PONEGLYPHE_SPAWN::send);

        Cuboid cuboid = poneglyphe.getCuboid();
        cuboid.getBlockList().forEach(block -> block.setType(Material.REDSTONE_BLOCK));

        ArmorStand armorStand = (ArmorStand) cuboid.getWorld().spawnEntity(cuboid.getCenter().clone().add(0, 5, 0), EntityType.ARMOR_STAND);
        armorStand.setVisible(false);
        armorStand.setCustomName("&c&lRaid Ponéglyphe " + poneglyphe.getId());
        armorStand.setCustomNameVisible(true);
        armorStand.setGravity(false);
    }

    private Location getRandomLocation(Poneglyphe poneglyphe) {
        int i = poneglyphe.getId();
        World world = UHC.getGameManager().getUhcWorld();
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

        int y = world.getHighestBlockYAt(x, z);

        return new Location(world, x, y, z);
    }

}

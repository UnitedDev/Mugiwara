package fr.kohei.mugiwara.roles.marine;

import fr.kohei.mugiwara.game.player.MUPlayer;
import fr.kohei.mugiwara.power.impl.CrochetDamagePower;
import fr.kohei.mugiwara.power.impl.GroundSeccoRightPower;
import fr.kohei.mugiwara.roles.RolesType;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class CrocodileRole extends RolesType.MURole implements Listener {

    private boolean isInWater = false;
    private Location location = null;
    private boolean isUseGroundSecco = false;
    private int timer = 0;

    public CrocodileRole() {
        super(Arrays.asList(
                new CrochetDamagePower(),
                new GroundSeccoRightPower()
        ));
    }

    @Override
    public RolesType getRole() {
        return RolesType.CROCODILE;
    }

    @Override
    public void onDay(Player player) {
        player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
    }

    @Override
    public void onNight(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));
    }

    @Override
    public void onSecond(Player player) {
        if (player.getLocation().clone().add(0, -1, 0).getBlock().getType() == Material.SAND)
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 4, 1, false, false));

        isInWater = false;

        if (isInWater(player)) {
            isInWater = true;
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 30 * 20, 0, false, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 30 * 20, 0, false, false));
        }

        if (isUseGroundSecco && timer < 11) {
            if (!checkCoord(player) || timer == 10) {
                replaceAllBlocks(player, (timer * 5));

                isUseGroundSecco = false;
                timer = 0;
                return;
            }
            timer++;
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player || e.getEntity() instanceof Player)) return;

        Player damager = (Player) e.getDamager();
        Player player = (Player) e.getEntity();

        MUPlayer muDamager = MUPlayer.get(damager);

        if (!(muDamager.getRole() instanceof CrocodileRole)) return;

        if (damager.getItemInHand().getType().name().contains("SWORD")) {
            player.setFoodLevel(player.getFoodLevel() - 1);
        }
    }

    public boolean isInWater(Player player) {
        return player.getLocation().clone().getBlock().getType().name().contains("WATER") || player.getLocation().clone().add(0, -1, 0).getBlock().getType().name().contains("WATER");
    }

    public boolean checkCoord(Player player) {
        int x = player.getLocation().getBlockX();
        int y = player.getLocation().getBlockY();
        int z = player.getLocation().getBlockZ();
        return (x == getLocation().getBlockX() && y == getLocation().getBlockY() && z == getLocation().getBlockZ());
    }

    private void replaceAllBlocks(Player player, int radius) {

        List<Location> sphereLocation = getSphere(player.getLocation(), radius);

        sphereLocation.stream()
                .filter(location -> location.getBlock().getType() != Material.BEDROCK)
                .filter(location -> location.getBlock().getType() != Material.AIR)
                .forEach(location -> location.getBlock().setType(Material.SAND));


    }

    private List<Location> getSphere(Location centerBlock, int radius) {
        List<Location> circleBlocks = new ArrayList<>();
        int bX = centerBlock.getBlockX();
        int bY = centerBlock.getBlockY();
        int bZ = centerBlock.getBlockZ();
        for (int x = bX - radius; x <= bX + radius; x++) {
            for (int y = bY - radius; y <= bY + radius; y++) {
                for (int z = bZ - radius; z <= bZ + radius; z++) {
                    Location block = new Location(centerBlock.getWorld(), x, y, z);
                    circleBlocks.add(block);
                }
            }
        }
        return circleBlocks;
    }

}

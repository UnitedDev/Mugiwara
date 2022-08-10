package fr.kohei.mugiwara.power.impl;

import fr.kohei.mugiwara.Mugiwara;
import fr.kohei.mugiwara.game.player.MUPlayer;
import fr.kohei.mugiwara.power.RightClickPower;
import fr.kohei.mugiwara.roles.marine.CrocodileRole;
import fr.kohei.mugiwara.utils.config.Messages;
import fr.kohei.mugiwara.utils.config.Replacement;
import fr.kohei.mugiwara.utils.utils.Utils;
import fr.kohei.mugiwara.utils.utils.packets.MathUtil;
import fr.kohei.utils.ItemBuilder;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

@Getter
@Setter
public class GroundSeccoRightPower extends RightClickPower {

    private Location location = null;


    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.SAND).setName(Utils.itemFormat("&6&lGround Secco")).toItemStack();
    }

    @Override
    public String getName() {
        return "Ground Secco";
    }

    @Override
    public Integer getCooldownAmount() {
        return 600;
    }

    @Override
    public boolean onEnable(Player player, boolean rightClick) {
        MUPlayer muPlayer = MUPlayer.get(player);

        CrocodileRole crocodileRole = (CrocodileRole) muPlayer.getRole();

        if (crocodileRole.isInWater()) {
            Messages.CROCODILE_GROUND_SECCO_WATER.send(player);
            return false;
        }

        setLocation(player.getLocation().clone());

        new BukkitRunnable() {
            int radius = 0;
            @Override
            public void run() {


                if (!checkCoord(player) || radius == 50) {
                    replaceAllBlocks(player, radius);
                    Mugiwara.getInstance().removeActionBar(player, "radius");
                    cancel();
                    return;
                }

                radius = radius + 5;
                Mugiwara.getInstance().addActionBar(player, "&cRadius &8» &f" + radius, "radius");

            }
        }.runTaskTimer(Mugiwara.getInstance(), 0L, 10L);

        Messages.CROCODILE_GROUND_SECCO_USE.send(player);

        return true;
    }

    public boolean checkCoord(Player player) {
        int x = player.getLocation().getBlockX();
        int y = player.getLocation().getBlockY();
        int z = player.getLocation().getBlockZ();
        return (x == getLocation().getBlockX() && y == getLocation().getBlockY() && z == getLocation().getBlockZ());
    }

    private void replaceAllBlocks(Player player, int radius) {

        List<Location> sphereLocation = MathUtil.getSphere(player.getLocation(), radius, false);

        sphereLocation.stream()
                .filter(location -> location.getBlock().getType() != Material.BEDROCK)
                .filter(location -> location.getBlock().getType() != Material.AIR)
                .filter(location -> !location.getBlock().getType().name().contains("SAND"))
                .forEach(location -> location.getBlock().setType(Material.SANDSTONE));

        Messages.CROCODILE_GROUND_SECCO_REPLACE.send(player, new Replacement("<radius>", radius));


    }
}

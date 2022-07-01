package fr.kohei.mugiwara.power.impl;

import fr.kohei.mugiwara.Mugiwara;
import fr.kohei.mugiwara.power.RightClickPower;
import fr.kohei.mugiwara.utils.config.Messages;
import fr.kohei.mugiwara.utils.utils.Utils;
import fr.kohei.mugiwara.utils.utils.packets.Movement;
import fr.kohei.utils.Cuboid;
import fr.kohei.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class KaishinPower extends RightClickPower {
    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.FEATHER).addEnchant(Enchantment.DAMAGE_ALL, 1).hideItemFlags()
                .setName(Utils.itemFormat("&b&lKaishin")).toItemStack();
    }

    @Override
    public String getName() {
        return "Kaishin";
    }

    @Override
    public Integer getCooldownAmount() {
        return 15 * 60;
    }

    @Override
    public boolean onEnable(Player player, boolean rightClick) {
        for (Player nearPlayer : Utils.getNearPlayers(player, 50)) {
            Movement.freeze(nearPlayer, 5);
            nearPlayer.playSound(nearPlayer.getLocation(), Sound.WITHER_DEATH, 1, 1);
        }

        Messages.TEACH_KAISHIN_USE.send(player);
        Utils.getPlayers().forEach(Messages.TEACH_KAISHIN_EVERYONE::send);

        Bukkit.getScheduler().runTaskLater(Mugiwara.getInstance(), () -> destroy(player), 5 * 20);
        return true;
    }

    private void destroy(Player player) {
        Location initialLocation = player.getLocation();
        initialLocation = initialLocation.clone().add(5, 0, 10);

        List<Location> first = new ArrayList<>();
        for (int i = 0; i <= 30; i++) {
            Location newLoc = initialLocation.clone().add(i, 0, i);
            first.add(newLoc);
        }
        this.removeBlocks(first);

        initialLocation = initialLocation.clone().add(-10, 0, -20);
        List<Location> second = new ArrayList<>();
        for (int i = 0; i <= 30; i++) {
            Location newLoc = initialLocation.clone().add(i, 0, -i);
            second.add(newLoc);
        }
        this.removeBlocks(second);

        initialLocation = initialLocation.clone().add(-10, 0, 20);
        List<Location> third = new ArrayList<>();
        for (int i = 0; i <= 30; i++) {
            Location newLoc = initialLocation.clone().add(-i, 0, i);
            third.add(newLoc);
        }
        this.removeBlocks(third);
    }

    private void removeBlocks(List<Location> locations) {
        for (Location location : locations) {
            int random = -(30 + (int) (Math.random() * 10));

            Cuboid cuboid = new Cuboid(
                    location.clone().add(4, 2, 4),
                    location.clone().add(-4, random, -4)
            );
            cuboid.getBlockList().stream().filter(block -> block.getType() != Material.BEDROCK)
                    .forEach(block -> block.setType(Material.AIR));
        }
    }
}

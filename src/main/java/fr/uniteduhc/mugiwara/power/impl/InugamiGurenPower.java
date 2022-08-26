package fr.uniteduhc.mugiwara.power.impl;

import fr.uniteduhc.mugiwara.power.RightClickPower;
import fr.uniteduhc.mugiwara.utils.config.Messages;
import fr.uniteduhc.mugiwara.utils.config.Replacement;
import fr.uniteduhc.mugiwara.utils.utils.Utils;
import fr.uniteduhc.mugiwara.utils.utils.packets.MathUtil;
import fr.uniteduhc.utils.ItemBuilder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class InugamiGurenPower extends RightClickPower {

    private int use = 3;

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.BLAZE_ROD)
                .setName(Utils.itemFormat("&6&lInugami Guren"))
                .toItemStack();
    }

    @Override
    public String getName() {
        return "Inugami Guren";
    }

    @Override
    public Integer getCooldownAmount() {
        return 300;
    }

    @Override
    public boolean onEnable(Player player, boolean rightClick) {

        if (this.use == 0) {
            Messages.AKAINU_INUGAMI_CANT.send(player);
            return false;
        }

        Optional<Player> opt = Utils.getPlayers().stream()
                .filter(players -> MathUtil.getLookingAt(player, players))
                .findFirst();

        Player target = (Player) opt.orElse(null);

        if (target == null){
            Messages.AKAINU_INUGAMI_NULL.send(player);
            return false;
        }

        if (player.getLocation().distance(target.getLocation()) > 10) {
            Messages.AKAINU_INUGAMI_NEAR.send(player, new Replacement("<name>", target.getName()));
            return false;
        }

        this.use--;
        Messages.AKAINU_INUGAMI_USE.send(player, new Replacement("<name>", target.getName()));
        Messages.AKAINU_INUGAMI_TARGET.send(target);

        Location location = target.getLocation();

        location.getBlock().setType(Material.LAVA);

        /**
         * TODO EDIT
         */

        location.clone().add(1, 0, 2).getBlock().setType(Material.LAVA);
        location.clone().add(0, 0, 2).getBlock().setType(Material.LAVA);
        location.clone().add(-1, 0, 2).getBlock().setType(Material.LAVA);

        location.clone().add(1, 0, 1).getBlock().setType(Material.LAVA);
        location.clone().add(0, 0, 1).getBlock().setType(Material.LAVA);
        location.clone().add(-1, 0, 1).getBlock().setType(Material.LAVA);

        location.clone().add(1, 0, 0).getBlock().setType(Material.LAVA);
        location.clone().add(0, 0, 0).getBlock().setType(Material.LAVA);
        location.clone().add(-1, 0, 0).getBlock().setType(Material.LAVA);

        location.clone().add(1, 0, -1).getBlock().setType(Material.LAVA);
        location.clone().add(0, 0, -1).getBlock().setType(Material.LAVA);
        location.clone().add(-1, 0, -1).getBlock().setType(Material.LAVA);

        location.clone().add(1, 0, -2).getBlock().setType(Material.LAVA);
        location.clone().add(0, 0, -2).getBlock().setType(Material.LAVA);
        location.clone().add(-1, 0, -2).getBlock().setType(Material.LAVA);


        player.teleport(location);


        return true;
    }
}

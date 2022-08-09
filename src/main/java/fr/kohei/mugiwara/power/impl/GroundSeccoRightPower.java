package fr.kohei.mugiwara.power.impl;

import fr.kohei.mugiwara.game.player.MUPlayer;
import fr.kohei.mugiwara.power.RightClickPower;
import fr.kohei.mugiwara.roles.marine.CrocodileRole;
import fr.kohei.mugiwara.utils.config.Messages;
import fr.kohei.mugiwara.utils.utils.Utils;
import fr.kohei.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GroundSeccoRightPower extends RightClickPower {

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

        if(crocodileRole.isInWater()){
            Messages.CROCODILE_GROUND_SECCO_WATER.send(player);
            return false;
        }

        crocodileRole.setLocation(player.getLocation().clone());
        crocodileRole.setUseGroundSecco(true);
        Messages.CROCODILE_GROUND_SECCO_USE.send(player);

        return true;
    }
}

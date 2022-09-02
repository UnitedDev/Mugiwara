package fr.uniteduhc.mugiwara.power.impl;

import fr.uniteduhc.mugiwara.power.RightClickPlayerPower;
import fr.uniteduhc.mugiwara.utils.config.Messages;
import fr.uniteduhc.mugiwara.utils.utils.Utils;
import fr.uniteduhc.utils.ChatUtil;
import fr.uniteduhc.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class AssignPower extends RightClickPlayerPower {

    private int use = 3;

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.NETHER_STAR)
                .setName(Utils.itemFormat("&c&lAssign"))
                .toItemStack();
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public Integer getCooldownAmount() {
        return null;
    }

    @Override
    public void onEnable(Player player, Player target) {

        if(use == 0){
            player.sendMessage(ChatUtil.prefix("&cVous avez déjà utilisé votre pouvoir !"));
            return;
        }

        use--;
        Messages.EUSTASS_ASSIGN_USE.send(player);

    }
}

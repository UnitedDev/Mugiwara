package fr.uniteduhc.mugiwara.power.impl;

import fr.uniteduhc.mugiwara.game.player.MUPlayer;
import fr.uniteduhc.mugiwara.power.RightClickPower;
import fr.uniteduhc.mugiwara.roles.RolesType;
import fr.uniteduhc.mugiwara.roles.solo.KuzanRole;
import fr.uniteduhc.mugiwara.utils.config.Messages;
import fr.uniteduhc.mugiwara.utils.config.Replacement;
import fr.uniteduhc.mugiwara.utils.utils.Utils;
import fr.uniteduhc.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class HieHieLeftPower extends RightClickPower {
    public boolean rightClick() {
        return false;
    }

    public ItemStack getItem() {
        return (new ItemBuilder(Material.PACKED_ICE))
                .setName(Utils.itemFormat("&b&lHie Hie"))
                .toItemStack();
    }

    public String getName() {
        return null;
    }

    public Integer getCooldownAmount() {
        return null;
    }

    public boolean onEnable(Player player, boolean rightClick) {
        if (rightClick)
            return false;
        MUPlayer muPlayer = MUPlayer.get(player);
        RolesType.MURole muRole = muPlayer.getRole();
        KuzanRole kuzanRole = (KuzanRole)muRole;
        kuzanRole.setHieHiePowerType(KuzanRole.HieHiePowerType.getNextPower(kuzanRole.getHieHiePowerType()));
        Messages.KUZAN_HIE_SWITCH.send(player, new Replacement("<name>", kuzanRole
                .getHieHiePowerType().getName()));
        return true;
    }
}
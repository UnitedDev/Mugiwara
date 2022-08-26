package fr.uniteduhc.mugiwara.power.impl;

import fr.uniteduhc.mugiwara.game.menu.ConvSelectMenu;
import fr.uniteduhc.mugiwara.power.RightClickPower;
import fr.uniteduhc.mugiwara.roles.RolesType;
import fr.uniteduhc.mugiwara.utils.utils.Utils;
import fr.uniteduhc.utils.ChatUtil;
import fr.uniteduhc.utils.ItemBuilder;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Getter
@Setter
public class ConvSelectPower extends RightClickPower {

    private RolesType rolesType = null;

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.CHEST).setName(Utils.itemFormat("&9&lSéléctionne")).toItemStack();
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
    public boolean onEnable(Player player, boolean rightClick) {

        if(rolesType != null){
            player.sendMessage(ChatUtil.prefix("Vous avez déjà séléctionné votre Amiral"));
            return false;
        }

        new ConvSelectMenu().openMenu(player);

        return true;
    }
}

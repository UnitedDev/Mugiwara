package fr.kohei.mugiwara.power.impl;

import com.sun.org.apache.bcel.internal.generic.NEW;
import fr.kohei.mugiwara.game.menu.ConvSelectMenu;
import fr.kohei.mugiwara.power.RightClickPower;
import fr.kohei.mugiwara.roles.RolesType;
import fr.kohei.mugiwara.utils.utils.Utils;
import fr.kohei.utils.ChatUtil;
import fr.kohei.utils.ItemBuilder;
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

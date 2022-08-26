package fr.uniteduhc.mugiwara.power.impl;

import fr.uniteduhc.mugiwara.Mugiwara;
import fr.uniteduhc.mugiwara.power.RightClickPower;
import fr.uniteduhc.mugiwara.utils.config.Messages;
import fr.uniteduhc.mugiwara.utils.utils.Utils;
import fr.uniteduhc.utils.ChatUtil;
import fr.uniteduhc.utils.ItemBuilder;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Getter
@Setter
public class BlackHolePower extends RightClickPower {
    private int uses = 0;
    private boolean using;

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.NETHER_STAR).setName(Utils.itemFormat("&7&lBlack Hole")).toItemStack();
    }

    @Override
    public String getName() {
        return "Black Hole";
    }

    @Override
    public Integer getCooldownAmount() {
        return 5 * 60;
    }

    @Override
    public boolean onEnable(Player player, boolean rightClick) {
        if(uses >=3) {
            player.sendMessage(ChatUtil.prefix("&cVous avez déjà utilisé ce pouvoir 3 fois."));
            return false;
        }

        Messages.TEACH_BLACKHOLE_USE.send(player);
        using = true;
        uses++;

        Bukkit.getScheduler().runTaskLater(Mugiwara.getInstance(), () -> {
            using = false;
            Messages.TEACH_BLACKHOLE_END.send(player);
        }, 5 * 60 * 20);
        return true;
    }
}

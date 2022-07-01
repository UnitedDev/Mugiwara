package fr.kohei.mugiwara.power.impl;

import fr.kohei.mugiwara.Mugiwara;
import fr.kohei.mugiwara.power.RightClickPower;
import fr.kohei.mugiwara.utils.config.Messages;
import fr.kohei.mugiwara.utils.utils.Utils;
import fr.kohei.utils.ChatUtil;
import fr.kohei.utils.ItemBuilder;
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

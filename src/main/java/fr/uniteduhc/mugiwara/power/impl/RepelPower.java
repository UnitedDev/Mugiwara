package fr.uniteduhc.mugiwara.power.impl;

import fr.kohei.uhc.UHC;
import fr.uniteduhc.mugiwara.Mugiwara;
import fr.uniteduhc.mugiwara.game.player.MUPlayer;
import fr.uniteduhc.mugiwara.power.RightClickPower;
import fr.uniteduhc.mugiwara.utils.config.Messages;
import fr.uniteduhc.mugiwara.utils.utils.Utils;
import fr.uniteduhc.uhc.game.player.UPlayer;
import fr.uniteduhc.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class RepelPower extends RightClickPower {


    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.NETHER_STAR)
                .setName(Utils.itemFormat("&c&lRapel"))
                .toItemStack();
    }

    @Override
    public String getName() {
        return "Repel";
    }

    @Override
    public Integer getCooldownAmount() {
        return 600;
    }

    @Override
    public boolean onEnable(Player player, boolean rightClick) {

        Messages.EUSTASS_REPEL_USE.send(player);

        new BukkitRunnable() {
            int time = 60;

            @Override
            public void run() {

                if (time == 0) {
                    cancel();
                    return;
                }

                time++;

                /**
                 * TODO
                 */

            }
        }.runTaskTimer(Mugiwara.getInstance(), 0, 20L);

        return true;
    }
}

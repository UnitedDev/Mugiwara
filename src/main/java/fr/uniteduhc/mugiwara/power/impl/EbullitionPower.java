package fr.uniteduhc.mugiwara.power.impl;

import fr.uniteduhc.mugiwara.Mugiwara;
import fr.uniteduhc.mugiwara.power.RightClickPower;
import fr.uniteduhc.mugiwara.utils.utils.Utils;
import fr.uniteduhc.mugiwara.utils.utils.packets.MathUtil;
import fr.uniteduhc.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class EbullitionPower extends RightClickPower {

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.MAGMA_CREAM)
                .setName(Utils.itemFormat("&cÉbullition"))
                .toItemStack();
    }

    @Override
    public String getName() {
        return "Ébullition";
    }

    @Override
    public Integer getCooldownAmount() {
        return 300;
    }

    @Override
    public boolean onEnable(Player player, boolean rightClick) {

        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 120 * 20, 0, false, false));

        new BukkitRunnable() {
            int timer = 120;
            @Override
            public void run() {

                timer--;

                MathUtil.getSphere(player.getLocation(), 3, false).stream()
                        .filter(location -> location.getBlock().getType() == Material.WATER || location.getBlock().getType() == Material.STATIONARY_WATER)
                        .forEach(location -> location.getBlock().setType(Material.AIR));

                if(timer == 0){
                    cancel();
                }

            }

        }.runTaskTimer(Mugiwara.getInstance(), 0L, 20L);

        return true;
    }
}

package fr.kohei.mugiwara.power.impl;

import fr.kohei.mugiwara.Mugiwara;
import fr.kohei.mugiwara.power.RightClickPower;
import fr.kohei.mugiwara.utils.config.Messages;
import fr.kohei.mugiwara.utils.utils.Utils;
import fr.kohei.mugiwara.utils.utils.packets.MathUtil;
import fr.kohei.utils.ItemBuilder;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.EnumParticle;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

@Getter
public class PteranodonPower extends RightClickPower {
    private boolean using;

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.NETHER_STAR).setName(Utils.itemFormat("&4&lPtéranodon")).toItemStack();
    }

    @Override
    public String getName() {
        return "Ptéranodon";
    }

    @Override
    public Integer getCooldownAmount() {
        return 8 * 60;
    }

    @Override
    public boolean onEnable(Player player, boolean rightClick) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 2 * 20 * 60, 1, false, false));
        Messages.KING_PTERANODON_USE.send(player);
        using = true;
        Utils.getNearPlayers(player, 10).forEach(player1 -> {
            Messages.KING_PTERANODON_ONME.send(player1);
            player1.setFireTicks(50);
        });

        Bukkit.getScheduler().runTaskLater(Mugiwara.getInstance(), () -> {
            Messages.KING_PTERANODON_END.send(player);
            using = false;
        }, 2 * 20 * 60);

        new BukkitRunnable() {
            int seconds = 120;

            @Override
            public void run() {
                if(seconds == 0) cancel();

                MathUtil.sendCircleParticle(EnumParticle.FLAME, player.getLocation(), 1, 5);
                seconds--;
            }
        }.runTaskTimer(Mugiwara.getInstance(), 0, 20);

        return true;
    }
}

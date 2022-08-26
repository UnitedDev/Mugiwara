package fr.uniteduhc.mugiwara.power.impl;

import fr.uniteduhc.mugiwara.Mugiwara;
import fr.uniteduhc.mugiwara.power.RightClickPower;
import fr.uniteduhc.mugiwara.utils.config.Messages;
import fr.uniteduhc.mugiwara.utils.utils.Utils;
import fr.uniteduhc.mugiwara.utils.utils.packets.MathUtil;
import fr.uniteduhc.utils.ItemBuilder;
import fr.uniteduhc.utils.TimeUtil;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_8_R3.EnumParticle;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Set;

@Getter
@Setter
public class LazerRightPower extends RightClickPower {

    private int timer = 0;
    private boolean isUsed = false;

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.STONE_BUTTON).setName(Utils.itemFormat("&c&lLazer")).toItemStack();
    }

    @Override
    public String getName() {
        return "Lazer";
    }

    @Override
    public Integer getCooldownAmount() {
        return 300;
    }

    @Override
    public boolean onEnable(Player player, boolean rightClick) {

        if (!rightClick) return false;

        if (this.isUsed) {
            Messages.PACIFISTA_LAZER_CANT.send(player);
            return false;
        }

        this.isUsed = true;
        new BukkitRunnable() {
            @Override
            public void run() {

                if (!isUsed) {
                    cancel();
                    return;
                }

                timer++;
                Mugiwara.getInstance().addActionBar(player, "&cChargement &8» &f " + TimeUtil.getReallyNiceTime2(timer * 1000L), "chargement");
                MathUtil.sendLineParticle(EnumParticle.LAVA, player.getLocation(), player.getTargetBlock((Set<Material>) null, 30).getLocation(), 1, player);

                if (timer >= 6) {
                    timer = 0;
                    isUsed = false;
                    player.getWorld().createExplosion(player.getLocation(), 3.5F, false);
                    player.setHealth(player.getHealth() - 8);
                    Mugiwara.getInstance().removeActionBar(player, "chargement");
                    cancel();
                }

            }
        }.runTaskTimer(Mugiwara.getInstance(), 0L, 20L);

        return true;
    }

}

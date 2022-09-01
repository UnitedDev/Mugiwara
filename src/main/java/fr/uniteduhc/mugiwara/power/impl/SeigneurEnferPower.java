package fr.uniteduhc.mugiwara.power.impl;

import fr.uniteduhc.mugiwara.game.player.MUPlayer;
import fr.uniteduhc.mugiwara.power.RightClickPower;
import fr.uniteduhc.mugiwara.roles.mugiwara.ZoroRole;
import fr.uniteduhc.mugiwara.utils.config.Messages;
import fr.uniteduhc.mugiwara.utils.utils.Utils;
import fr.uniteduhc.mugiwara.utils.utils.packets.MathUtil;
import fr.uniteduhc.utils.ChatUtil;
import fr.uniteduhc.utils.ItemBuilder;
import net.minecraft.server.v1_8_R3.EnumParticle;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class SeigneurEnferPower extends RightClickPower {
    private int uses;

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.NETHER_STAR).setName(Utils.itemFormat("&5&lSeigneur des Enfers")).toItemStack();
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
        ZoroRole zoroRole = (ZoroRole) MUPlayer.get(player).getRole();
        if (zoroRole == null) return false;
        if (uses >= 3) {
            player.sendMessage(ChatUtil.prefix("&cVous avez déjà utilisé ce pouvoir 3 fois."));
            return false;
        }

        uses++;
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 60, 1, false, false));
        zoroRole.setUsedSeigneur(true);
        Messages.ZORO_SEIGNEUR_USE.send(player);

        new BukkitRunnable() {
            private int timer = 0;

            @Override
            public void run() {
                if (timer > 60) {
                    zoroRole.setUsedSeigneur(false);
                    cancel();
                    return;
                }

                MathUtil.sendCircleParticle(EnumParticle.PORTAL, player.getLocation(), 1, 10);
                timer++;
            }
        };
        return true;
    }
}

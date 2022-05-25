package fr.kohei.mugiwara.power.impl;

import fr.kohei.mugiwara.Mugiwara;
import fr.kohei.mugiwara.config.Messages;
import fr.kohei.mugiwara.game.MUPlayer;
import fr.kohei.mugiwara.power.RightClickPower;
import fr.kohei.mugiwara.roles.RolesType;
import fr.kohei.mugiwara.utils.MathUtil;
import fr.kohei.mugiwara.utils.PacketUtil;
import fr.kohei.mugiwara.utils.Utils;
import fr.kohei.uhc.UHC;
import fr.kohei.utils.ChatUtil;
import fr.kohei.utils.ItemBuilder;
import fr.kohei.utils.TimeUtil;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.EnumParticle;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
public class OSobaMaskPower extends RightClickPower {
    private boolean using;
    private boolean passed;

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.SUGAR).setName(Utils.itemFormat("O Soba Mask")).toItemStack();
    }

    @Override
    public String getName() {
        return "O Soba Mask";
    }

    @Override
    public Integer getCooldownAmount() {
        return 5 * 60;
    }

    @Override
    public boolean onEnable(Player player, boolean rightClick) {
        if (using) {
            PacketUtil.showArmor(player);
            this.using = false;
            Messages.SANJI_OSOBAMASK_DESACTIVATE.send(player);
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 60 * 20, 0, false, false));
            player.removePotionEffect(PotionEffectType.INVISIBILITY);
            return true;
        }

        if (passed) {
            player.sendMessage(ChatUtil.prefix("&cVos 15 minutes se sont écoulées"));
            return false;
        }

        Messages.SANJI_OSOBAMASK_USE.send(player);
        PacketUtil.hideArmor(player);
        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1, false, false));
        this.using = true;

        final UUID uuid = player.getUniqueId();
        new BukkitRunnable() {
            private int timer = 15 * 60;

            @Override
            public void run() {
                timer--;
                Player player = Bukkit.getPlayer(uuid);
                if (!using) {
                    Mugiwara.getInstance().removeActionBar(player, "soba");
                    return;
                }

                if (player == null) return;
                Mugiwara.getInstance().addActionBar(player, "&6Invisibilité: &7" + TimeUtil.getReallyNiceTime(timer * 1000L), "soba");

                List<Player> players = Bukkit.getOnlinePlayers().stream()
                        .filter(player1 -> UHC.getGameManager().getPlayers().contains(player1.getUniqueId()))
                        .filter(player1 -> isTargetRole(player)).collect(Collectors.toList());
                MathUtil.sendParticle(players, EnumParticle.REDSTONE, player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), 1, 0, 0, 0);

                if (timer == 0) {
                    passed = true;
                    using = true;
                    onEnable(player, true);
                }
            }
        }.runTaskTimer(Mugiwara.getInstance(), 0, 20);

        return false;
    }

    private boolean isTargetRole(Player player) {
        RolesType role = ((RolesType.MURole) MUPlayer.get(player).getRole()).getRole();

        return role == RolesType.LUFFY || role == RolesType.ZORO || role == RolesType.NAMI || role == RolesType.USSOP || role == RolesType.CHOPPER
                || role == RolesType.ROBIN || role == RolesType.FRANKY || role == RolesType.BROOK || role == RolesType.JIMBE;
    }
}

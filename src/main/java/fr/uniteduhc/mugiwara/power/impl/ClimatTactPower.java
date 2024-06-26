package fr.uniteduhc.mugiwara.power.impl;

import fr.uniteduhc.mugiwara.Mugiwara;
import fr.uniteduhc.mugiwara.game.player.MUPlayer;
import fr.uniteduhc.mugiwara.roles.RolesType;
import fr.uniteduhc.mugiwara.utils.config.Messages;
import fr.uniteduhc.mugiwara.utils.config.Replacement;
import fr.uniteduhc.mugiwara.power.RightClickPower;
import fr.uniteduhc.mugiwara.utils.utils.packets.Movement;
import fr.uniteduhc.mugiwara.utils.utils.Utils;
import fr.uniteduhc.utils.ItemBuilder;
import fr.uniteduhc.utils.ReflectionUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

public class ClimatTactPower extends RightClickPower {
    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.STICK).addEnchant(Enchantment.DAMAGE_ALL, 1).hideItemFlags()
                .setName(Utils.itemFormat("&b&lClimat-Tact")).toItemStack();
    }

    @Override
    public String getName() {
        return "Climat-Tact";
    }

    @Override
    public Integer getCooldownAmount() {
        return 5 * 60;
    }

    @Override
    public boolean onEnable(Player player, boolean rightClick) {
        Player target = Utils.getPlayers().stream()
                .filter(p -> p.getLocation().distance(player.getLocation()) <= 15)
                .filter(p -> ReflectionUtils.getLookingAt(player, p))
                .findFirst().orElse(null);
        if (target == null) return false;

        RolesType role = MUPlayer.get(target).getRole().getRole();

        if (role != RolesType.LUFFY) {
            target.setMaxHealth(target.getMaxHealth() - 6);
            Movement.freeze(target, 1);
            target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 5 * 20, 4, false, false));
            target.getWorld().strikeLightningEffect(target.getLocation());
        }

        final UUID targetUUID = target.getUniqueId();

        Bukkit.getScheduler().runTaskLater(Mugiwara.getInstance(), () -> {
            Player targetUpdated = Bukkit.getPlayer(targetUUID);
            if (targetUpdated == null) return;

            if (role != RolesType.LUFFY) {
                targetUpdated.setMaxHealth(targetUpdated.getMaxHealth() + 6);
            }
        }, 60 * 20);

        Messages.NAMI_CLIMATTACT_USE.send(player,
                new Replacement("<name>", target.getName())
        );
        Messages.NAMI_CLIMATTACT_TARGET.send(target);
        return true;
    }
}

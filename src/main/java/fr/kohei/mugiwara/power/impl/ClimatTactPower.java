package fr.kohei.mugiwara.power.impl;

import fr.kohei.mugiwara.Mugiwara;
import fr.kohei.mugiwara.config.Messages;
import fr.kohei.mugiwara.config.Replacement;
import fr.kohei.mugiwara.power.RightClickPower;
import fr.kohei.mugiwara.utils.Movement;
import fr.kohei.mugiwara.utils.Utils;
import fr.kohei.utils.ItemBuilder;
import fr.kohei.utils.ReflectionUtils;
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
        Player target = Bukkit.getOnlinePlayers().stream()
                .filter(p -> p.getLocation().distance(player.getLocation()) <= 15)
                .filter(p -> ReflectionUtils.getLookingAt(player, p))
                .findFirst().orElse(null);
        if (target == null) return false;

        target.setMaxHealth(target.getMaxHealth() - 6);
        Movement.freeze(target, 1);
        target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 5 * 20, 4, false, false));
        target.getWorld().strikeLightningEffect(target.getLocation());

        final UUID targetUUID = target.getUniqueId();

        Bukkit.getScheduler().runTaskLater(Mugiwara.getInstance(), () -> {
            Player targetUpdated = Bukkit.getPlayer(targetUUID);
            if (targetUpdated == null) return;

            targetUpdated.setMaxHealth(targetUpdated.getMaxHealth() + 6);
        }, 60 * 20);

        Messages.NAMI_CLIMATTACT_USE.send(player,
                new Replacement("<name>", target.getName())
        );
        Messages.NAMI_CLIMATTACT_TARGET.send(target);
        return true;
    }
}

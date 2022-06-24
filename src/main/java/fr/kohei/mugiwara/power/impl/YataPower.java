package fr.kohei.mugiwara.power.impl;

import fr.kohei.mugiwara.utils.config.Messages;
import fr.kohei.mugiwara.utils.config.Replacement;
import fr.kohei.mugiwara.power.RightClickPower;
import fr.kohei.mugiwara.utils.utils.Utils;
import fr.kohei.utils.ItemBuilder;
import fr.kohei.utils.ReflectionUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class YataPower extends RightClickPower {
    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.SUGAR).addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1).hideItemFlags()
                .setName(Utils.itemFormat("&b&lYata No Kagami")).toItemStack();
    }

    @Override
    public String getName() {
        return "Yata No Kagami";
    }

    @Override
    public Integer getCooldownAmount() {
        return 3 * 60;
    }

    @Override
    public boolean onEnable(Player player, boolean rightClick) {
        Player target = Bukkit.getOnlinePlayers().stream()
                .filter(p -> p.getLocation().distance(player.getLocation()) <= 50)
                .filter(p -> ReflectionUtils.getLookingAt(player, p))
                .findFirst().orElse(null);
        if (target == null) return false;

        // teleport player to target and send him the kizaru yata use message with the target name
        player.teleport(target.getLocation());
        Messages.KIZARU_YATA_USE.send(player, new Replacement("<target>", target.getName()));

        // add speed 2 for 1 minute to the player
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 60 * 20, 1));
        return true;
    }
}

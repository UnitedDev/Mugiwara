package fr.kohei.mugiwara.power.impl;

import fr.kohei.mugiwara.Mugiwara;
import fr.kohei.mugiwara.game.player.MUPlayer;
import fr.kohei.mugiwara.power.RightClickPlayerPower;
import fr.kohei.mugiwara.power.RightClickPower;
import fr.kohei.mugiwara.roles.RolesType;
import fr.kohei.mugiwara.utils.config.Messages;
import fr.kohei.mugiwara.utils.config.Replacement;
import fr.kohei.mugiwara.utils.utils.Utils;
import fr.kohei.mugiwara.utils.utils.packets.Damage;
import fr.kohei.mugiwara.utils.utils.packets.Movement;
import fr.kohei.utils.ChatUtil;
import fr.kohei.utils.Cuboid;
import fr.kohei.utils.ItemBuilder;
import fr.kohei.utils.ReflectionUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.List;

public class FlowerPower extends RightClickPower {
    private boolean used;

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.YELLOW_FLOWER).setName(Utils.itemFormat("&d&lMellow"))
                .toItemStack();
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
        if (used) {
            player.sendMessage(ChatUtil.prefix("&cVous avez déjà utilisé ce pouvoir."));
            return false;
        }

        Player target = Utils.getPlayers().stream()
                .filter(p -> p.getLocation().distance(player.getLocation()) <= 15)
                .filter(p -> ReflectionUtils.getLookingAt(player, p))
                .findFirst().orElse(null);
        if (target == null) return false;

        RolesType role = MUPlayer.get(target).getRole().getRole();
        List<RolesType> immune = Arrays.asList(RolesType.NAMI, RolesType.ROBIN, RolesType.TSURU, RolesType.BIG_MOM, RolesType.LUFFY);
        if (immune.contains(role)) {
            player.sendMessage(ChatUtil.prefix("&cCe joueur est immunisé de votre pouvoir."));
            return false;
        }

        used = true;
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 30 * 20, 2, false, false));
        Movement.freeze(target, 30);
        Damage.addNoDamage(target, 30);
        Cuboid cuboid = new Cuboid(target.getLocation().clone().add(1, 1, 1), target.getLocation().clone().add(-1, -1, -1));
        cuboid.getBlockList().forEach(block -> {
            if (block.getType() != Material.REDSTONE_BLOCK)
                block.setType(Material.STONE);
        });
        Messages.HANCOCK_AMOUR_USE.send(player, new Replacement("<name>", target.getName()));
        Messages.HANCOCK_AMOUR_TARGET.send(target);

        // task 30 seconds after
        Bukkit.getScheduler().runTaskLater(Mugiwara.getInstance(), () -> cuboid.getBlockList().forEach(block -> {
            if (block.getType() != Material.REDSTONE_BLOCK)
                block.setType(Material.AIR);
        }), 30 * 20);
        if (role == RolesType.SANJI) {
            target.setMaxHealth(target.getMaxHealth() - 4);
            Messages.HANCOCK_AMOUR_SANJI.send(target);
        }
        return true;
    }
}

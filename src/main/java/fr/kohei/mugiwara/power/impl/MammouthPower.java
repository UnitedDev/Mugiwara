package fr.kohei.mugiwara.power.impl;

import fr.kohei.mugiwara.Mugiwara;
import fr.kohei.mugiwara.utils.config.Messages;
import fr.kohei.mugiwara.power.RightClickPower;
import fr.kohei.mugiwara.utils.utils.packets.Damage;
import fr.kohei.mugiwara.utils.utils.Utils;
import fr.kohei.utils.ChatUtil;
import fr.kohei.utils.Cuboid;
import fr.kohei.utils.ItemBuilder;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@Getter
@Setter
public class MammouthPower extends RightClickPower {
    private int uses = 0;
    private boolean using;

    @Override
    public ItemStack getItem() {
        // new item builder with material bone and name "Mammouth" with the item format
        return new ItemBuilder(Material.BONE).setName(Utils.itemFormat("&6&lMammouth")).toItemStack();
    }

    @Override
    public String getName() {
        return "Mammouth";
    }

    @Override
    public Integer getCooldownAmount() {
        // 8 minutes cooldown
        return 480;
    }

    @Override
    public boolean onEnable(Player player, boolean rightClick) {
        // if the uses is more than 3, return false and send a message
        if (this.uses > 3) {
            player.sendMessage(ChatUtil.prefix("&cVous avez déjà utilisé 3 fois ce pouvoir."));
            return false;
        }

        Messages.JACK_MAMMOUTH_USE.send(player);

        // set using
        this.using = true;

        // add potion effect damage_resistance 2, slowness 2 for 2 minutes
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 2 * 20, 1, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 2 * 20, 1, false, false));

        // disable using in 2 minutes
        Bukkit.getServer().getScheduler().runTaskLater(Mugiwara.getInstance(), () -> this.using = false, 2 * 20);

        //
        Damage.addTempNoDamage(player.getUniqueId(), EntityDamageEvent.DamageCause.FALL, 10);

        // create a cuboid of 8 blocks radius
        Cuboid cuboid = new Cuboid(player.getLocation().clone().add(4, 4, 4), player.getLocation().clone().add(-4, -8, -4));
        // remove all blocks (set the material to air)
        cuboid.getBlockList().forEach(block -> {
            if (block.getType() != Material.REDSTONE_BLOCK)
                block.setType(Material.AIR);

        });

        uses++;
        return true;
    }
}

package fr.kohei.mugiwara.roles.marine;

import fr.kohei.mugiwara.power.impl.EbullitionPower;
import fr.kohei.mugiwara.power.impl.InugamiGurenPower;
import fr.kohei.mugiwara.roles.RolesType;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;

@Getter
@Setter
public class AkainuRole extends RolesType.MURole {
    private int inWater = 0;

    public AkainuRole() {
        super(Arrays.asList(
                new EbullitionPower(),
                new InugamiGurenPower()
        ));
    }

    @Override
    public RolesType getRole() {
        return RolesType.KIZARU;
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(Material.LAVA_BUCKET);
    }

    @Override
    public void onDay(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
    }

    @Override
    public void onNight(Player player) {
        player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
    }

    @Override
    public void onDistribute(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
    }

    @Override
    public void onSecond(Player player) {

        // in water
        Block block = player.getLocation().getBlock();

        if (block.getType() == Material.STATIONARY_WATER || block.getType() == Material.WATER) this.inWater++;
        else this.inWater = 0;

        if (this.inWater >= 5) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 6 * 20, 2, false, false));
            //Messages.WATER.send(player);
            this.inWater = 0;
        }
    }

}

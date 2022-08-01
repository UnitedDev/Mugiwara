package fr.kohei.mugiwara.roles.marine;

import fr.kohei.mugiwara.Mugiwara;
import fr.kohei.mugiwara.power.impl.IcePower;
import fr.kohei.mugiwara.roles.RolesType;
import fr.kohei.utils.Cuboid;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;

@Getter
@Setter
public class KuzanRole extends RolesType.MURole {
    private int inWater = 0;
    private int timer = 0;
    private int endurence = 0;
    private boolean ice;

    public KuzanRole() {
        super(Arrays.asList(
                new IcePower()
        ));
    }

    @Override
    public RolesType getRole() {
        return RolesType.KUZAN;
    }

    @Override
    public void onDistribute(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false));
        Mugiwara.knowsRole(player, RolesType.AKAINU);
        Mugiwara.knowsRole(player, RolesType.KIZARU);
    }

    @Override
    public void onSecond(Player player) {
        final Block block = player.getLocation().getBlock();

        if (block.getType() == Material.STATIONARY_WATER || block.getType() == Material.WATER) this.inWater++;
        else this.inWater = 0;

        if (this.inWater >= 5) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 6 * 20, 2, false, false));
            //Messages.WATER.send(player);
            this.inWater = 0;
        }

        timer++;

        Mugiwara.getInstance().addActionBar(player, "&cEndurance &8» &f" + endurence, "endurence");

        if (timer % 3 == 0) {
            if (endurence < 200)
                endurence++;
        }

        if (isIce()) {
            Cuboid cuboid = new Cuboid(
                    player.getLocation().clone().add(2, -1, 2),
                    player.getLocation().clone().add(-2, -2, -2)
            );
            cuboid.getBlockList().stream().filter(block1 -> block1.getType().name().contains("WATER")).forEach(block1 -> {
                block.setType(Material.PACKED_ICE);
            });
        }
    }
}

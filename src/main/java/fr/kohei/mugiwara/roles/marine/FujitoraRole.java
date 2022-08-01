package fr.kohei.mugiwara.roles.marine;

import fr.kohei.mugiwara.power.impl.HakiPower;
import fr.kohei.mugiwara.power.impl.MeteoritePower;
import fr.kohei.mugiwara.power.impl.MokoPower;
import fr.kohei.mugiwara.roles.RolesType;
import fr.kohei.mugiwara.utils.config.Messages;
import fr.kohei.mugiwara.utils.utils.Utils;
import fr.kohei.mugiwara.utils.utils.WorldUtils;
import fr.kohei.mugiwara.utils.utils.packets.Damage;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

@Getter
public class FujitoraRole extends RolesType.MURole implements Listener {
    private int inWater = 0;
    private final HashMap<UUID, Integer> avancement = new HashMap<>();

    public FujitoraRole() {
        super(Arrays.asList(
                new MokoPower(),
                new MeteoritePower(),
                new HakiPower()
        ));
    }

    @Override
    public RolesType getRole() {
        return RolesType.FUJITORA;
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(Material.STAINED_GLASS, 1, (short) 11);
    }

    @Override
    public void onDistribute(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));
    }

    @Override
    public void onSecond(Player player) {
        Block block = player.getLocation().getBlock();

        if (block.getType() == Material.STATIONARY_WATER || block.getType() == Material.WATER) this.inWater++;
        else this.inWater = 0;

        if (this.inWater >= 5) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 6 * 20, 2, false, false));
            //Messages.WATER.send(player);
            this.inWater = 0;
        }

        Utils.getNearPlayers(player, 20).forEach(player1 -> {
            int avancement = this.avancement.getOrDefault(player1.getUniqueId(), 0);
            avancement++;

            this.avancement.put(player1.getUniqueId(), avancement);
        });
    }

    private boolean used = false;
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockUpdate(EntityChangeBlockEvent event) {
        Block block = event.getBlock();
        if (!(event.getEntity() instanceof FallingBlock)) return;
        FallingBlock fallingBlock = (FallingBlock) event.getEntity();
        if (fallingBlock.getCustomName() == null) return;
        if (!fallingBlock.getCustomName().equals(MeteoritePower.MeteoresGenerator.METEORE_KEY)) return;

        if(used) return;

        block.setType(Material.AIR);
        WorldUtils.createBeautyExplosion(fallingBlock.getLocation(), 25);
        used = true;
        Utils.getNearPlayers(fallingBlock, 50).forEach(player -> {
            if (player.getUniqueId().equals(getPlayer().getUniqueId())) return;
            int distance = (int) player.getLocation().distance(fallingBlock.getLocation());
            if (distance <= 20) {
                player.damage(999999999);
            } else if (distance <= 30) {
                player.damage(1);
                if (player.getHealth() - 10 <= 0) {
                    player.damage(9999999);
                } else {
                    player.setHealth(player.getHealth() - 10);
                }
            } else if (distance <= 40) {
                player.damage(1);
                if (player.getHealth() - 6 <= 0) {
                    player.damage(9999999);
                } else {
                    player.setHealth(player.getHealth() - 6);
                }
            } else if (distance <= 50) {
                player.damage(1);
                if (player.getHealth() - 3 <= 0) {
                    player.damage(9999999);
                } else {
                    player.setHealth(player.getHealth() - 3);
                }
            }

            if (distance <= 50) {
                Damage.addTempNoDamage(player.getUniqueId(), EntityDamageEvent.DamageCause.FALL, 10);
            }
            Messages.FUJITORA_MOKO_TARGET.send(player);
        });
    }
}

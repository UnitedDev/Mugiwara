package fr.kohei.mugiwara.roles.impl.marine;

import fr.kohei.mugiwara.power.impl.FemurSwordPower;
import fr.kohei.mugiwara.power.impl.FlowerPower;
import fr.kohei.mugiwara.roles.RolesType;
import fr.kohei.mugiwara.utils.config.Messages;
import fr.kohei.mugiwara.utils.utils.Cooldown;
import fr.kohei.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;

public class HancockRole extends RolesType.MURole implements Listener {
    private final Cooldown arrowCooldown = new Cooldown("Femur (Arc)");
    private int inWater = 0;

    public HancockRole() {
        super(Arrays.asList(
                new FemurSwordPower(),
                new FlowerPower()
        ));
    }

    @Override
    public RolesType getRole() {
        return RolesType.HANCOCK;
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(Material.YELLOW_FLOWER, 1);
    }

    @Override
    public String[] getDescription() {
        return new String[0];
    }

    @Override
    public void onDistribute(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false));

        player.getInventory().addItem(new ItemBuilder(Material.BOW).addEnchant(Enchantment.ARROW_DAMAGE, 3).toItemStack());
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();
        if(!isRole(player)) return;

        if(event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            event.setCancelled(true);
        }
    }

    @Override
    public void onSecond(Player player) {
        final Block block = player.getLocation().getBlock();

        if (block.getType() == Material.STATIONARY_WATER || block.getType() == Material.WATER) this.inWater += 1;
        else this.inWater = 0;

        if (this.inWater >= 5) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 6 * 20, 2, false, false));
            Messages.WATER.send(player);
            this.inWater = 0;
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        // check if the entity is a player and the shooter is an arrow
        if (!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player)) return;
        Player player = (Player) event.getEntity();

        if(!(event.getEntity() instanceof Arrow)) return;
        Arrow arrow = (Arrow) event.getDamager();

        if(arrowCooldown.isCooldown(player)) return;

        // check if shooter is not null & player
        if(arrow.getShooter() == null || !(arrow.getShooter() instanceof Player)) return;

        Player shooter = (Player) arrow.getShooter();
        if(!isRole(shooter)) return;

        FemurSwordPower.femur(shooter, player, arrow.getLocation().getY());
        arrowCooldown.setCooldown(30);
        arrowCooldown.task();
    }
}

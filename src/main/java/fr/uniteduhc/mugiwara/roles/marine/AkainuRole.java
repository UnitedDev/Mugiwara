package fr.uniteduhc.mugiwara.roles.marine;

import fr.uniteduhc.mugiwara.power.impl.ConvPower;
import fr.uniteduhc.mugiwara.power.impl.EbullitionPower;
import fr.uniteduhc.mugiwara.power.impl.FirePower;
import fr.uniteduhc.mugiwara.power.impl.InugamiGurenPower;
import fr.uniteduhc.mugiwara.roles.RolesType;
import fr.uniteduhc.mugiwara.utils.config.Messages;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;

@Getter
@Setter
public class AkainuRole extends RolesType.MURole implements Listener {

    private int inFire = 0;
    private boolean isFire = false;

    public AkainuRole() {
        super(Arrays.asList(
                new EbullitionPower(),
                new InugamiGurenPower(),
                new FirePower(),
                new ConvPower()
        ), 0L);
    }

    @Override
    public RolesType getRole() {
        return RolesType.AKAINU;
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
    public boolean hasFruit() {
        return true;
    }

    @Override
    public void onSecond(Player player) {
        super.onSecond(player);

        if (player.getFireTicks() > 0) this.inFire++;
        else this.inFire = 0;

        if (this.inFire >= 6) {
            player.setHealth(player.getHealth() + 1);
            this.inFire = 0;
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {

        if (!(e.getEntity() instanceof Player)) return;

        Player player = (Player) e.getEntity();

        if (!isRole(player)) return;

        if(e.getDamager() instanceof Player){

            Player damager = (Player) e.getDamager();

            if(!this.isFire) return;

            damager.setFireTicks(120 * 20);

            return;
        }

        if(!(e.getDamager() instanceof Arrow)) return;

        Arrow arrow = (Arrow) e.getDamager();

        if(!(arrow.getShooter() instanceof Player && arrow.getShooter() instanceof Skeleton)) return;

        if(Math.random() > 0.50D){
            e.setCancelled(true);
            Messages.AKAINU_LOGIA_NODAMAGE.send(player);
        }

        if(!(arrow.getShooter() instanceof Player)) return;

        Player damager = (Player) arrow.getShooter();

        if(!this.isFire) return;

        damager.setFireTicks(120 * 20);

    }

}

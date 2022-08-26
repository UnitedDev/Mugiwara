package fr.uniteduhc.mugiwara.roles.marine;

import fr.uniteduhc.mugiwara.game.player.MUPlayer;
import fr.uniteduhc.mugiwara.power.impl.CrochetDamagePower;
import fr.uniteduhc.mugiwara.power.impl.GroundSeccoRightPower;
import fr.uniteduhc.mugiwara.roles.RolesType;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;

@Getter
@Setter
public class CrocodileRole extends RolesType.MURole implements Listener {

    private boolean isInWater = false;
    private int inWater = 0;

    public CrocodileRole() {
        super(Arrays.asList(
                new CrochetDamagePower(),
                new GroundSeccoRightPower()
        ), 81000000L);
    }

    @Override
    public RolesType getRole() {
        return RolesType.CROCODILE;
    }

    @Override
    public void onDay(Player player) {
        player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
    }

    @Override
    public void onNight(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));
    }

    @Override
    public void onSecond(Player player) {
        if (player.getLocation().clone().add(0, -1, 0).getBlock().getType().name().contains("SAND"))
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 4, 1, false, false));

        if (isInWater(player)) {
            this.inWater++;
        } else {
            this.isInWater = false;
            this.inWater = 0;
        }

        if (this.inWater >= 5) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 6 * 20, 0, false, false));
            //Messages.WATER.send(player);
            this.isInWater = true;
            this.inWater = 0;
        }


    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player)) return;
        if (!(e.getEntity() instanceof Player)) return;

        Player damager = (Player) e.getDamager();

        if (damager != getPlayer()) return;

        Player player = (Player) e.getEntity();

        MUPlayer muDamager = MUPlayer.get(damager);
        MUPlayer muPlayer = MUPlayer.get(player);

        if (!(muDamager.getRole() instanceof CrocodileRole)) return;

        RolesType rolesType = muPlayer.getRole().getRole();

        if (rolesType == RolesType.FRANKY || rolesType == RolesType.EUSTASS || rolesType == RolesType.KUMA || rolesType == RolesType.QUEEN || rolesType == RolesType.PACIFISTA)
            return;

        if (damager.getItemInHand().getType().name().contains("SWORD")) {
            player.setFoodLevel(player.getFoodLevel() - 1);
        }
    }

    public boolean isInWater(Player player) {
        return player.getLocation().clone().getBlock().getType().name().contains("WATER") || player.getLocation().clone().add(0, -1, 0).getBlock().getType().name().contains("WATER");
    }


}

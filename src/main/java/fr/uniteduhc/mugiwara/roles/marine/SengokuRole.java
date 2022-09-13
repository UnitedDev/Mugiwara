package fr.uniteduhc.mugiwara.roles.marine;

import fr.uniteduhc.mugiwara.game.player.MUPlayer;
import fr.uniteduhc.mugiwara.power.impl.ConvPower;
import fr.uniteduhc.mugiwara.power.impl.ConvSelectPower;
import fr.uniteduhc.mugiwara.power.impl.HitoHitoNoMiPower;
import fr.uniteduhc.mugiwara.power.impl.PrimePower;
import fr.uniteduhc.mugiwara.roles.RolesType;
import fr.uniteduhc.mugiwara.utils.config.Messages;
import fr.uniteduhc.mugiwara.utils.config.Replacement;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;

@Getter
@Setter
public class SengokuRole extends RolesType.MURole implements Listener {

    private boolean isExplose = false;

    public SengokuRole() {
        super(Arrays.asList(
                new HitoHitoNoMiPower(),
                new ConvPower(),
                new ConvSelectPower(),
                new PrimePower()
        ), 0L);
    }

    @Override
    public RolesType getRole() {
        return RolesType.SENGOKU;
    }

    @Override
    public void onDistribute(Player player) {
        //player.setWalkSpeed(0.21F);
    }

    @Override
    public boolean hasFruit() {
        return true;
    }

    @Override
    public void onSecond(Player player) {
        super.onSecond(player);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Player killer = player.getKiller();

        if (!(MUPlayer.get(player).getRole() instanceof GarpRole)) return;

        Player sengoku = getPlayer();
        if(sengoku == null) return;

        if (killer == null) {
            Messages.SENGOKU_GARPDEATH_PVE.send(sengoku);
        } else {
            Messages.SENGOKU_GARPDEATH_KILLER.send(sengoku, new Replacement("<name>", killer.getName()));
        }

    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e){
        if(!(e.getDamager() instanceof Player)) return;
        if(!(e.getEntity() instanceof Player)) return;

        Player damager = (Player) e.getDamager();

        if(!isRole(damager)) return;

        Player player = (Player) e.getEntity();

        if(!this.isExplose) return;

        player.getWorld().createExplosion(player.getLocation(), 3.5F, false);
        player.setHealth(player.getHealth() - 3);
        this.isExplose = false;

    }
}

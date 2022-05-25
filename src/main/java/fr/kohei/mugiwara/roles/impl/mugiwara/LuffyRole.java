package fr.kohei.mugiwara.roles.impl.mugiwara;

import fr.kohei.mugiwara.Mugiwara;
import fr.kohei.mugiwara.config.Messages;
import fr.kohei.mugiwara.game.MUPlayer;
import fr.kohei.mugiwara.power.impl.GearFourthPower;
import fr.kohei.mugiwara.power.impl.GomuNoMiLeftPower;
import fr.kohei.mugiwara.power.impl.GomuNoMiRightPower;
import fr.kohei.mugiwara.roles.RolesType;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;

import static fr.kohei.mugiwara.roles.RolesType.*;

public class LuffyRole extends RolesType.MURole implements Listener {
    private int inWater = 0;

    public LuffyRole() {
        super(Arrays.asList(
                new GearFourthPower(),
                new GomuNoMiRightPower(),
                new GomuNoMiLeftPower()
        ));
    }

    @Override
    public RolesType getRole() {
        return RolesType.LUFFY;
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(Material.LEATHER_HELMET);
    }

    @Override
    public String[] getDescription() {
        return new String[0];
    }

    @Override
    public void onDistribute(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));

        for (RolesType role : new RolesType[]{NAMI, USSOP, SANJI, CHOPPER, ROBIN, FRANKY, BROOK, JIMBE})
            Mugiwara.knowsRole(player, role);

    }

    @Override
    public void onSecond(Player player) {
        Block block = player.getLocation().getBlock();

        if (player.isOnGround()) {
            RolesType.MURole role = (RolesType.MURole) MUPlayer.get(player).getRole();

            GearFourthPower power = role.getPowers().stream()
                    .filter(power1 -> power1 instanceof GearFourthPower)
                    .map(power1 -> (GearFourthPower) power1)
                    .findFirst().orElse(null);

            if (power == null) return;

            if (power.isUsing()) player.setAllowFlight(true);
        }

        if (block.getType() == Material.STATIONARY_WATER || block.getType() == Material.WATER) this.inWater += 1;
        else this.inWater = 0;

        if (this.inWater >= 5) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 4 * 20, 0, false, false));
            Messages.WATER.send(player);
            this.inWater = 0;
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (!isRole((Player) event.getEntity())) return;

        event.setDamage(0.0F);
    }

    @EventHandler
    public void onFlight(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();
        if (!isRole(player)) return;

        if (!event.isFlying()) return;

        RolesType.MURole role = (RolesType.MURole) MUPlayer.get(player).getRole();

        GearFourthPower power = role.getPowers().stream()
                .filter(power1 -> power1 instanceof GearFourthPower)
                .map(power1 -> (GearFourthPower) power1)
                .findFirst().orElse(null);

        if (power == null) return;

        if (!power.isUsing()) return;

        event.setCancelled(true);
        player.setVelocity(player.getLocation().getDirection().multiply(2).setY(0.5));
    }
}

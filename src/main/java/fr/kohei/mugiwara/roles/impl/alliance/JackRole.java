package fr.kohei.mugiwara.roles.impl.alliance;

import fr.kohei.mugiwara.Mugiwara;
import fr.kohei.mugiwara.config.Messages;
import fr.kohei.mugiwara.config.Replacement;
import fr.kohei.mugiwara.power.impl.ExterminPowerLeft;
import fr.kohei.mugiwara.power.impl.ExterminPowerRight;
import fr.kohei.mugiwara.power.impl.MammouthPower;
import fr.kohei.mugiwara.roles.RolesType;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class JackRole extends RolesType.MURole {
    // variable called inwater
    private int inWater;
    private Block first;
    private Block second;
    private Block third;
    private List<UUID> playersInZone = new ArrayList<>();

    public JackRole() {
        super(Arrays.asList(
                new MammouthPower(),
                new ExterminPowerRight(),
                new ExterminPowerLeft()
        ));
    }

    @Override
    public RolesType getRole() {
        return RolesType.JACK;
    }

    @Override
    public ItemStack getItem() {
        // TODO DEMANDE C QUOI LITEM
        return new ItemStack(Material.GOLDEN_APPLE);
    }

    @Override
    public String[] getDescription() {
        return new String[0];
    }

    @Override
    public void onSecond(Player player) {
        Block block = player.getLocation().getBlock();

        if (block.getType() == Material.STATIONARY_WATER || block.getType() == Material.WATER) this.inWater += 1;
        else this.inWater = 0;

        if (this.inWater >= 5) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 6 * 20, 2, false, false));
            Messages.WATER.send(player);
            this.inWater = 0;
        }
    }

    @Override
    public void onDistribute(Player player) {
        // add increase damage potion effect
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));

        // send the role of Kaido, Queen and King
        Mugiwara.knowsRole(player, RolesType.KING);
        Mugiwara.knowsRole(player, RolesType.QUEEN);
        Mugiwara.knowsRole(player, RolesType.KAIDO);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        // if damager or entity is not player return
        if (!(event.getDamager() instanceof Player)) return;
        if (!(event.getEntity() instanceof Player)) return;

        // get damager and entity
        Player damager = (Player) event.getDamager();
        Player entity = (Player) event.getEntity();

        // if damage is not role return
        if (!isRole(damager)) return;

        MammouthPower power = this.getPowers().stream()
                .filter(power1 -> power1 instanceof MammouthPower)
                .map(power1 -> (MammouthPower) power1)
                .findFirst().orElse(null);
        // if power is null or is not using return
        if (power == null || !power.isUsing()) return;

        // 50% chance condition
        if (Math.random() > 0.5) {
            entity.setVelocity(damager.getLocation().getDirection().multiply(5).setY(2));
            Messages.JACK_MAMMOUTH_TARGET.send(entity, new Replacement("<name>", damager.getName()));
        }
    }

    @Override
    public void onEpisode(Player player) {
        // loop all the players in and get the player
        for (UUID uuid : this.playersInZone) {
            Player target = Bukkit.getPlayer(uuid);
            if (target == null) continue;

            // two random numbers between 1 and 600
            int random = (int) (Math.random() * 600) + 1;
            int random2 = 600 + ((int) (Math.random() * 600) + 1);
            // run a task random seconds after
            Bukkit.getScheduler().runTaskLater(Mugiwara.getInstance(), () -> {
                Player newTarget = Bukkit.getPlayer(uuid);
                if (newTarget == null || !newTarget.isOnline()) return;

                poison(newTarget);
            }, random);
            Bukkit.getScheduler().runTaskLater(Mugiwara.getInstance(), () -> {
                Player newTarget = Bukkit.getPlayer(uuid);
                if (newTarget == null || !newTarget.isOnline()) return;

                poison(newTarget);
            }, random2);
        }
    }

    private void poison(Player target) {
        target.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 15 * 20, 0, false, false));
        target.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 15 * 20, 2, false, false));

        Messages.JACK_EXTERMIN_POISON_TARGET.send(target);
    }
}

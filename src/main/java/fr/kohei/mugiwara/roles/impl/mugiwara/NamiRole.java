package fr.kohei.mugiwara.roles.impl.mugiwara;

import fr.kohei.mugiwara.Mugiwara;
import fr.kohei.mugiwara.camp.CampType;
import fr.kohei.mugiwara.config.Messages;
import fr.kohei.mugiwara.game.MUPlayer;
import fr.kohei.mugiwara.power.impl.ClimatTactPower;
import fr.kohei.mugiwara.power.impl.PisterPower;
import fr.kohei.mugiwara.power.impl.VolPower;
import fr.kohei.mugiwara.power.impl.ZeusPower;
import fr.kohei.mugiwara.roles.RolesType;
import fr.kohei.uhc.UHC;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
public class NamiRole extends RolesType.MURole {
    private final List<UUID> seenPlayers = new ArrayList<>();

    public NamiRole() {
        super(Arrays.asList(
                new ClimatTactPower(),
                new PisterPower(),
                new VolPower(),
                new ZeusPower()
        ));
    }

    @Override
    public RolesType getRole() {
        return RolesType.NAMI;
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(Material.STICK);
    }

    @Override
    public String[] getDescription() {
        return new String[0];
    }

    @Override
    public void onDistribute(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, Integer.MAX_VALUE, 0, false, false));
        Mugiwara.knowsRole(player, RolesType.LUFFY);

        int random = (int) (Math.random() * 3);
        if (random == 1) ZoroRole.randomRole(player);
    }

    @Override
    public void onSecond(Player player) {
        for (Player target : Bukkit.getOnlinePlayers().stream()
                .filter(p -> p.getLocation().distance(player.getLocation()) <= 15)
                .filter(p -> !seenPlayers.contains(p.getUniqueId()))
                .filter(p -> p.getGameMode() == GameMode.SURVIVAL)
                .filter(p -> UHC.getGameManager().getPlayers().contains(p.getUniqueId()))
                .collect(Collectors.toList())) {
            seenPlayers.add(target.getUniqueId());
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        if (!isRole(player)) return;
        if (!(event.getCause() == EntityDamageEvent.DamageCause.PROJECTILE
                || event.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK)) return;

        ZeusPower power = this.getPowers().stream().filter(power1 -> power1 instanceof ZeusPower)
                .map(power1 -> (ZeusPower) power1).findFirst().orElse(null);
        if (power == null) return;

        if (power.isUsingPower()) {
            int random = (int) (Math.random() * 5);
            if (random != 2) return;

            for (Player target : player.getNearbyEntities(25, 25, 25).stream()
                    .filter(entity -> entity instanceof Player)
                    .map(entity -> (Player) entity)
                    .collect(Collectors.toList())) {
                RolesType type = ((RolesType.MURole) MUPlayer.get(target).getRole()).getRole();

                if (type == RolesType.LUFFY || type == RolesType.KIZARU) continue;
                if (type.getCampType() == CampType.MUGIWARA_HEART) continue;

                Messages.NAMI_ZEUS_LIGHTNING.send(player);
                target.getWorld().strikeLightning(target.getLocation());
                target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 25, 4, false, false));
            }
        }
    }
}

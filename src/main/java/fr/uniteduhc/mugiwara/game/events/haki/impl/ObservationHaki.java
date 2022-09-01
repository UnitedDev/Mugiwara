package fr.uniteduhc.mugiwara.game.events.haki.impl;

import fr.uniteduhc.mugiwara.Mugiwara;
import fr.uniteduhc.mugiwara.game.events.DemonFruitManager;
import fr.uniteduhc.mugiwara.game.events.HakiManager;
import fr.uniteduhc.mugiwara.game.events.haki.AbstractHaki;
import fr.uniteduhc.mugiwara.game.events.haki.HakiType;
import fr.uniteduhc.mugiwara.game.player.MUPlayer;
import fr.uniteduhc.mugiwara.roles.RolesType;
import fr.uniteduhc.mugiwara.utils.config.Messages;
import fr.uniteduhc.mugiwara.utils.config.Replacement;
import fr.uniteduhc.mugiwara.utils.utils.Cooldown;
import fr.uniteduhc.mugiwara.utils.utils.Utils;
import fr.uniteduhc.mugiwara.utils.utils.packets.MathUtil;
import net.minecraft.server.v1_8_R3.EnumParticle;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ObservationHaki extends AbstractHaki implements Listener {
    private final Cooldown cooldown = new Cooldown("Haki de l'Observation");
    private boolean using = false;
    private int uses = 1;

    @Override
    public HakiType getHakiType() {
        return HakiType.OBSERVATION;
    }

    public ObservationHaki() {
        List<RolesType> demonFruitRoles = new ArrayList<>();
        demonFruitRoles.addAll(DemonFruitManager.LOGIA);
        demonFruitRoles.addAll(DemonFruitManager.PARAMECIA);
        demonFruitRoles.addAll(DemonFruitManager.ZOAN);
        Bukkit.getScheduler().runTaskTimer(Mugiwara.getInstance(), () -> {
            Utils.getPlayers().stream()
                    .filter(player -> MUPlayer.get(player).getRole() != null)
                    .filter(player -> demonFruitRoles.contains(MUPlayer.get(player).getRole().getRole()))
                    .forEach(this::checkWater);
        }, 0, 20);
    }

    private void checkWater(Player player) {
        Block block = player.getLocation().getBlock();
        if (!block.getType().name().contains("WATER")) return;

        if (!using) return;

        using = false;
        Messages.HAKI_WATER_DISABLED.send(player);
    }

    @Override
    public void onRightClick(Player player) {
        if (cooldown.isCooldown(player)) return;

        using = true;

        cooldown.setCooldown(uses * 5 * 60);
        cooldown.task();
        List<RolesType> cooldownDoesntChange = Arrays.asList(RolesType.LUFFY, RolesType.FUJITORA, RolesType.KATAKURI, RolesType.KAIDO);
        RolesType role = MUPlayer.get(player).getRole().getRole();
        if (!cooldownDoesntChange.contains(role)) uses++;
        Messages.HAKI_POWER_USE.send(player, new Replacement("<haki>", getHakiType().getDisplayName()));

        Bukkit.getScheduler().runTaskLater(Mugiwara.getInstance(), () -> {
            using = false;
            Messages.HAKI_POWER_DISABLED.send(player, new Replacement("<haki>", getHakiType().getDisplayName()));
        }, 5 * 20);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();

        if (!hasHaki(player)) return;
        if (!using) return;

        if (event.getDamager() instanceof Projectile) {
            event.setCancelled(true);
            MathUtil.sendCircleParticle(EnumParticle.REDSTONE, player.getLocation(), 1, 10);
            return;
        }

        if (!(event.getDamager() instanceof Player)) return;

        Player damager = (Player) event.getDamager();

        Location playerLocation = player.getLocation();
        Location damagerLocation = damager.getLocation();

        Vector swap = damagerLocation.toVector()
                .subtract(playerLocation.toVector()).normalize();
        Location target = damagerLocation.clone().add(swap);
        MathUtil.sendCircleParticle(EnumParticle.REDSTONE, player.getLocation(), 1, 10);
    }
}

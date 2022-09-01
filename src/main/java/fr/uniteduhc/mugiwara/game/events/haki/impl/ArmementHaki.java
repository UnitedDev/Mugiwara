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
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ArmementHaki extends AbstractHaki implements Listener {
    private final Cooldown cooldown = new Cooldown("Haki de l'Armement");
    private boolean using = false;
    private int uses = 1;

    public ArmementHaki() {
        Bukkit.getServer().getPluginManager().registerEvents(this, Mugiwara.getInstance());

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
    public HakiType getHakiType() {
        return HakiType.ARMEMENT;
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();

        if (!using) return;
        if (!hasHaki(player)) return;

        event.setDamage(event.getDamage() * 0.90);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getDamager();

        if (!using) return;
        if (!hasHaki(player)) return;

        event.setDamage(event.getDamage() * 1.05);
    }

    @Override
    public void onRightClick(Player player) {
        if (cooldown.isCooldown(player)) return;

        using = true;
        Messages.HAKI_POWER_USE.send(player, new Replacement("<haki>", this.getHakiType().getDisplayName()));
        cooldown.setCooldown(uses * 5 * 60);
        cooldown.task();
        uses++;

        final UUID uuid = player.getUniqueId();
        new BukkitRunnable() {
            int timer = 5 * 60;

            @Override
            public void run() {
                if (timer <= 0) {
                    using = false;
                    Messages.HAKI_POWER_DISABLED.send(player, new Replacement("<haki>", getHakiType().getDisplayName()));
                    cancel();
                    return;
                }

                Player player = Bukkit.getPlayer(uuid);
                if (player == null) return;

                timer--;
                MathUtil.sendCircleParticle(EnumParticle.PORTAL, player.getLocation(), 1, 10);
            }
        }.runTaskTimer(Mugiwara.getInstance(), 0, 20);
    }
}

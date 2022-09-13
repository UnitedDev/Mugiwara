package fr.uniteduhc.mugiwara.roles.mugiwara;

import fr.uniteduhc.mugiwara.Mugiwara;
import fr.uniteduhc.mugiwara.power.impl.*;
import fr.uniteduhc.mugiwara.power.impl.*;
import fr.uniteduhc.mugiwara.utils.config.Messages;
import fr.uniteduhc.mugiwara.roles.RolesType;
import fr.uniteduhc.mugiwara.utils.utils.Cooldown;
import fr.uniteduhc.mugiwara.utils.utils.Utils;
import fr.uniteduhc.mugiwara.utils.utils.packets.MathUtil;
import fr.uniteduhc.mugiwara.utils.utils.arrow.BowAimbot;
import fr.uniteduhc.utils.ChatUtil;
import fr.uniteduhc.utils.ReflectionUtils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_8_R3.EnumParticle;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;

public class LawRole extends RolesType.MURole implements Listener {
    private final Cooldown healCooldown = new Cooldown("Heal");
    private int healUses = 0;

    public LawRole() {
        super(Arrays.asList(
                new HealPower(),
                new MesPower(),
                new RoomPower(),
                new ShamblesRightPower(),
                new ShamblesLeftPower()
        ), 500000000L);
    }

    @Override
    public RolesType getRole() {
        return RolesType.LAW;
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(Material.SHEARS);
    }

    @Override
    public void onDistribute(Player player) {
        Mugiwara.knowsRole(player, RolesType.LUFFY);

        player.getInventory().addItem(new ItemStack(Material.POTION, 2, (short) 16460));
        player.getInventory().addItem(new ItemStack(Material.POTION, 1, (short) 8193));
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
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();
        Player law = getPlayer();

        if (law == null) return;
        if (law.getLocation().distance(player.getLocation()) > 50) return;
        if (player.getHealth() > 8) return;

        HealPower healPower = (HealPower) this.getPowers().stream().filter(power -> power instanceof HealPower).findFirst().orElse(null);
        if (healPower == null) return;
        if (healPower.getUses() >= 2) return;

        law.sendMessage(ChatUtil.prefix("&c" + player.getName() + " &fest à moins de &c4 coeurs&f."));
        TextComponent text = new TextComponent(ChatUtil.translate("&a&l[SOIGNER]"));
        text.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mu heal " + player.getName()));
        law.spigot().sendMessage(text);
    }

    @Override
    public void onDeath(Player player, Player killer) {
        MesPower mesPower = (MesPower) this.getPowers().stream().filter(power -> power instanceof MesPower).findFirst().orElse(null);
        if (mesPower == null) return;
        if (mesPower.getTarget() == null) return;

        Player target = Bukkit.getPlayer(mesPower.getTarget());
        if (target == null) return;

        mesPower.setTarget(null);
        Messages.LAW_MES_DEATH.send(target);
        target.setMaxHealth(target.getMaxHealth() + 6);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        RoomPower shamblesPower = (RoomPower) this.getPowers().stream().filter(power -> power instanceof RoomPower).findFirst().orElse(null);
        if (shamblesPower == null) return;
        if (shamblesPower.getCenter() == null) return;

        Player player = event.getPlayer();

        if (shamblesPower.getInZone().contains(player.getUniqueId()) && player.getLocation().distance(shamblesPower.getCenter()) > 32) {
            player.teleport(shamblesPower.getCenter());
        }

        if (!shamblesPower.getInZone().contains(player.getUniqueId()) && player.getLocation().distance(shamblesPower.getCenter()) <= 32) {
            shamblesPower.getInZone().add(player.getUniqueId());
            BowAimbot.addTracker(player, shamblesPower.getTimer());
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!isRole(player)) return;

        Player target = Utils.getPlayers().stream()
                .filter(p -> p.getLocation().distance(player.getLocation()) <= 5)
                .filter(p -> ReflectionUtils.getLookingAt(player, p))
                .findFirst().orElse(null);
        if (target == null) return;

        if(event.getAction().name().contains("LEFT")) return;

        if (healCooldown.isCooldown(player)) return;

        if (healUses >= 2) {
            return;
        }

        target.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 2 * 20, 4, false, false));

        healUses++;
        healCooldown.setCooldown(3);
        healCooldown.task();

        new BukkitRunnable() {
            int timer = 20;

            @Override
            public void run() {
                if (timer == 0) {
                    cancel();
                    return;
                }

                MathUtil.sendCircleParticle(EnumParticle.HEART, target.getLocation(), 2, 10);
                timer--;
            }
        }.runTaskTimer(Mugiwara.getInstance(), 0, 2);
    }
}

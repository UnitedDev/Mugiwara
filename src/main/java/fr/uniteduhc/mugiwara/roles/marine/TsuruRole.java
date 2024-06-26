package fr.uniteduhc.mugiwara.roles.marine;

import fr.uniteduhc.mugiwara.game.player.MUPlayer;
import fr.uniteduhc.mugiwara.power.impl.WoshiPower;
import fr.uniteduhc.mugiwara.roles.RolesType;
import fr.uniteduhc.mugiwara.utils.config.Messages;
import fr.uniteduhc.mugiwara.utils.config.Replacement;
import fr.uniteduhc.mugiwara.utils.utils.Cooldown;
import fr.uniteduhc.mugiwara.utils.utils.Utils;
import fr.uniteduhc.mugiwara.utils.utils.packets.Damage;
import fr.uniteduhc.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TsuruRole extends RolesType.MURole implements Listener {

    private final List<Egg> eggs = new ArrayList<>();
    private final Cooldown eggCooldown = new Cooldown("Woshu");

    public TsuruRole() {
        super(Arrays.asList(
                new WoshiPower()
        ), 0L);
    }

    @Override
    public RolesType getRole() {
        return RolesType.TSURU;
    }

    @Override
    public ItemStack getItem() {
            return new ItemStack(Material.SUGAR);
    }

    @Override
    public boolean hasFruit() {
        return true;
    }

    @Override
    public void onSecond(Player player) {
        super.onSecond(player);
    }

    @Override
    public void onDistribute(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false));

        player.getInventory().addItem(new ItemBuilder(Material.EGG, 2).setName(Utils.itemFormat("&6&lWoshu")).toItemStack());
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Player tsuru = getPlayer();

        if (tsuru == null) return;
        if (player.getKiller() == null) return;
        Player killer = player.getKiller();

        RolesType role = MUPlayer.get(player).getRole().getRole();
        if (!(role == RolesType.SENGOKU || role == RolesType.GARP)) return;

        Messages.TSURU_KILLER_COORDINATES.send(tsuru,
                new Replacement("<role>", role.getName()),
                new Replacement("<x>", killer.getLocation().getBlockX()),
                new Replacement("<y>", killer.getLocation().getBlockY()),
                new Replacement("<z>", killer.getLocation().getBlockZ())
        );
    }

    @EventHandler
    public void onShoot(ProjectileLaunchEvent event) {
        if (!(event.getEntity() instanceof Egg)) return;
        if (!(event.getEntity().getShooter() instanceof Player)) return;

        Player player = (Player) event.getEntity().getShooter();
        Egg egg = (Egg) event.getEntity();

        if (!isRole(player)) return;

        if (eggCooldown.isCooldown(player)) {
            event.setCancelled(true);
            return;
        }

        eggCooldown.setCooldown(3 * 60);
        eggCooldown.task();

        eggs.add(egg);
        player.setItemInHand(new ItemBuilder(player.getItemInHand()).setAmount(player.getItemInHand().getAmount() + 1).toItemStack());
        Messages.TSURU_WHOSHU_USE.send(player);
    }

    @EventHandler
    public void onHit(ProjectileHitEvent event) {
        if (!(event.getEntity() instanceof Egg)) return;
        if (!(event.getEntity().getShooter() instanceof Player)) return;

        Player player = (Player) event.getEntity().getShooter();
        Egg egg = (Egg) event.getEntity();

        if (!isRole(player)) return;
        if (!eggs.contains(egg)) return;

        eggs.remove(egg);

        Utils.getNearPlayers(egg, 30).forEach(nearPlayer -> {
            if (nearPlayer == player) return;
            Messages.TSURU_WHOSHU_HIT.send(nearPlayer);

            Damage.addCantDamageTemp(nearPlayer, 10);
            nearPlayer.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 10 * 20, 3, false, false));
        });
    }
}

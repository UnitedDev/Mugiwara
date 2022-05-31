package fr.kohei.mugiwara.roles.impl.mugiwara;

import fr.kohei.mugiwara.Mugiwara;
import fr.kohei.mugiwara.config.Messages;
import fr.kohei.mugiwara.config.Replacement;
import fr.kohei.mugiwara.roles.RolesType;
import fr.kohei.mugiwara.utils.Cooldown;
import fr.kohei.mugiwara.utils.Movement;
import fr.kohei.mugiwara.utils.Utils;
import fr.kohei.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UssopRole extends RolesType.MURole implements Listener {
    private final List<Arrow> arrows = new ArrayList<>();
    private ItemStack bow = null;
    private final Cooldown arrowCooldown = new Cooldown("Kabuto");

    public UssopRole() {
        super(Arrays.asList(

        ));
    }

    @Override
    public RolesType getRole() {
        return RolesType.USSOP;
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(Material.BOW);
    }

    @Override
    public String[] getDescription() {
        return new String[0];
    }

    @Override
    public void onDistribute(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, Integer.MAX_VALUE, 0, false, false));
        player.getInventory().addItem(this.bow =
                new ItemBuilder(Material.BOW).setInfinityDurability().setName(Utils.notClickItem("&a&lKabuto")).addEnchant(Enchantment.ARROW_DAMAGE, 1).toItemStack()
        );
        player.getInventory().addItem(new ItemStack(Material.ARROW, 64));

        Mugiwara.knowsRole(player, RolesType.LUFFY);
        int random = (int) (Math.random() * 3);
        if (random == 1) ZoroRole.randomRole(player);
    }

    @Override
    public void onSecond(Player player) {
        if (player.getHealth() <= 10) {
            player.removePotionEffect(PotionEffectType.SPEED);
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 2 * 20, 1, false, false));
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (!(event.getDamager() instanceof Arrow)) return;

        Player player = (Player) event.getEntity();
        Arrow damager = (Arrow) event.getDamager();
        Player shooter = (Player) damager.getShooter();

        if (!isRole(shooter)) return;

        if (!arrows.contains(damager)) return;

        this.tryAllEffects(player, shooter);

        arrows.remove(damager);
    }

    private void tryAllEffects(Player player, Player shooter) {
        int random = (int) (Math.random() * 5);

        if (this.arrowCooldown.isCooldown(player)) return;

        if (random == 0)
            this.tryPoison(player, shooter);
        else if (random == 1)
            this.tryFire(player, shooter);
        else if (random == 2)
            this.tryStunt(player, shooter);
        else if (random == 3)
            this.tryExplosion(player, shooter);
        else
            this.tryBlindness(player, shooter);
        this.arrowCooldown.setCooldown(10);
    }

    private void tryPoison(Player player, Player damager) {
        Messages.USSOP_KABUTO_POISON_USE.send(damager,
                new Replacement("<name>", player.getName())
        );
        Messages.USSOP_KABUTO_POISON_ONME.send(player);
        player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 12 * 20, 0, false, false));
    }

    private void tryFire(Player player, Player damager) {
        Messages.USSOP_KABUTO_FIRE_USE.send(damager,
                new Replacement("<name>", player.getName())
        );
        Messages.USSOP_KABUTO_FIRE_ONME.send(player);
        player.setFireTicks(30);
    }

    private void tryStunt(Player player, Player damager) {
        Messages.USSOP_KABUTO_STUN_USE.send(damager,
                new Replacement("<name>", player.getName())
        );
        Messages.USSOP_KABUTO_STUN_ONME.send(player);
        Movement.freeze(player, 3);
    }

    private void tryExplosion(Player player, Player damager) {
        Messages.USSOP_KABUTO_EXPLOSION_USE.send(damager,
                new Replacement("<name>", player.getName())
        );
        Messages.USSOP_KABUTO_EXPLOSION_ONME.send(player);
        player.getWorld().createExplosion(player.getLocation(), 2f);
    }

    private void tryBlindness(Player player, Player damager) {
        Messages.USSOP_KABUTO_AVEUGLE_USE.send(damager,
                new Replacement("<name>", player.getName())
        );
        Messages.USSOP_KABUTO_AVEUGLE_ONME.send(player);
        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 8 * 20, 1, false, false));
    }

    @EventHandler
    public void onShoot(ProjectileLaunchEvent event) {
        if (!(event.getEntity() instanceof Arrow)) return;

        Arrow damager = (Arrow) event.getEntity();
        Player shooter = (Player) damager.getShooter();

        if (!isRole(shooter)) return;

        ItemStack item = shooter.getItemInHand();
        if (!item.hasItemMeta()) return;
        if (!item.getItemMeta().hasDisplayName()) return;

        if (item.getItemMeta().getDisplayName().equals(bow.getItemMeta().getDisplayName())) {
            this.arrows.add(damager);
        }
    }
}

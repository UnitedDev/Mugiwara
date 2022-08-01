package fr.kohei.mugiwara.roles.solo;

import fr.kohei.mugiwara.Mugiwara;
import fr.kohei.mugiwara.game.player.MUPlayer;
import fr.kohei.mugiwara.power.impl.BigMomVivreCardPower;
import fr.kohei.mugiwara.power.impl.PrometheePower;
import fr.kohei.mugiwara.power.impl.SoulPocusPower;
import fr.kohei.mugiwara.power.impl.ZeusPower;
import fr.kohei.mugiwara.roles.RolesType;
import fr.kohei.mugiwara.utils.config.Messages;
import fr.kohei.mugiwara.utils.config.Replacement;
import fr.kohei.mugiwara.utils.utils.Utils;
import fr.kohei.mugiwara.utils.utils.packets.Damage;
import fr.kohei.mugiwara.utils.utils.packets.MathUtil;
import fr.kohei.uhc.utils.LocationUtils;
import fr.kohei.utils.ItemBuilder;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_8_R3.EnumParticle;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.UUID;

@Getter
@Setter
public class BigMomRole extends RolesType.MURole implements Listener {
    int i = 0;
    boolean was = false;
    public static final ItemBuilder BIGMOM_VIVE_CARD = new ItemBuilder(Material.PAPER).setName(Utils.notClickItem("&d&lLinlin Card"));
    private int timer = 0;

    private int targetChoice = -1;
    private int bigMomChoice = -1;

    private UUID cakeTarget = null;

    public BigMomRole() {
        super(Arrays.asList(
                new BigMomVivreCardPower(),
                new SoulPocusPower(),
                new PrometheePower()
        ));
    }

    @Override
    public RolesType getRole() {
        return RolesType.BIG_MOM;
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(Material.CAKE);
    }

    @Override
    public void onDistribute(Player player) {
        player.setMaxHealth(26D);
        Mugiwara.knowsRole(player, RolesType.KATAKURI);
        Mugiwara.knowsRole(player, RolesType.KAIDO);

        player.getInventory().addItem(new ItemBuilder(Material.IRON_SWORD).setDurability(235)
                .addEnchant(Enchantment.DAMAGE_ALL, 5).setName(Utils.notClickItem("Napoléon")).toItemStack());
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (!(event.getDamager() instanceof Player)) return;

        Player player = (Player) event.getEntity();
        Player damager = (Player) event.getDamager();

        if (!isRole(damager)) return;

        PrometheePower power = (PrometheePower) getPowers().stream()
                .filter(p -> p instanceof PrometheePower)
                .findFirst()
                .orElse(null);
        if (power == null) return;
        if (!power.isUsing()) return;

        // 33% chance of enflamming the player
        if (Math.random() < 0.33) {
            player.setFireTicks(20 * 5);
        }
    }

    @Override
    public void onKill(Player death, Player killer) {
        for (ItemStack content : killer.getInventory().getContents()) {
            if (content.getType() != Material.IRON_SWORD) continue;
            if (!content.hasItemMeta()) continue;
            if (!content.getItemMeta().getDisplayName().contains("Napoléon")) continue;

            int first = killer.getInventory().first(content);
            ItemStack is = content;
            is.setDurability((short) (is.getDurability() - 10));

            killer.getInventory().setItem(first, is);
        }
    }

    @EventHandler
    public void onNapoleon(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (!(event.getDamager() instanceof Player)) return;

        Player damager = (Player) event.getDamager();

        if (!isRole(damager)) return;

        ItemStack item = damager.getInventory().getItemInHand();
        if (item == null) return;
        if (!item.getType().equals(Material.IRON_SWORD)) return;
        if (!item.hasItemMeta()) return;

        if (!item.getItemMeta().hasDisplayName()) return;
        if (!item.getItemMeta().getDisplayName().contains("Napoléon")) return;

        if (item.getDurability() >= 245) event.setCancelled(true);
    }

    public void cakeEnable(Player player, int choice) {
        if (getPlayer().getUniqueId().equals(player.getUniqueId())) {
            bigMomChoice = choice;
        } else {
            targetChoice = choice;
        }

        Player target = Bukkit.getPlayer(cakeTarget);
        Player bigmom = getPlayer();
        Damage.CANNOT_DAMAGE.remove(target.getUniqueId());
        Damage.CANNOT_DAMAGE.remove(player.getUniqueId());
        Damage.NO_DAMAGE.remove(target.getUniqueId());
        Damage.NO_DAMAGE.remove(player.getUniqueId());

        setCakeTarget(null);

        if (bigMomChoice != -1 && targetChoice != -1) {
            if (bigMomChoice == targetChoice) {
                Messages.BIGMOM_SOULEND_TARGET.send(player);
                Messages.BIGMOM_SOULEND_BIGMOM.send(bigmom, new Replacement("<name>", target.getName()));

                int health = (int) (target.getMaxHealth() - 10);
                target.setMaxHealth(target.getMaxHealth() - 10);

                final UUID targetUUID = target.getUniqueId();
                new BukkitRunnable() {
                    int all = health;

                    @Override
                    public void run() {
                        if (all == 0) {
                            cancel();
                            return;
                        }

                        Player player = Bukkit.getPlayer(targetUUID);
                        player.setMaxHealth(player.getMaxHealth() + 2);
                        all--;
                    }
                }.runTaskTimer(Mugiwara.getInstance(), 0, 4 * 20 * 60);
            }
        }
    }

    @Override
    public void onDeath(Player player, Player killer) {
        for (Player player1 : Utils.getPlayers()) {
            if(player1.getInventory().contains(BIGMOM_VIVE_CARD.toItemStack())) {
                player1.getInventory().remove(BIGMOM_VIVE_CARD.toItemStack());
                Messages.VIVECARD_BIGMOM_DEATH.send(player1);
            }
        }
    }

    @Override
    public void onSecond(Player player) {
        for (Player player1 : Utils.getPlayers()) {
            if (player1.getItemInHand().isSimilar(BIGMOM_VIVE_CARD.toItemStack())) {
                Player bigmom = getPlayer();
                if (bigmom == null) continue;

                String distance = new DecimalFormat("#.#").format(player1.getLocation().distance(bigmom.getLocation()));
                String arrow = LocationUtils.getArrow(player1.getLocation(), bigmom.getLocation());

                String targetName = "&cBigMom";
                String display = "&6" + targetName + " &7" + arrow + "&f " + distance;
                Mugiwara.getInstance().addActionBar(player1, display, "bigmom_vive_card");
            } else {
                Mugiwara.getInstance().removeActionBar(player1, "bigmom_vive_card");
            }
        }

        if (player.getFoodLevel() >= 10) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20, 0, false, false));
        } else if (player.getFoodLevel() >= 5.5) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20, 0, false, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20, 0, false, false));
        } else if (player.getFoodLevel() >= 4.5) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20, 0, false, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20, 0, false, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20, 0, false, false));
        } else if (player.getFoodLevel() >= 0) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20, 0, false, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20, 1, false, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20, 4, false, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 20, 1, false, false));
        }

        if (player.getLocation().getBlock().getType() != Material.WATER) {
            i = 0;

            if (was) {
                was = false;
                player.removePotionEffect(PotionEffectType.CONFUSION);
            }
        } else {
            was = true;
            if (i > 4) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, Integer.MAX_VALUE, 0, false, false));
            }
        }
    }

    @EventHandler
    public void onPlayerEat(PlayerItemConsumeEvent event) {
        if (event.getItem().getType().isEdible()) {
            final Player player = event.getPlayer();
            player.setFoodLevel(player.getFoodLevel() + 3);
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if (event.getItemDrop().getItemStack().isSimilar(BIGMOM_VIVE_CARD.toItemStack())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        MUPlayer muPlayer = MUPlayer.get(player);

        if (muPlayer.getRole().getRole() != RolesType.NAMI) return;

        Location deathLocation = player.getLocation();
        new BukkitRunnable() {
            @Override
            public void run() {
                Player bigMom = getPlayer();
                if (bigMom == null) return;

                if (bigMom.getLocation().distance(deathLocation) <= 10) {
                    Mugiwara.getInstance().addActionBar(bigMom, "&cChargement &8» &f" + timer + "&7/&f60", "death_nami");

                    if (timer >= 60) {
                        MUPlayer.get(bigMom).getRole().getPowers().add(new ZeusPower());
                        Messages.BIGMOM_NAMI_ZEUS.send(bigMom);
                        cancel();
                        return;
                    }
                } else {
                    Mugiwara.getInstance().removeActionBar(bigMom, "death_nami");
                }

                for (int i = 1; i < 10; i++)
                    MathUtil.sendCircleParticle(EnumParticle.CLOUD, deathLocation, i, 5, bigMom);
            }
        }.runTaskTimer(Mugiwara.getInstance(), 0, 10);
    }

    @EventHandler
    public void onVivreCard(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();
        if (!isRole(player)) return;
        if (player.getHealth() >= 8) return;

        for (Player player1 : Utils.getPlayers()) {
            if (player1.getInventory().contains(BIGMOM_VIVE_CARD.toItemStack())) {
                Messages.VIVECARD_BIGMOM_HEALTH.send(player1);
            }
        }
    }
}

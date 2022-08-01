package fr.kohei.mugiwara.roles.solo;

import fr.kohei.mugiwara.Mugiwara;
import fr.kohei.mugiwara.camp.CampType;
import fr.kohei.mugiwara.game.player.MUPlayer;
import fr.kohei.mugiwara.power.impl.*;
import fr.kohei.mugiwara.roles.RolesType;
import fr.kohei.mugiwara.roles.marine.AkainuRole;
import fr.kohei.mugiwara.utils.config.Messages;
import fr.kohei.mugiwara.utils.utils.Cooldown;
import fr.kohei.mugiwara.utils.utils.Utils;
import fr.kohei.mugiwara.utils.utils.packets.Damage;
import fr.kohei.mugiwara.utils.utils.packets.Movement;
import fr.kohei.mugiwara.utils.utils.packets.PlayerUtils;
import fr.kohei.uhc.game.player.UPlayer;
import fr.kohei.utils.ChatUtil;
import fr.kohei.utils.TimeUtil;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static fr.kohei.mugiwara.utils.utils.packets.Damage.CANNOT_DAMAGE;
import static fr.kohei.mugiwara.utils.utils.packets.Damage.NO_DAMAGE;

@Getter
@Setter
public class SaboRole extends RolesType.MURole implements Listener {
    private int inWater = 0;
    private final Cooldown arrowCooldown = new Cooldown("Feu");
    private boolean fire;
    private Pacte pacte;
    private boolean akainu;
    private int nextToKuma = 1;
    private boolean pacte3;
    private UUID kumaDead;

    public SaboRole() {
        super(Arrays.asList(
                new FireTogglePower(),
                new ChoosePactePower(),
                new HealLuffyPower(),
                new BlazeRodPower(),
                new DuoPower()
        ));
    }

    @Override
    public RolesType getRole() {
        return RolesType.SABO;
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(Material.BLAZE_POWDER);
    }

    @Override
    public void onDistribute(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        // if the damager or the entity is not player return
        if (!(event.getDamager() instanceof Player)) return;
        if (!(event.getEntity() instanceof Player)) return;

        Player damager = (Player) event.getDamager();
        Player entity = (Player) event.getEntity();

        if (!isRole(damager)) return;
        if (!fire) return;

        fire(entity, damager);

    }

    @EventHandler
    public void onArrow(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Arrow)) return;
        if (!(event.getEntity() instanceof Player)) return;

        Arrow arrow = (Arrow) event.getDamager();

        if (!(arrow.getShooter() instanceof Player)) return;
        Player attacker = (Player) arrow.getShooter();
        Player entity = (Player) event.getEntity();

        if (!isRole(attacker)) return;

        fire(entity, attacker);

        if (!arrowCooldown.isCooldownNoMessage(attacker)) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (arrow.isDead() || arrow.isOnGround()) {
                        cancel();
                        return;
                    }

                    Utils.getNearPlayers(arrow, 3).forEach(player -> player.setFireTicks(20 * 5));
                }
            }.runTaskTimer(Mugiwara.getInstance(), 0, 5);
            arrowCooldown.setCooldown(30);
            arrowCooldown.task();
        }
    }

    @Override
    public void onSecond(Player player) {
        Block block = player.getLocation().getBlock();

        player.removePotionEffect(PotionEffectType.POISON);

        if (block.getType() == Material.STATIONARY_WATER || block.getType() == Material.WATER) this.inWater++;
        else this.inWater = 0;

        if (this.inWater >= 5) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 6 * 20, 2, false, false));
            //Messages.WATER.send(player);
            this.inWater = 0;
        }

        if (pacte == Pacte.ONE) {
            Player luffy = Mugiwara.findRole(RolesType.LUFFY);
            if (luffy != null && luffy.getLocation().distance(player.getLocation()) <= 25) {
                luffy.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 5 * 20, 0, false, false));
            }

            if (luffy != null) {
                String display = "&cLuffy &8» &f" + ((int) luffy.getHealth()) + "&c❤";
                Mugiwara.getInstance().addActionBar(player, display, "luffy_life");
            } else {
                Mugiwara.getInstance().removeActionBar(player, "luffy_life");
            }
        }

        if (pacte == Pacte.TWO) {
            Player akainu = Mugiwara.findRole(RolesType.AKAINU);
            if (akainu != null && akainu.getLocation().distance(player.getLocation()) <= 15) {
                akainu.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 5 * 20, 0, false, false));
            }
        }

        if (pacte == Pacte.THREE) {
            Player kuma = Mugiwara.findRole(RolesType.KUMA);
            if (kuma != null && kuma.getLocation().distance(player.getLocation()) <= 50 && !pacte3) {
                nextToKuma++;
                Mugiwara.getInstance().addActionBar(player, "&cKuma &8» &f" + TimeUtil.getReallyNiceTime2(nextToKuma * 1000L) + "&7/&f10m00", "kuma_distance");
                if (nextToKuma >= 10 * 60) {
                    pacte3 = true;
                    Messages.SABO_PACTE_SABO.send(player);
                    Messages.SABO_PACTE_SABO.send(kuma);
                    UPlayer.get(kuma).setCamp(CampType.SABO_KUMA.getCamp());
                    UPlayer.get(player).setCamp(CampType.SABO_KUMA.getCamp());
                    PlayerUtils.makePlayerSeePlayersHealthAboveHead(kuma);
                    PlayerUtils.makePlayerSeePlayersHealthAboveHead(player);
                }
            }

            if (kuma != null && kuma.getLocation().distance(player.getLocation()) <= 50 && pacte3) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 2 * 20, 0, false, false));
            }
        }

    }

    @Override
    public void onKill(Player death, Player killer) {
        if (pacte != null && pacte == Pacte.TWO) {
            killer.setMaxHealth(killer.getMaxHealth() + 1);

            if (MUPlayer.get(death).getRole() instanceof AkainuRole) {
                Mugiwara.knowsRole(killer, RolesType.KIZARU);
                Mugiwara.knowsRole(killer, RolesType.FUJITORA);

                killer.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false));
                killer.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));
                killer.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
                Damage.addTempNoDamage(killer, EntityDamageEvent.DamageCause.FALL, 60 * 60 * 60);
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();

        if (MUPlayer.get(player).getRole().getRole() != RolesType.KUMA) return;
        Player sabo = getPlayer();
        if (sabo == null) return;

        if (player.getLocation().distance(sabo.getLocation()) >= 50) return;
        if (event.getDamage() >= player.getHealth()) return;

        if (kumaDead != null && kumaDead.equals(player.getUniqueId())) {
            event.setCancelled(true);
            return;
        }

        Damage.addCantDamageTemp(player, 60);
        Damage.addNoDamage(player, 60);
        kumaDead = player.getUniqueId();
        event.setCancelled(true);
        player.setHealth(1);
        Movement.freeze(player, 60);
        new BukkitRunnable() {
            int seconds = 60;

            @Override
            public void run() {
                if (kumaDead == null) {
                    cancel();
                    return;
                }

                if (seconds == 0) {
                    player.setHealth(0);
                }
                seconds--;
            }
        }.runTaskTimer(Mugiwara.getInstance(), 0, 20);

    }

    @EventHandler
    public void onDamageBySabo(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (!(event.getDamager() instanceof Player)) return;

        Player player = (Player) event.getEntity();
        Player damager = (Player) event.getDamager();

        if (!isRole(damager)) return;
        if (MUPlayer.get(player).getRole().getRole() != RolesType.KUMA) return;
        if (kumaDead == null || !kumaDead.equals(player.getUniqueId())) return;

        kumaDead = null;
        Movement.unfreeze(player);
        player.setHealth(10);
        event.setCancelled(true);
        CANNOT_DAMAGE.remove(player.getUniqueId());
        NO_DAMAGE.remove(player.getUniqueId());
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (MUPlayer.get(player).getRole().getRole() == RolesType.AKAINU) {
            Player sabo = getPlayer();
            if (sabo == null) return;

            Messages.SABO_PACTE_AKAINUDEATH.send(sabo);
            sabo.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
            sabo.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));
        }

        if (kumaDead != null) {
            Bukkit.getPlayer(kumaDead).setHealth(0);
            kumaDead = null;
        }
    }

    private void fire(Player player, Player damager) {
        if (Math.random() < 0.5) {
            player.setFireTicks(20 * 5);
        }
    }

    public void onPacteSelection(Player player) {
        player.closeInventory();
        if (pacte == Pacte.ONE) {
            Mugiwara.knowsRole(player, RolesType.LUFFY);
        }
    }

    @Override
    public void onDay(Player player) {
        if (pacte != null && (pacte == Pacte.ONE || pacte == Pacte.TWO)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));
        }
    }

    @Override
    public void onNight(Player player) {
        if (!akainu && pacte != null && (pacte == Pacte.ONE || pacte == Pacte.TWO)) {
            player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
        }
    }

    private boolean cd;

    @EventHandler
    public void onLuffyDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;
        if (!(event.getEntity() instanceof Player)) return;

        Player damager = (Player) event.getDamager();
        Player entity = (Player) event.getEntity();

        Player luffy = Mugiwara.findRole(RolesType.LUFFY);
        if (luffy == null) return;

        if (!(luffy.getUniqueId() == entity.getUniqueId() || luffy.getUniqueId() == damager.getUniqueId())) return;

        Player sabo = getPlayer();
        if (sabo == null) return;

        if (!cd) {
            cd = true;
            new BukkitRunnable() {
                @Override
                public void run() {
                    cd = false;
                }
            }.runTaskLater(Mugiwara.getInstance(), 200);
        }
        if (!cd)
            Messages.SABO_PACTE_LUFFYCOMBAT.send(sabo);
        sabo.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 30 * 20, 0, false, false));

        if (luffy.getUniqueId() == entity.getUniqueId() && entity.getHealth() <= 10) {
            sabo.sendMessage(ChatUtil.prefix("&cLuffy est à moins de 5 coeurs."));
            TextComponent textComponent = new TextComponent(ChatUtil.translate("&a&l[SE TELEPORTER]"));
            textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mu tptoluffy " + entity.getName()));
            sabo.spigot().sendMessage(textComponent);
        }
    }

    @Getter
    public enum Pacte {
        ONE(Messages.SABO_PACTE_ONE_NAME.getDisplay(), Messages.SABO_PACTE_ONE_MATERIAL.getDisplay(),
                Messages.SABO_PACTE_ONE_LORE.getDisplay(), Messages.SABO_PACTE_ONE_SELECT),
        TWO(Messages.SABO_PACTE_TWO_NAME.getDisplay(), Messages.SABO_PACTE_TWO_MATERIAL.getDisplay(),
                Messages.SABO_PACTE_TWO_LORE.getDisplay(), Messages.SABO_PACTE_TWO_SELECT),
        THREE(Messages.SABO_PACTE_THREE_NAME.getDisplay(), Messages.SABO_PACTE_THREE_MATERIAL.getDisplay(),
                Messages.SABO_PACTE_THREE_LORE.getDisplay(), Messages.SABO_PACTE_THREE_SELECT);

        private final String display;
        private final Material material;
        private final List<String> lore;
        private final Messages select;

        Pacte(String display, String material, String lore, Messages select) {
            this.display = display;
            this.material = Material.getMaterial(material);
            this.lore = Arrays.asList(lore.split("\n"));
            this.select = select;
        }
    }
}
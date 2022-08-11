package fr.kohei.mugiwara.roles.alliance;

import fr.kohei.mugiwara.Mugiwara;
import fr.kohei.mugiwara.camp.CampType;
import fr.kohei.mugiwara.camp.impl.MarineCamp;
import fr.kohei.mugiwara.camp.impl.MugiwaraHeartCamp;
import fr.kohei.mugiwara.camp.impl.SaboKumaCamp;
import fr.kohei.mugiwara.utils.config.Messages;
import fr.kohei.uhc.game.player.UPlayer;
import fr.kohei.mugiwara.power.impl.BoroPower;
import fr.kohei.mugiwara.power.impl.SbirePower;
import fr.kohei.mugiwara.power.impl.UoPower;
import fr.kohei.mugiwara.roles.RolesType;
import fr.kohei.mugiwara.roles.RolesType.MURole;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;

public class KaidoRole extends RolesType.MURole implements Listener {
    public int i = 0;
    public boolean was = false;
    public boolean damage = false;
    public boolean nofall = false;
    public boolean nauseaToPlayer = false;
    public Player sbirePlayer = null;
    public int progression = 0;

    public KaidoRole() {
        super(Arrays.asList(
                new UoPower(),
                new SbirePower(),
                new BoroPower()
        ));
    }

    @Override
    public RolesType getRole() {
        return RolesType.KAIDO;
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(Material.STONE_AXE);
    }

    @Override
    public void onDistribute(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
        Mugiwara.knowsRole(player, RolesType.BIG_MOM);
        Mugiwara.knowsRole(player, RolesType.QUEEN);
        Mugiwara.knowsRole(player, RolesType.KING);
        Mugiwara.knowsRole(player, RolesType.JACK);

        player.setWalkSpeed(0.21F);
    }

    @Override
    public void onDay(Player player) {
        player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
    }

    @Override
    public void onNight(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));
    }

    @Override
    public double getStrengthBuffer() {
        return 1.05F;
    }

    @Override
    public double getResistanceBuffer() {
        return 0.95F;
    }

    @Override
    public void onSecond(Player player) {
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

        if (sbirePlayer != null && progression <= 3000) {
            final double distance = sbirePlayer.getLocation().distance(player.getLocation());
            if (distance <= 1) {
                progression += 10;
            } else if (distance <= 4) {
                progression += 5;
            } else if (distance <= 10) {
                progression += 3;
            } else if (distance <= 15) {
                progression += 1;
            }
        }

        if (progression >= 3000) {
            if (sbirePlayer == null) return;
            final UPlayer up = UPlayer.get(sbirePlayer);
            final MURole muRole = (RolesType.MURole) up.getRole();
            Messages.KAIDO_SBIRE_3K_REACHED.send(player);

            if (muRole.getRole() == RolesType.SABO ||
                    muRole.getRole() == RolesType.TEACH ||
                    muRole.getRole() == RolesType.LAW ||
                    muRole.getRole() == RolesType.EUSTASS ||
                    muRole.getRole() == RolesType.LUFFY ||
                    muRole.getRole() == RolesType.ZORO ||
                    muRole.getRole() == RolesType.AKAINU ||
                    muRole.getRole() == RolesType.KIZARU ||
                    muRole.getRole() == RolesType.SENGOKU ||
                    muRole.getRole() == RolesType.GARP ||
                    muRole.getRole() == RolesType.TSURU ||
                    muRole.getRole() == RolesType.FUJITORA)
                return;

            if (up.getCamp() instanceof MarineCamp) {
                if (up.getCamp() instanceof SaboKumaCamp) return;
                up.setCamp(CampType.BIGMOM_KAIDO.getCamp());
                Messages.KAIDO_SBIRE_TRANSFORMED.send(sbirePlayer);
            } else if (up.getCamp() instanceof MugiwaraHeartCamp) {
                if (Math.random() > 0.5) {
                    up.setCamp(CampType.BIGMOM_KAIDO.getCamp());
                    Messages.KAIDO_SBIRE_TRANSFORMED.send(sbirePlayer);
                }
            }

            /*if (muRole.getRole() == RolesType.SOLDAT) {
                ((SoldatRole) muRole).onTransformation(player, sbirePlayer);
            }*/

            sbirePlayer = null;
            progression = 0;
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getEntityType() != EntityType.PLAYER ||
                event.getDamager().getType() != EntityType.PLAYER ||
                !isRole((Player) event.getDamager())) {
            return;
        }

        final Player player = (Player) event.getEntity();
        if (damage && Math.random() > 0.20) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 12 * 20, 1, false, false));
        }

        if (nauseaToPlayer) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 10 * 20, 1, false, false));
        }
    }

    @EventHandler
    public void onFallDamage(EntityDamageEvent event) {
        if (event.getEntityType() != EntityType.PLAYER ||
                !isRole((Player) event.getEntity())
                || event.getCause() != DamageCause.FALL
                || !nofall) {
            return;
        }

        event.setCancelled(true);
    }
}
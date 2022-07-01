package fr.kohei.mugiwara.roles.impl.solo;

import fr.kohei.mugiwara.Mugiwara;
import fr.kohei.mugiwara.power.impl.BigMomVivreCardPower;
import fr.kohei.mugiwara.roles.RolesType;
import fr.kohei.mugiwara.utils.config.Messages;
import fr.kohei.mugiwara.utils.utils.Utils;
import fr.kohei.uhc.utils.LocationUtils;
import fr.kohei.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.text.DecimalFormat;
import java.util.Arrays;

public class BigMomRole extends RolesType.MURole implements Listener {
    int i = 0;
    boolean was = false;
    public static final ItemBuilder BIGMOM_VIVE_CARD = new ItemBuilder(Material.PAPER).setName(Utils.notClickItem("&d&lLinlin Card"));

    public BigMomRole() {
        super(Arrays.asList(
                new BigMomVivreCardPower()
        ));
    }

    @Override
    public RolesType getRole() {
        return RolesType.BIG_MOM;
    }

    @Override
    public String[] getDescription() {
        return null;
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(Material.CAKE);
    }

    @Override
    public void onDistribute(Player player) {
        player.setHealth(26D);
        Mugiwara.knowsRole(player, RolesType.KATAKURI);
        Mugiwara.knowsRole(player, RolesType.KAIDO);
    }

    @Override
    public void onDeath(Player player, Player killer) {
        for (Player player1 : Utils.getPlayers()) {
            player1.getInventory().remove(BIGMOM_VIVE_CARD.toItemStack());
            Messages.VIVECARD_BIGMOM_DEATH.send(player1);
        }
    }

    @Override
    public void onSecond(Player player) {
        Bukkit.broadcastMessage("0");
        for (Player player1 : Utils.getPlayers()) {
            if(player1.getItemInHand().isSimilar(BIGMOM_VIVE_CARD.toItemStack())) {
                Player bigmom = getPlayer();
                if(bigmom == null) continue;

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
    public void onVivreCard(EntityDamageEvent event) {
        if(!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();
        if(!isRole(player)) return;
        if(player.getHealth() >= 8) return;

        for (Player player1 : Utils.getPlayers()) {
            if(player1.getInventory().contains(BIGMOM_VIVE_CARD.toItemStack())) {
                Messages.VIVECARD_BIGMOM_HEALTH.send(player1);
            }
        }
    }
}

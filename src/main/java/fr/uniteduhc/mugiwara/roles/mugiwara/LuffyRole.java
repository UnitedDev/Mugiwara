package fr.uniteduhc.mugiwara.roles.mugiwara;

import fr.uniteduhc.mugiwara.Mugiwara;
import fr.uniteduhc.mugiwara.power.impl.VivreCardPower;
import fr.uniteduhc.mugiwara.utils.config.Messages;
import fr.uniteduhc.mugiwara.game.player.MUPlayer;
import fr.uniteduhc.mugiwara.power.impl.GearFourthPower;
import fr.uniteduhc.mugiwara.power.impl.GomuNoMiLeftPower;
import fr.uniteduhc.mugiwara.power.impl.GomuNoMiRightPower;
import fr.uniteduhc.mugiwara.roles.RolesType;
import fr.uniteduhc.mugiwara.utils.utils.Utils;
import fr.uniteduhc.uhc.utils.LocationUtils;
import fr.uniteduhc.utils.ChatUtil;
import fr.uniteduhc.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static fr.uniteduhc.mugiwara.roles.RolesType.*;

public class LuffyRole extends RolesType.MURole implements Listener {
    public static final ItemBuilder LUFFY_VIVE_CARD = new ItemBuilder(Material.PAPER).setName(Utils.notClickItem("&c&lLuffy Card"));

    public LuffyRole() {
        super(Arrays.asList(
                new GearFourthPower(),
                new GomuNoMiRightPower(),
                new GomuNoMiLeftPower(),
                new VivreCardPower()
        ), 1500000000L);
    }

    @Override
    public RolesType getRole() {
        return RolesType.LUFFY;
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(Material.LEATHER_HELMET);
    }

    @Override
    public void onDistribute(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));

        List<String> names = new ArrayList<>();
        for (RolesType role : new RolesType[]{NAMI, USSOP, SANJI, CHOPPER, ROBIN, FRANKY, BROOK, JIMBE}) {
            Player playerRole = Mugiwara.findRole(role);
            if(playerRole != null) names.add(playerRole.getName());
        }

        StringBuilder builder = new StringBuilder();
        names.forEach(s -> {
            if (builder.toString().equalsIgnoreCase("")) {
                builder.append(s);
            } else {
                builder.append(", " + s);
            }
        });
        player.sendMessage(ChatUtil.prefix("&cListe &fdes pseudos: " + builder));
    }

    @Override
    public boolean hasFruit() {
        return true;
    }

    @Override
    public void onSecond(Player player) {
        super.onSecond(player);

        for (Player player1 : Utils.getPlayers()) {
            if (player1.getItemInHand().isSimilar(LUFFY_VIVE_CARD.toItemStack())) {
                Player luffy = getPlayer();
                if (luffy == null) continue;

                String distance = new DecimalFormat("#.#").format(player1.getLocation().distance(luffy.getLocation()));
                String arrow = LocationUtils.getArrow(player1.getLocation(), luffy.getLocation());

                String targetName = "&cLuffy";
                String display = "&6" + targetName + " &7" + arrow + "&f " + distance;
                Mugiwara.getInstance().addActionBar(player1, display, "luffy_vive_card");
            } else {
                Mugiwara.getInstance().removeActionBar(player1, "luffy_vive_card");
            }
        }

        if (player.isOnGround() || player.getLocation().clone().add(0, -1, 0).getBlock().getType() != Material.AIR) {
            RolesType.MURole role = MUPlayer.get(player).getRole();

            GearFourthPower power = role.getPowers().stream()
                    .filter(power1 -> power1 instanceof GearFourthPower)
                    .map(power1 -> (GearFourthPower) power1)
                    .findFirst().orElse(null);

            if (power == null) return;

            player.setAllowFlight(power.isUsing());
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (!isRole((Player) event.getEntity())) return;

        if (event.getCause() == EntityDamageEvent.DamageCause.FALL)
            event.setDamage(0.0F);
    }

    @EventHandler
    public void onFlight(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();
        if (!isRole(player)) return;

        if (!event.isFlying()) return;

        RolesType.MURole role = MUPlayer.get(player).getRole();

        GearFourthPower power = role.getPowers().stream()
                .filter(power1 -> power1 instanceof GearFourthPower)
                .map(power1 -> (GearFourthPower) power1)
                .findFirst().orElse(null);

        if (power == null) return;

        if (!power.isUsing()) return;

        event.setCancelled(true);
        player.setAllowFlight(false);
        player.setVelocity(player.getLocation().getDirection().multiply(2).setY(0.5));
    }

    @Override
    public void onDeath(Player player, Player killer) {
        for (Player player1 : Utils.getPlayers()) {
            if(player1.getInventory().contains(LUFFY_VIVE_CARD.toItemStack())) {
                player1.getInventory().remove(LUFFY_VIVE_CARD.toItemStack());
                Messages.VIVECARD_LUFFY_DEATH.send(player1);
            }
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if (event.getItemDrop().getItemStack().isSimilar(LUFFY_VIVE_CARD.toItemStack())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onVivreCard(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();
        if (!isRole(player)) return;
        if (player.getHealth() >= 10) return;

        for (Player player1 : Utils.getPlayers()) {
            if (player1.getInventory().contains(LUFFY_VIVE_CARD.toItemStack())) {
                Messages.VIVECARD_LUFFY_HEALTH.send(player1);
            }
        }
    }
}

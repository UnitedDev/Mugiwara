package fr.kohei.mugiwara.roles.impl.mugiwara;

import fr.kohei.mugiwara.Mugiwara;
import fr.kohei.mugiwara.utils.config.Messages;
import fr.kohei.mugiwara.power.impl.GeneralFrankyPower;
import fr.kohei.mugiwara.power.impl.VisionPower;
import fr.kohei.mugiwara.roles.RolesType;
import fr.kohei.mugiwara.utils.utils.Utils;
import fr.kohei.uhc.UHC;
import fr.kohei.utils.ItemBuilder;
import fr.kohei.utils.Title;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
public class FrankyRole extends RolesType.MURole implements Listener {
    private final List<UUID> visions = new ArrayList<>();
    private int bouclierUses = 0;

    public FrankyRole() {
        super(Arrays.asList(
                new VisionPower(),
                new GeneralFrankyPower()
        ));
    }

    @Override
    public RolesType getRole() {
        return RolesType.FRANKY;
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(Material.IRON_BLOCK);
    }

    @Override
    public String[] getDescription() {
        return new String[0];
    }

    @Override
    public void onDistribute(Player player) {
        player.setMaxHealth(player.getMaxHealth() + 4);
        player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, Integer.MAX_VALUE, 0, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
        player.getInventory().addItem(new ItemBuilder(Material.DIAMOND_PICKAXE).addEnchant(Enchantment.DIG_SPEED, 4).toItemStack());

        int random = (int) (Math.random() * 3);
        if (random == 1) ZoroRole.randomRole(player);
    }

    @Override
    public void onSecond(Player player) {
        StringBuilder stringBuilder = new StringBuilder();
        for (UUID vision : visions) {
            Player target = Bukkit.getPlayer(vision);
            if (vision == null) continue;

            DecimalFormat format = new DecimalFormat("#.#");
            String life = "&c" + format.format(target.getHealth()) + " ❤";

            String display = "&7" + target.getName() + " " + life;
            stringBuilder.append(display).append(" ");
        }

        Mugiwara.getInstance().addActionBar(player, stringBuilder.toString(), "vision");
    }

    @Override
    public void onDeath(Player player, Player killer) {
        final Location location = player.getLocation();
        new BukkitRunnable() {
            private int timer = 5;

            @Override
            public void run() {
                List<Player> nearPlayers = Bukkit.getOnlinePlayers().stream()
                        .filter(player1 -> UHC.getGameManager().getPlayers().contains(player1.getUniqueId()))
                        .filter(player1 -> player1.getLocation().distance(location) <= 15)
                        .collect(Collectors.toList());
                if (timer == 0) {
                    nearPlayers.forEach(player1 -> {
                        if (player1.getHealth() <= 10) {
                            player1.setHealth(0.5);
                        } else {
                            player1.setHealth(player1.getHealth() - 10);
                        }
                    });
                    cancel();
                    return;
                }

                nearPlayers.forEach(player1 -> Title.sendTitle(player1, 0, 20, 0, "&c" + timer));
                timer--;
            }
        }.runTaskTimer(Mugiwara.getInstance(), 0, 20);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();
        if (!isRole(player)) return;

        if (bouclierUses > 0) {
            event.setDamage(0.0F);
            bouclierUses--;
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        if (!isRole(event.getPlayer())) return;
        GeneralFrankyPower power = (GeneralFrankyPower) getPowers().stream()
                .filter(power1 -> power1 instanceof GeneralFrankyPower)
                .findFirst().orElse(null);

        if (power == null) return;

        if (!(power.getPower() == GeneralPowers.TNT && event.getItemInHand().isSimilar(GeneralPowers.TNT.getToReplace().toItemStack())))
            return;

        event.setCancelled(true);
        replaceItem(power, event.getPlayer());
        tntPower(event.getPlayer());
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (!isRole(event.getPlayer())) return;
        GeneralFrankyPower power = (GeneralFrankyPower) getPowers().stream()
                .filter(power1 -> power1 instanceof GeneralFrankyPower)
                .findFirst().orElse(null);

        if (power == null) return;
        if (event.getItem() == null) return;

        if (power.getPower() == GeneralPowers.ASHIMOTO && event.getItem().isSimilar(GeneralPowers.ASHIMOTO.getToReplace().toItemStack())) {
            replaceItem(power, event.getPlayer());
            ashimotoPower(event.getPlayer());
        }

        if (power.getPower() == GeneralPowers.BOUCLIER && event.getItem().isSimilar(GeneralPowers.BOUCLIER.getToReplace().toItemStack())) {
            replaceItem(power, event.getPlayer());
            bouclierPower(event.getPlayer());
        }

    }

    private void replaceItem(GeneralFrankyPower power, Player player) {
        player.setItemInHand(power.getItem());
    }

    private void tntPower(Player player) {
        for (final Player target : Utils.getNearPlayers(player, 5)) {
            target.teleport(target.getLocation().add(new Location(target.getWorld(), 0.5, -5.0, 0.5)));
        }
        Messages.FRANKY_GENERAL_NEWPOWER_TNT_USE.send(player);
        player.getWorld().createExplosion(player.getLocation(), 3.0f);
    }

    private void bouclierPower(Player player) {
        this.bouclierUses = 10;
        Messages.FRANKY_GENERAL_NEWPOWER_BOUCLIER_USE.send(player);
    }

    private void ashimotoPower(Player player) {
        new BukkitRunnable() {
            private int time = 10;

            public void run() {
                if (this.time <= 0) {
                    this.cancel();
                    return;
                }
                this.time--;
                if (player == null || !player.isOnline()) {
                    return;
                }
                final Location loc = player.getLocation();
                loc.setYaw(loc.getYaw() + 180.0f);
                player.teleport(loc);
                for (final Player proche : Utils.getNearPlayers(player, 10)) {
                    proche.setHealth(proche.getHealth() - 1);
                }
            }
        }.runTaskTimer(Mugiwara.getInstance(), 10L, 10L);
        Messages.FRANKY_GENERAL_NEWPOWER_ASHIMOTO_USE.send(player);
    }

    @Getter
    @RequiredArgsConstructor
    public enum GeneralPowers {
        TNT(Messages.FRANKY_GENERAL_NEWPOWER_TNT_GET,
                new ItemBuilder(Material.TNT).setName(Utils.itemFormat("TNT"))
        ),
        ASHIMOTO(Messages.FRANKY_GENERAL_NEWPOWER_ASHIMOTO_GET,
                new ItemBuilder(Material.GOLD_SWORD).setInfinityDurability().setName(Utils.itemFormat("Général Ashimoto Dangereux"))
        ),
        BOUCLIER(Messages.FRANKY_GENERAL_NEWPOWER_BOUCLIER_GET,
                new ItemBuilder(Material.INK_SACK).setDurability(4).setName(Utils.itemFormat("Teinture grise"))
        );

        private final Messages messages;
        private final ItemBuilder toReplace;
    }
}

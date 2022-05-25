package fr.kohei.mugiwara.roles.impl.mugiwara;

import fr.kohei.mugiwara.Mugiwara;
import fr.kohei.mugiwara.power.impl.GeneralFrankyPower;
import fr.kohei.mugiwara.power.impl.VisionPower;
import fr.kohei.mugiwara.roles.RolesType;
import fr.kohei.uhc.UHC;
import fr.kohei.utils.ItemBuilder;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
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
public class FrankyRole extends RolesType.MURole {
    private final List<UUID> visions = new ArrayList<>();

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

                nearPlayers.forEach(player1 -> {
                    
                });
                timer--;
            }
        }.runTaskTimer(Mugiwara.getInstance(), 0, 20);
    }
}

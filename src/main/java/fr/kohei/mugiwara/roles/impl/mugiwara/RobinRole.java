package fr.kohei.mugiwara.roles.impl.mugiwara;

import fr.kohei.mugiwara.Mugiwara;
import fr.kohei.mugiwara.utils.config.Messages;
import fr.kohei.mugiwara.utils.config.Replacement;
import fr.kohei.mugiwara.power.impl.ClutchPower;
import fr.kohei.mugiwara.power.impl.OeilsPower;
import fr.kohei.mugiwara.power.impl.VoirInvPower;
import fr.kohei.mugiwara.roles.RolesType;
import fr.kohei.mugiwara.utils.utils.packets.PlayerUtils;
import fr.kohei.uhc.UHC;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;
import java.util.stream.Collectors;

@Getter
public class RobinRole extends RolesType.MURole implements Listener {
    private int inWater = 0;
    private final List<Block> blocks = new ArrayList<>();
    private final HashMap<Block, List<UUID>> inZone = new HashMap<>();

    public RobinRole() {
        super(Arrays.asList(
                new VoirInvPower(),
                new OeilsPower(),
                new ClutchPower()
        ));
    }

    @Override
    public RolesType getRole() {
        return RolesType.ROBIN;
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(Material.BOOK);
    }

    @Override
    public String[] getDescription() {
        return new String[0];
    }

    @Override
    public void onDistribute(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 1, false, false));
        Mugiwara.knowsRole(player, RolesType.LUFFY);

        int random = (int) (Math.random() * 3);
        if (random == 1) ZoroRole.randomRole(player);
    }

    @Override
    public void onSecond(Player player) {
        Block block = player.getLocation().getBlock();

        if (block.getType() == Material.STATIONARY_WATER || block.getType() == Material.WATER) this.inWater += 1;
        else this.inWater = 0;

        if (this.inWater >= 5) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 6 * 20, 2, false, false));
            Messages.WATER.send(player);
            this.inWater = 0;
        }

        blocks.forEach(block1 -> {
            for (Player player1 : Bukkit.getOnlinePlayers().stream()
                    .filter(player1 -> UHC.getGameManager().getPlayers().contains(player1.getUniqueId()))
                    .collect(Collectors.toList())) {
                if (!inZone.get(block1).contains(player1.getUniqueId()) && player1.getLocation().distance(block1.getLocation()) <= 25) {
                    Messages.ROBIN_OEIL_JOIN.send(player, new Replacement("<name>", player1.getName()));

                    List<UUID> list = inZone.get(block1);
                    list.add(player1.getUniqueId());
                    inZone.put(block1, list);

                    if (player1.getUniqueId().equals(player.getUniqueId())) {
                        PlayerUtils.makePlayerSeePlayersHealthAboveHead(player1);
                    }
                } else if (inZone.get(block1).contains(player1.getUniqueId()) && player1.getLocation().distance(block1.getLocation()) > 25) {
                    List<UUID> list = inZone.get(block1);
                    list.remove(player1.getUniqueId());
                    inZone.put(block1, list);

                    if (player1.getUniqueId().equals(player.getUniqueId())) {
                        PlayerUtils.stopSeeHealthHead(player1);
                    }
                }
            }
        });
    }

    @Override
    public void onAllUse(Player player, Player use) {
        if (blocks.stream().anyMatch(block -> block.getLocation().distance(use.getLocation()) <= 25)) {
            Messages.ROBIN_OEIL_POWER.send(player, new Replacement("<name>", use.getName()));
        }
    }

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        if (getPlayer() == null) return;

        if (blocks.stream().anyMatch(block -> block.getLocation().distance(player.getLocation()) <= 25)) {
            Messages.ROBIN_OEIL_GAPPLE.send(getPlayer(), new Replacement("<name>", player.getName()));
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;
        if (!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getDamager();
        if (getPlayer() == null) return;

        if (blocks.stream().anyMatch(block -> block.getLocation().distance(player.getLocation()) <= 25)) {
            Messages.ROBIN_OEIL_COMBAT.send(getPlayer(), new Replacement("<name>", player.getName()));
        }
    }
}

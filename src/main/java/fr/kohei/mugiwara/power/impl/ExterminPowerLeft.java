package fr.kohei.mugiwara.power.impl;

import fr.kohei.mugiwara.Mugiwara;
import fr.kohei.mugiwara.utils.config.Messages;
import fr.kohei.mugiwara.game.player.MUPlayer;
import fr.kohei.mugiwara.power.RightClickPower;
import fr.kohei.mugiwara.roles.impl.alliance.JackRole;
import fr.kohei.mugiwara.utils.utils.Utils;
import fr.kohei.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public class ExterminPowerLeft extends RightClickPower {
    @Override
    public ItemStack getItem() {
        // TODO DEMANDE C QUOI LITEM
        return new ItemBuilder(Material.BONE).setName(Utils.itemFormat("&d&lExtermin")).toItemStack();
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public Integer getCooldownAmount() {
        return null;
    }

    @Override
    public boolean onEnable(Player player, boolean rightClick) {
        JackRole jackRole = (JackRole) MUPlayer.get(player).getRole();
        ExterminPowerRight right = (ExterminPowerRight) jackRole.getPowers()
                .stream().filter(power -> power instanceof ExterminPowerRight)
                .findFirst().orElse(null);
        if (right == null) return false;

        // get the block he is looking
        Block block = player.getTargetBlock((HashSet<Byte>) null, 4);
        if (block == null) return false;

        if (jackRole.getFirst() != null && jackRole.getFirst().getType() == Material.YELLOW_FLOWER) return false;

        block.setType(Material.YELLOW_FLOWER);
        jackRole.setFirst(block);

        int points = right.getTimer();

        if (points >= 180) {
            exterminTask(player, 3);
        } else if (points >= 120) {
            exterminTask(player, 5);
        } else if (points >= 60) {
            exterminTask(player, 10);
        }

        return true;
    }

    @Override
    public boolean rightClick() {
        return false;
    }

    @Override
    public boolean isGive() {
        return false;
    }

    private void exterminTask(Player player, int every) {
        JackRole jackRole = (JackRole) MUPlayer.get(player).getRole();
        final UUID uuid = player.getUniqueId();

        new BukkitRunnable() {
            private int timer = 0;
            private final List<UUID> playersIn = new ArrayList<>();

            @Override
            public void run() {
                Player player = Bukkit.getPlayer(uuid);
                if (player == null) return;

                Block block = jackRole.getFirst();
                if (block.getType() != Material.YELLOW_FLOWER) {
                    jackRole.setFirst(null);
                    cancel();
                    return;
                }

                if (timer % every == 0) {
                    Utils.getNearPlayers(player, 50).forEach(nearPlayer -> nearPlayer.damage(1));
                }

                List<UUID> nearUuids = new ArrayList<>();

                Utils.getNearPlayers(player, 50).forEach(player1 -> {
                    nearUuids.add(player1.getUniqueId());
                    if (!playersIn.contains(player1.getUniqueId())) {
                        playersIn.add(player1.getUniqueId());
                        jackRole.getPlayersInZone().add(player1.getUniqueId());
                        Messages.JACK_EXTERMIN_TARGET.send(player1);
                    }
                });

                playersIn.forEach(uuid1 -> {
                    if (!nearUuids.contains(uuid1)) {
                        playersIn.remove(uuid1);
                    }
                });

                timer++;
            }
        }.runTaskTimer(Mugiwara.getInstance(), 0, 20);
    }

}

package fr.uniteduhc.mugiwara.power.impl;

import fr.uniteduhc.mugiwara.Mugiwara;
import fr.uniteduhc.mugiwara.utils.config.Messages;
import fr.uniteduhc.mugiwara.game.player.MUPlayer;
import fr.uniteduhc.mugiwara.power.DamagePlayerPower;
import fr.uniteduhc.mugiwara.roles.RolesType;
import fr.uniteduhc.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.stream.Stream;

public class MorsurePower extends DamagePlayerPower {

    @Override
    public String getName() {
        return "Morsure";
    }

    @Override
    public Integer getCooldownAmount() {
        return 90;
    }

    @Override
    public ItemStack getItem() {
        final ItemStack item = new ItemStack(Material.QUARTZ);
        final ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(ChatUtil.translate("&cMorsure"));
        item.setItemMeta(meta);

        return item;
    }

    @Override
    public boolean onEnable(Player player, Player target, EntityDamageByEntityEvent event) {
        final MUPlayer uPlayer = MUPlayer.get(player);
        final AllosaurusPower allosaurusPower = (AllosaurusPower) uPlayer.getRole().getPowers().stream().filter(power ->
                power.getName().equalsIgnoreCase("Allosaurus")).findAny().get();

        final List<Class<?>> exemptedRoles = Stream.of(
                RolesType.QUEEN,
                RolesType.KING,
                RolesType.JACK,
                RolesType.KAIDO,
                RolesType.CHOPPER
        ).map(RolesType::getRoleClass).collect(
                java.util.stream.Collectors.toList()
        );

        if (allosaurusPower.isCurrentlyInUse() && !exemptedRoles.contains(MUPlayer.get(target).getRole().getClass())) {
            player.setPassenger(target);

            Bukkit.getScheduler().runTaskLater(Mugiwara.getInstance(), () -> {
                player.setPassenger(null);
            }, 20 * 3);

            if (player.getPassenger().getName().equalsIgnoreCase(target.getName())) {
                Bukkit.getScheduler().scheduleSyncRepeatingTask(Mugiwara.getInstance(), () -> {
                    if (target.isSneaking())
                        player.setPassenger(null);

                }, 0, 1);

            }


            Messages.XDRAKE_MORSURE_VICTIM.send(player);


        }
        return false;
    }
}

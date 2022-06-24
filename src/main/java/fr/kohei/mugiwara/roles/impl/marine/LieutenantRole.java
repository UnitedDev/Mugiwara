package fr.kohei.mugiwara.roles.impl.marine;

import fr.kohei.mugiwara.Mugiwara;
import fr.kohei.mugiwara.power.impl.AnalysePower;
import fr.kohei.mugiwara.power.impl.RecherchePower;
import fr.kohei.mugiwara.utils.config.Messages;
import fr.kohei.mugiwara.utils.config.Replacement;
import fr.kohei.mugiwara.game.player.MUPlayer;
import fr.kohei.mugiwara.roles.RolesType;
import fr.kohei.utils.ItemBuilder;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

@Setter
@Getter
public class LieutenantRole extends RolesType.MURole implements Listener {
    private boolean canAnalyse = false;

    public LieutenantRole() {
        super(Arrays.asList(
                new RecherchePower(),
                new AnalysePower()
        ));
    }

    @Override
    public RolesType getRole() {
        return RolesType.LIEUTENANT;
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(Material.IRON_INGOT);
    }

    @Override
    public String[] getDescription() {
        return new String[0];
    }

    @Override
    public void onDistribute(Player player) {
        final ItemBuilder sword = new ItemBuilder(Material.DIAMOND_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 3);
        final ItemBuilder bow = new ItemBuilder(Material.BOW).addEnchant(Enchantment.ARROW_DAMAGE, 2);

        player.getInventory().addItem(sword.toItemStack());
        player.getInventory().addItem(bow.toItemStack());

        final List<RolesType> rolesTypes = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            final int index = i + new Random().nextInt(RolesType.values().length - i);
            if (Arrays.asList(RolesType.values()).get(index) != RolesType.LIEUTENANT) {
                rolesTypes.add(Arrays.asList(RolesType.values()).get(index));
            } else {
                i--;
            }
        }

        rolesTypes.add(RolesType.KAIDO);

        final Map<Player, RolesType> playerRolesTypeMap = new HashMap<>();

        for (Player players : Bukkit.getOnlinePlayers()) {
            if (rolesTypes.contains(MUPlayer.get(players).getRole().getRole()))
                playerRolesTypeMap.put(player, MUPlayer.get(players).getRole().getRole());
        }

        for (Player players : playerRolesTypeMap.keySet()) {
            Mugiwara.knowsRole(players, playerRolesTypeMap.get(players));
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player lieutenant = getPlayer();
        Player player = event.getEntity();
        if (lieutenant == null) return;

        if (!(MUPlayer.get(player).getRole() instanceof CommandantRole)) return;

        Messages.LIEUTENANT_COMMANDANT_DEATH.send(lieutenant);
        lieutenant.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
    }

    @Override
    public void onEpisode(Player player) {
        canAnalyse = true;
        Bukkit.getScheduler().runTaskLater(Mugiwara.getInstance(), () -> canAnalyse = false, 120 * 20L);
    }
}

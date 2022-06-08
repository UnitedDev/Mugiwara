package fr.kohei.mugiwara.roles.impl.marine;

import com.google.common.collect.Lists;
import fr.kohei.mugiwara.config.Messages;
import fr.kohei.mugiwara.config.Replacement;
import fr.kohei.mugiwara.game.MUPlayer;
import fr.kohei.mugiwara.power.Power;
import fr.kohei.mugiwara.roles.RolesType;
import fr.kohei.uhc.game.player.UPlayer;
import fr.kohei.uhc.module.manager.Role;
import fr.kohei.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

/**
 * @author Salers
 * made on fr.kohei.mugiwara.roles.impl.marine
 */
public class LieutenantRole extends RolesType.MURole {

    public LieutenantRole() {
        super(Arrays.asList(

        ));
    }

    @Override
    public RolesType getRole() {
        return RolesType.LIEUTENANT;
    }

    @Override
    public ItemStack getItem() {
        return null;
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
            if(Arrays.asList(RolesType.values()).get(index) != RolesType.LIEUTENANT) {
                rolesTypes.add(Arrays.asList(RolesType.values()).get(index));
            }


        }

        rolesTypes.add(RolesType.KAIDO);

        final Map<Player, RolesType> playerRolesTypeMap = new HashMap<>();

        for (Player players : Bukkit.getOnlinePlayers()) {
            if (rolesTypes.contains(MUPlayer.get(players).getRole().getRole()))
                playerRolesTypeMap.put(player, MUPlayer.get(players).getRole().getRole());

        }

        for (Player players : playerRolesTypeMap.keySet()) {
            Messages.LIEUTENANT_FIVE_ROLES.send(players
                    , new Replacement("<name>", player.getName()),
                    new Replacement("<role>", playerRolesTypeMap.get(players).getName()));

        }

    }

    @Override
    public void onDeath(Player player, Player killer) {
        if (MUPlayer.get(player).getRole().getRole() == RolesType.COMMANDANT) {
            MUPlayer.players.values().stream().filter(muPlayer -> muPlayer.getRole().getClass().equals(this.getClass())).
                    findAny().get().getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,
                            1000000, 1, false, false));
            Messages.LIEUTENANT_COMMANDANT_DEATH.send(player);


        }
    }
}

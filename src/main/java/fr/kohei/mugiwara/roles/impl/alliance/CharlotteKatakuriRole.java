package fr.kohei.mugiwara.roles.impl.alliance;

import fr.kohei.mugiwara.Mugiwara;
import fr.kohei.mugiwara.config.Messages;
import fr.kohei.mugiwara.config.Replacement;
import fr.kohei.mugiwara.game.MUPlayer;
import fr.kohei.mugiwara.power.Power;
import fr.kohei.mugiwara.roles.RolesType;
import fr.kohei.utils.ChatUtil;
import fr.kohei.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.List;

/**
 * @author Salers
 * made on fr.kohei.mugiwara.roles.impl.alliance
 */
public class CharlotteKatakuriRole extends RolesType.MURole {

    private int inWater;

    public CharlotteKatakuriRole() {
        super(Arrays.asList(

        ));
    }

    @Override
    public void onDistribute(Player player) {
        player.setMaxHealth(28);
        player.getInventory().addItem(new ItemBuilder(Material.SNOW_BALL).setAmount(12).setName(ChatUtil.translate("&6Mochi")).toItemStack());

        for(MUPlayer muPlayers : MUPlayer.players.values()) {
            if(muPlayers.getRole().getRole() == RolesType.BIG_MOM) {
                Messages.KATAKURI_ROLES_REVEAL.send(player, new Replacement("<name>", muPlayers.getPlayer().
                        getName()), new Replacement("<role>", muPlayers.getRole().getRole().getName()));
            }
        }
    }

    @Override
    public void onSecond(Player player) {
        final Block block = player.getLocation().getBlock();

        if (block.getType() == Material.STATIONARY_WATER || block.getType() == Material.WATER) this.inWater += 1;
        else this.inWater = 0;

        if (this.inWater >= 5) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 6 * 20, 2, false, false));
            Messages.WATER.send(player);
            this.inWater = 0;
        }

        final int amountOfSnowballs = player.getInventory().getItem(player.getInventory().first(Material.SNOW_BALL)).getAmount();

        Bukkit.getScheduler().scheduleSyncRepeatingTask(Mugiwara.getInstance(), () -> {
            if(amountOfSnowballs < 12) {
                player.getInventory().getItem(player.getInventory().first(Material.SNOW_BALL)).setAmount(amountOfSnowballs + 1);

            }
        }, 0, 20 * 90);
    }

    @Override
    public void onKill(Player death, Player killer) {
        if(MUPlayer.get(killer).getRole().getRole() == RolesType.KATAKURI) {
            final int amountOfSnowballs = killer.getInventory().getItem(killer.getInventory().first(Material.SNOW_BALL)).getAmount();
            final int diff = 12 - amountOfSnowballs;
            killer.getInventory().getItem(killer.getInventory().first(Material.SNOW_BALL)).setAmount(amountOfSnowballs + diff);
        }
    }

    @Override
    public RolesType getRole() {
        return RolesType.KATAKURI;
    }

    @Override
    public ItemStack getItem() {
        return null;
    }

    @Override
    public String[] getDescription() {
        return new String[0];
    }
}

package fr.uniteduhc.mugiwara.roles.alliance;

import fr.uniteduhc.mugiwara.Mugiwara;
import fr.uniteduhc.mugiwara.game.player.MUPlayer;
import fr.uniteduhc.mugiwara.roles.RolesType;
import fr.uniteduhc.utils.ChatUtil;
import fr.uniteduhc.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;

public class CharlotteKatakuriRole extends RolesType.MURole {

    public CharlotteKatakuriRole() {
        super(Arrays.asList(

        ), 1057000000L);
    }

    @Override
    public boolean hasFruit() {
        return true;
    }

    @Override
    public void onDistribute(Player player) {
        player.setMaxHealth(14);
        player.getInventory().addItem(new ItemBuilder(Material.SNOW_BALL).setAmount(12).setName(ChatUtil.translate("&6Mochi")).toItemStack());

        Mugiwara.knowsRole(player, RolesType.BIG_MOM);
    }

    @Override
    public void onSecond(Player player) {
        super.onSecond(player);

        final int amountOfSnowballs = player.getInventory().getItem(player.getInventory().first(Material.SNOW_BALL)).getAmount();

        Bukkit.getScheduler().runTaskTimer(Mugiwara.getInstance(), () -> {
            if (amountOfSnowballs < 12) {
                player.getInventory().getItem(player.getInventory().first(Material.SNOW_BALL)).setAmount(amountOfSnowballs + 1);
            }
        }, 0, 20 * 90);
    }

    @Override
    public void onKill(Player death, Player killer) {
        if (MUPlayer.get(killer).getRole().getRole() == RolesType.KATAKURI) {
            final int amountOfSnowballs = killer.getInventory().getItem(killer.getInventory().first(Material.SNOW_BALL)).getAmount();
            final int diff = 12 - amountOfSnowballs;
            killer.getInventory().getItem(killer.getInventory().first(Material.SNOW_BALL)).setAmount(amountOfSnowballs + diff);
        }
    }

    @Override
    public void onDeath(Player player, Player killer) {
        final Player p = Mugiwara.findRole(RolesType.BIG_MOM);
        if (p == null) return;
        p.setHealth(p.getHealth() - 4);
    }

    @Override
    public RolesType getRole() {
        return RolesType.KATAKURI;
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(Material.SNOW_BALL);
    }
}

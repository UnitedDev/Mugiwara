package fr.kohei.mugiwara.power.impl;

import fr.kohei.mugiwara.Mugiwara;
import fr.kohei.mugiwara.config.Messages;
import fr.kohei.mugiwara.game.MUPlayer;
import fr.kohei.mugiwara.power.RightClickPower;
import fr.kohei.mugiwara.roles.RolesType;
import fr.kohei.mugiwara.utils.Utils;
import fr.kohei.uhc.game.player.UPlayer;
import fr.kohei.utils.ChatUtil;
import fr.kohei.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class YasakaniPower extends RightClickPower {
    private boolean used;

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.NETHER_STAR).setName(Utils.itemFormat("&a&lYasakani no Magatama")).toItemStack();
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
        // if used return false and send the player the error message
        if (used) {
            player.sendMessage(ChatUtil.prefix("&cVous avez déjà utilisé ce pouvoir."));
            return false;
        }

        // spawn a lightning bolt at player's location
        player.getWorld().strikeLightningEffect(player.getLocation());

        // enable used
        used = true;

        // add speed 3 for 2 minutes to player
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 120 * 20, 2));

        // get all players with a 30 blocks radius
        Utils.getNearPlayers(player, 30).forEach(nearPlayer -> {
            // the role of the nearPlayer
            RolesType role = MUPlayer.get(nearPlayer).getRole().getRole();

            // if the role is Luffy, Nami or Big Mom continue
            if (role == RolesType.LUFFY || role == RolesType.NAMI || role == RolesType.BIG_MOM) return;

            // remove 3 hearts to the near player
            // if the player health is less than 3 hearts, set it to 0.5 heart
            if (nearPlayer.getHealth() <= 6) {
                nearPlayer.setHealth(0.5);
            } else {
                nearPlayer.setHealth(nearPlayer.getHealth() - 6);
            }
        });
        // send the kizaru yasakani use message
        Messages.KIZARU_YASAKANI_USE.send(player);

        // send the kizaru yasakani end message 2 minutes later (run task later)
        player.getServer().getScheduler().runTaskLater(Mugiwara.getInstance(), () -> Messages.KIZARU_YASAKANI_END.send(player), 20 * 120);

        return true;
    }
}

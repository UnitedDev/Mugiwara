package fr.kohei.mugiwara.power.impl;

import fr.kohei.mugiwara.Mugiwara;
import fr.kohei.mugiwara.game.player.MUPlayer;
import fr.kohei.mugiwara.power.Power;
import fr.kohei.mugiwara.power.RightClickPower;
import fr.kohei.mugiwara.utils.config.Messages;
import fr.kohei.mugiwara.utils.utils.Utils;
import fr.kohei.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

public class LazerLeftPower extends RightClickPower {
    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.STONE_BUTTON).setName(Utils.itemFormat("&c&lLazer")).toItemStack();
    }

    @Override
    public boolean isGive() {
        return false;
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

        if (rightClick) return false;

        MUPlayer muPlayer = MUPlayer.get(player);

        if (muPlayer == null) return false;

        Power power = muPlayer.getRole().getPowers().stream()
                .filter(power1 -> power1 instanceof LazerRightPower)
                .findFirst()
                .orElse(null);

        if (power == null) return false;

        LazerRightPower lazerRightPower = (LazerRightPower) power;

        if (!lazerRightPower.isUsed()) {
            Messages.PACIFISTA_LAZER_CANT.send(player);
            return false;
        }

        int timer = lazerRightPower.getTimer();

        if (timer <= 2) {
            removeHealthDamage(player,1.5);
        } else if (timer == 3 || timer == 2) {
            removeHealthDamage(player, 2.5);
        } else if (timer == 5) {
            removeHealthDamage(player, 3.5);
        }

        Mugiwara.getInstance().removeActionBar(player, "chargement");
        lazerRightPower.setUsed(false);
        lazerRightPower.setTimer(0);

        return true;
    }

    @Override
    public boolean rightClick() {
        return false;
    }

    private void removeHealthDamage(Player player, double health) {
        Location location = player.getTargetBlock((Set<Material>) null, 30).getLocation();
        player.getWorld().createExplosion(location, 3.5F, false);
        Bukkit.getOnlinePlayers().stream()
                .filter(players -> players.getLocation().distance(location) <= 5)
                .forEach(players -> players.damage(health));
    }
}

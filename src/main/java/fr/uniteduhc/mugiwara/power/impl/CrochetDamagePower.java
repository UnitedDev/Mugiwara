package fr.uniteduhc.mugiwara.power.impl;

import fr.uniteduhc.mugiwara.Mugiwara;
import fr.uniteduhc.mugiwara.power.DamagePlayerPower;
import fr.uniteduhc.mugiwara.utils.config.Messages;
import fr.uniteduhc.mugiwara.utils.config.Replacement;
import fr.uniteduhc.mugiwara.utils.utils.Utils;
import fr.uniteduhc.utils.ChatUtil;
import fr.uniteduhc.utils.ItemBuilder;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class CrochetDamagePower extends DamagePlayerPower {

    private int use = 0;

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.GOLD_HOE).setName(Utils.notClickItem("&6&lCrochet")).setInfinityDurability().toItemStack();
    }

    @Override
    public boolean onEnable(Player player, Player target, EntityDamageByEntityEvent damage) {

        if(use > 2){
            player.sendMessage(ChatUtil.prefix("&cVous ne pouvez plus utilisé Crochet."));
            return false;
        }

        use++;
        target.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 10 * 20, 0, false, false));
        Messages.CROCODILE_CROCHET_USE.send(player, new Replacement("<name>", target.getName()));
        Messages.CROCODILE_CROCHET_HIT.send(target);
        new BukkitRunnable(){
            int time = 10;
            Vector vector = player.getLocation().getDirection();
            int y = player.getLocation().clone().getBlockY();
            @Override
            public void run() {

                if(target.getGameMode() == GameMode.SPECTATOR){
                    cancel();
                    return;
                }

                target.setVelocity(vector.multiply(1.2).setY(0.5));

                time--;

                if(time == 0){
                    cancel();
                }

            }
        }.runTaskTimer(Mugiwara.getInstance(), 0L, 20L);

        return true;
    }

    @Override
    public String getName() {
        return "Crochet";
    }

    @Override
    public Integer getCooldownAmount() {
        return null;
    }
}

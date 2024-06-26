package fr.uniteduhc.mugiwara.roles.solo;

import fr.uniteduhc.mugiwara.power.impl.BlackHolePower;
import fr.uniteduhc.mugiwara.power.impl.KaishinPower;
import fr.uniteduhc.mugiwara.power.impl.PoneglypheRemovePower;
import fr.uniteduhc.mugiwara.roles.RolesType;
import fr.uniteduhc.mugiwara.utils.config.Messages;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;

public class TeachRole extends RolesType.MURole implements Listener {

    public TeachRole() {
        super(Arrays.asList(
                new KaishinPower(),
                new BlackHolePower(),
                new PoneglypheRemovePower()
        ), 2247600000L);
    }

    @Override
    public RolesType getRole() {
        return RolesType.TEACH;
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(Material.OBSIDIAN);
    }

    @Override
    public void onDistribute(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));
    }

    @Override
    public boolean hasFruit() {
        return true;
    }

    @Override
    public void onSecond(Player player) {
        super.onSecond(player);

        player.removePotionEffect(PotionEffectType.POISON);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (!(event.getDamager() instanceof Player)) return;

        Player player = (Player) event.getEntity();
        Player damager = (Player) event.getDamager();

        if (!isRole(damager)) return;

        BlackHolePower blackHolePower = (BlackHolePower) getPowers().stream()
                .filter(power -> power instanceof BlackHolePower).findFirst().orElse(null);

        if (blackHolePower == null) return;

        if (blackHolePower.isUsing()) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 10 * 20, 0, false, false));
            EntityPlayer playerEntity = ((CraftPlayer) player).getHandle();
            EntityPlayer damagerEntity = ((CraftPlayer) damager).getHandle();

            if(playerEntity.getAbsorptionHearts() + damagerEntity.getAbsorptionHearts() >= 4) {
                damagerEntity.setAbsorptionHearts(4);
            } else {
                damagerEntity.setAbsorptionHearts(playerEntity.getAbsorptionHearts() + damagerEntity.getAbsorptionHearts());
            }
            playerEntity.setAbsorptionHearts(0);
            Messages.TEACH_BLACKHOLE_TARGET.send(player);
        }
    }
}

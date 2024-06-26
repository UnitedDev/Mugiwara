package fr.uniteduhc.mugiwara.roles.marine;

import fr.uniteduhc.mugiwara.power.impl.DuoPower;
import fr.uniteduhc.mugiwara.power.impl.BiblePower;
import fr.uniteduhc.mugiwara.power.impl.VoyagePower;
import fr.uniteduhc.mugiwara.roles.RolesType;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Getter
public class BartholomewKumaRole extends RolesType.MURole implements Listener {

    private final List<UUID> teleportedPlayers = new ArrayList<>();

    public BartholomewKumaRole() {
        super(Arrays.asList(
                new BiblePower(),
                new VoyagePower(),
                new DuoPower()
        ), 296000000L);
    }

    @Override
    public RolesType getRole() {
        return RolesType.KUMA;
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(Material.ANVIL);
    }

    @Override
    public boolean hasFruit() {
        return true;
    }

    @Override
    public void onSecond(Player player) {
        super.onSecond(player);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if(!(event.getEntity() instanceof Player)) return;

        final Player player = (Player) event.getEntity();

        if(!this.isRole(player)) return;

        if(player.getOpenInventory().getTopInventory() == null) return;
        if(player.getOpenInventory().getTopInventory().getName().equalsIgnoreCase("Kuma Bible")) event.setCancelled(true);
    }

    @Override
    public void onDistribute(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
        player.setMaxHealth(24.0);

        player.setWalkSpeed(0.21F);
    }

    @Override
    public double getStrengthBuffer() {
        return 1.05F;
    }

    @Override
    public double getResistanceBuffer() {
        return 0.95F;
    }
}

package fr.kohei.mugiwara.roles.marine;

import fr.kohei.mugiwara.Mugiwara;
import fr.kohei.mugiwara.game.player.MUPlayer;
import fr.kohei.mugiwara.power.impl.IcePower;
import fr.kohei.mugiwara.power.impl.IceRightPower;
import fr.kohei.mugiwara.roles.RolesType;
import fr.kohei.mugiwara.utils.utils.Cooldown;
import fr.kohei.utils.Cuboid;
import fr.kohei.utils.ItemBuilder;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class KuzanRole extends RolesType.MURole implements Listener {

    private int coup = 0;
    private boolean isSaberMode = false;

    private int inWater = 0;
    private int timer = 0;
    private int endurence = 0;
    private boolean ice;
    private Player lastHit;
    private List<Location> domeLocation = new ArrayList<>();
    private final Cooldown domeCooldown = new Cooldown("domeCooldown");

    private ItemStack iceSaber = new ItemBuilder(Material.DIAMOND_SWORD).setName("§bIce Saber").addEnchant(Enchantment.DAMAGE_ALL, 4).toItemStack();

    public KuzanRole() {
        super(Arrays.asList(
                new IcePower(),
                new IceRightPower()
        ), 0L);
    }

    @Override
    public RolesType getRole() {
        return RolesType.KUZAN;
    }

    @Override
    public void onDistribute(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false));
        Mugiwara.knowsRole(player, RolesType.AKAINU);
        Mugiwara.knowsRole(player, RolesType.KIZARU);
    }

    @Override
    public void onSecond(Player player) {
        final Block block = player.getLocation().getBlock();

        if (block.getType() == Material.STATIONARY_WATER || block.getType() == Material.WATER) this.inWater++;
        else this.inWater = 0;

        if (this.inWater >= 5) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 6 * 20, 0, false, false));
            //Messages.WATER.send(player);
            this.inWater = 0;
        }

        timer++;

        Mugiwara.getInstance().addActionBar(player, "&cEndurance &8» &f" + endurence, "endurence");

        if (timer % 3 == 0) {
            if (endurence < 200)
                endurence++;
        }

        if (timer % 5 == 0) {
            lastHit = null;
        }

        if (isIce()) {
            Cuboid cuboid = new Cuboid(
                    player.getLocation().clone().add(2, -1, 2),
                    player.getLocation().clone().add(-2, -2, -2)
            );
            cuboid.getBlockList().stream().filter(block1 -> block1.getType().name().contains("WATER")).forEach(block1 -> {
                block.setType(Material.PACKED_ICE);
            });
        }

        if (domeCooldown.isCooldownNoMessage(player)) return;

        domeLocation.stream()
                .filter(domeLoc -> domeLoc.getBlock().getType() != Material.BEDROCK)
                .forEach(domeLoc -> domeLoc.getBlock().setType(Material.AIR));

    }

    public void incrementCoup() {
        if (!isSaberMode) return;
        coup++;
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        if (!(e.getDamager() instanceof Player)) return;

        Player damager = (Player) e.getDamager();

        if(damager != getPlayer()) return;

        Player player = (Player) e.getEntity();

        MUPlayer muDamager = MUPlayer.get(damager);
        MUPlayer muPlayer = MUPlayer.get(player);

        if(muPlayer.getRole() instanceof KuzanRole){

            KuzanRole kuzanRole = (KuzanRole) muPlayer.getRole();

            kuzanRole.setLastHit(damager);

        }

        if(!(muDamager.getRole() instanceof KuzanRole)) return;

        KuzanRole kuzanRole = (KuzanRole) muDamager.getRole();

        kuzanRole.incrementCoup();

        if (kuzanRole.getCoup() == 30) {
            damager.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
            damager.getInventory().remove(getIceSaber());
            kuzanRole.setSaberMode(false);
            kuzanRole.setCoup(0);
        }

        if (!(damager.getItemInHand().isSimilar(getIceSaber()) || kuzanRole.isSaberMode())) return;

        if (Math.random() < 0.16D) {

            player.getLocation().clone().add(0, -1, 0).getBlock().setType(Material.PACKED_ICE);

        }


    }

}

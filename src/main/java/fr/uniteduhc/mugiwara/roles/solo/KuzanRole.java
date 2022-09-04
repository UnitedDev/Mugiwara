package fr.uniteduhc.mugiwara.roles.solo;

import fr.uniteduhc.mugiwara.Mugiwara;
import fr.uniteduhc.mugiwara.game.player.MUPlayer;
import fr.uniteduhc.mugiwara.power.impl.HieHieLeftPower;
import fr.uniteduhc.mugiwara.power.impl.HieHieRightPower;
import fr.uniteduhc.mugiwara.power.impl.IceTogglePower;
import fr.uniteduhc.mugiwara.roles.RolesType;
import fr.uniteduhc.mugiwara.roles.marine.AkainuRole;
import fr.uniteduhc.utils.Cuboid;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;

@Getter
@Setter
public class KuzanRole extends RolesType.MURole implements Listener {

    private int inWater = 0;
    private int timer = 0;
    private int endurence = 0;
    private boolean ice = false;
    private HieHiePowerType hieHiePowerType = HieHiePowerType.ICE_AGE;

    public KuzanRole() {
        super(Arrays.asList(
                new IceTogglePower(),
                new HieHieLeftPower(),
                new HieHieRightPower()
        ), 0L);
    }

    public RolesType getRole() {
        return RolesType.KUZAN;
    }

    public void onDistribute(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 2147483647, 0, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 2147483647, 0, false, false));
    }

    public void onSecond(Player player) {
        Block block = player.getLocation().getBlock();
        if (block.getType() == Material.STATIONARY_WATER || block.getType() == Material.WATER) {
            this.inWater++;
        } else {
            this.inWater = 0;
        }
        if (this.inWater >= 5) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 120, 0, false, false));
            this.inWater = 0;
        }
        this.timer++;
        Mugiwara.getInstance().addActionBar(player, "&cEndurance &8&f" + this.endurence, "endurence");
        if (this.timer % 3 == 0 &&
                this.endurence < 200)
            this.endurence++;
        if (isIce()) {
            Cuboid cuboid = new Cuboid(player.getLocation().clone().add(-5.0D, -1.0D, -5.0D), player.getLocation().clone().add(5.0D, 5.0D, 5.0D));
            cuboid.getBlockListWithOnly(
                    Arrays.asList(Material.WATER, Material.STATIONARY_WATER)).forEach(block1 -> block1.setType(Material.PACKED_ICE));
        }
    }

    @EventHandler
    public void onLava(PlayerBucketEmptyEvent e) {
        if (!isIce())
            return;
        Player player = e.getPlayer();
        MUPlayer muPlayer = MUPlayer.get(player);
        RolesType.MURole role = muPlayer.getRole();
        if (e.getBucket() != Material.LAVA_BUCKET)
            return;
        if (role instanceof AkainuRole)
            return;
        Cuboid cuboid = new Cuboid(getPlayer().getLocation().clone().add(-5.0D, -1.0D, -5.0D), getPlayer().getLocation().clone().add(5.0D, 5.0D, 5.0D));
        cuboid.getBlockList().stream()
                .filter(block -> (block.getLocation().getBlockX() == player.getLocation().getBlockX()))
                .filter(block -> (block.getLocation().getBlockY() == player.getLocation().getBlockY()))
                .filter(block -> (block.getLocation().getBlockZ() == player.getLocation().getBlockZ()))
                .forEach(block -> e.setCancelled(true));
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Block block = e.getBlock();
        if (block.hasMetadata("invincibiliy"))
            e.setCancelled(true);
    }

    public enum HieHiePowerType {
        ICE_AGE("Ice Age"),
        PHEASANT_PEAK("Pheasant Peak"),
        ICE_BALL("Ice Ball");

        HieHiePowerType(String name) {
            this.name = name;
        }

        private final String name;

        public String getName() {
            return this.name;
        }

        public static HieHiePowerType getNextPower(HieHiePowerType hieHiePowerType) {
            if (hieHiePowerType == ICE_AGE)
                return PHEASANT_PEAK;
            if (hieHiePowerType == PHEASANT_PEAK)
                return ICE_BALL;
            if (hieHiePowerType == ICE_BALL)
                return ICE_AGE;
            return ICE_AGE;
        }
    }
}


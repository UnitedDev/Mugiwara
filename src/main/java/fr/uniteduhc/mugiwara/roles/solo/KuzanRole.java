package fr.uniteduhc.mugiwara.roles.solo;

import fr.uniteduhc.mugiwara.Mugiwara;
import fr.uniteduhc.mugiwara.camp.CampType;
import fr.uniteduhc.mugiwara.game.player.MUPlayer;
import fr.uniteduhc.mugiwara.power.impl.HieHieLeftPower;
import fr.uniteduhc.mugiwara.power.impl.HieHieRightPower;
import fr.uniteduhc.mugiwara.power.impl.IceTogglePower;
import fr.uniteduhc.mugiwara.power.impl.RevengePower;
import fr.uniteduhc.mugiwara.roles.RolesType;
import fr.uniteduhc.mugiwara.roles.marine.AkainuRole;
import fr.uniteduhc.mugiwara.utils.config.Messages;
import fr.uniteduhc.utils.Cuboid;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;

@Getter
@Setter
public class KuzanRole extends RolesType.MURole implements Listener {

    private boolean hasKillPirate = false;
    private boolean hasKillMarine = false;
    private int hitAkainu = 0;

    private Player lastHit = null;
    private int lastHitTimer = 0;
    private int timer = 0;
    private int endurence = 0;
    private boolean ice = false;
    private HieHiePowerType hieHiePowerType = HieHiePowerType.ICE_AGE;

    public KuzanRole() {
        super(Arrays.asList(
                new IceTogglePower(),
                new HieHieLeftPower(),
                new HieHieRightPower(),
                new RevengePower()
        ), 0L);
    }

    public RolesType getRole() {
        return RolesType.KUZAN;
    }

    public void onDistribute(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 2147483647, 0, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 2147483647, 0, false, false));
    }

    @Override
    public void onKill(Player death, Player killer) {

        if(!isRole(killer)) return;

        RolesType.MURole muRole = MUPlayer.get(death).getRole();

        if(muRole.getRole().getCampType() == CampType.MARINE){

            if(hasKillMarine) return;

            hasKillMarine = true;

            if(allowRevenge()){
                Messages.KUZAN_REVANGE_CAN.send(killer);
            }

            return;
        }

        if(muRole.getRole().getCampType() == CampType.MUGIWARA_HEART){

            if(hasKillPirate) return;

            hasKillPirate = true;

            if(allowRevenge()){
                Messages.KUZAN_REVANGE_CAN.send(killer);
            }

        }


    }

    @Override
    public boolean hasFruit() {
        return true;
    }

    public void onSecond(Player player) {
        super.onSecond(player);

        if(lastHit != null) lastHitTimer++;

        if(lastHitTimer == 5){
            lastHit = null;
            lastHitTimer = 0;
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
        if (!isIce()) return;

        Player player = e.getPlayer();
        MUPlayer muPlayer = MUPlayer.get(player);
        RolesType.MURole role = muPlayer.getRole();

        if (e.getBucket() != Material.LAVA_BUCKET) return;

        if (role instanceof AkainuRole) return;

        Cuboid cuboid = new Cuboid(getPlayer().getLocation().clone().add(-5.0D, -1.0D, -5.0D), getPlayer().getLocation().clone().add(5.0D, 5.0D, 5.0D));

        cuboid.getBlockList().stream()
                .filter(block -> (block.getLocation().getBlockX() == player.getLocation().getBlockX()))
                .filter(block -> (block.getLocation().getBlockY() == player.getLocation().getBlockY()))
                .filter(block -> (block.getLocation().getBlockZ() == player.getLocation().getBlockZ()))
                .forEach(block -> e.setCancelled(true));

    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e){
        if(!(e.getEntity() instanceof Player)) return;
        if(!(e.getDamager() instanceof Player)) return;

        Player player = (Player) e.getEntity();
        Player damager = (Player) e.getDamager();

        if(isRole(player)) lastHit = damager;

        if(isRole(damager)){
            MUPlayer muPlayer = MUPlayer.get(player);

            if(!(muPlayer.getRole() instanceof AkainuRole)) return;

            if(hitAkainu == 15) return;

            hitAkainu++;

            if(allowRevenge()){
                Messages.KUZAN_REVANGE_CAN.send(damager);
            }

        }
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

    public boolean allowRevenge(){
        return (hasKillPirate && hasKillMarine && hitAkainu >= 15);
    }
}


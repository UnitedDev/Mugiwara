package fr.uniteduhc.mugiwara.roles;

import fr.uniteduhc.mugiwara.camp.CampType;
import fr.uniteduhc.mugiwara.game.player.MUPlayer;
import fr.uniteduhc.mugiwara.power.Power;
import fr.uniteduhc.mugiwara.roles.alliance.CharlotteKatakuriRole;
import fr.uniteduhc.mugiwara.roles.alliance.KaidoRole;
import fr.uniteduhc.mugiwara.roles.alliance.KingRole;
import fr.uniteduhc.mugiwara.roles.marine.*;
import fr.uniteduhc.mugiwara.roles.mugiwara.*;
import fr.uniteduhc.mugiwara.roles.solo.BigMomRole;
import fr.uniteduhc.mugiwara.roles.solo.KuzanRole;
import fr.uniteduhc.mugiwara.roles.solo.SaboRole;
import fr.uniteduhc.mugiwara.roles.solo.TeachRole;
import fr.uniteduhc.mugiwara.utils.config.Messages;
import fr.uniteduhc.uhc.module.manager.Camp;
import fr.uniteduhc.uhc.module.manager.Role;
import fr.uniteduhc.utils.ChatUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
public enum RolesType {

    LUFFY("Monkey D. Luffy", CampType.MUGIWARA_HEART, LuffyRole.class, new ItemStack(Material.LEATHER_HELMET)),
    ZORO("Roronoa Zoro", CampType.MUGIWARA_HEART, ZoroRole.class, new ItemStack(Material.IRON_SWORD)),
    NAMI("Nami", CampType.MUGIWARA_HEART, NamiRole.class, new ItemStack(Material.STICK)),
    USSOP("Ussop", CampType.MUGIWARA_HEART, UssopRole.class, new ItemStack(Material.BOW)),
    SANJI("Vinsmoke Sanji", CampType.MUGIWARA_HEART, SanjiRole.class, new ItemStack(Material.DIAMOND_BOOTS)),
    CHOPPER("Tony Tony Chopper", CampType.MUGIWARA_HEART, ChopperRole.class, new ItemStack(Material.LEATHER)),
    ROBIN("Nico Robin", CampType.MUGIWARA_HEART, RobinRole.class, new ItemStack(Material.BOOK)),
    FRANKY("Franky", CampType.MUGIWARA_HEART, FrankyRole.class, new ItemStack(Material.IRON_BLOCK)),
    BROOK("Brook", CampType.MUGIWARA_HEART, BrookRole.class, new ItemStack(Material.BONE)),
    JIMBE("Jimbe", CampType.MUGIWARA_HEART, JimbeRole.class, new ItemStack(Material.RAW_FISH, 1, (short) 3)),
    LAW("Trafalgar D. Water Law", CampType.MUGIWARA_HEART, LawRole.class, new ItemStack(Material.SHEARS)),
    EUSTASS("Eustass Kid", CampType.MUGIWARA_HEART, EustassRole.class, null),
    PIRATE("Pirate", CampType.MUGIWARA_HEART, PirateRole.class, new ItemStack(Material.BANNER)),


    GARP("Monkey D. Garp", CampType.MARINE, GarpRole.class, new ItemStack(Material.TNT)),
    COBY("Coby ", CampType.MARINE, CobyRole.class, new ItemStack(Material.INK_SACK, 1, (short) 10)),
    DRAKE("X Drake", CampType.MARINE, XDrakeRole.class, new ItemStack(Material.ROTTEN_FLESH)),
    MIHAWK("Mihawk", CampType.MARINE, MihawkRole.class, new ItemStack(Material.DIAMOND_SWORD)),
    SMOKER("Smoker", CampType.MARINE, SmokerRole.class, new ItemStack(Material.INK_SACK, 1, (short) 0)),
    HANCOCK("Boa Hancock", CampType.MARINE, HancockRole.class, new ItemStack(Material.YELLOW_FLOWER, 1)),
    KIZARU("Kizaru", CampType.MARINE, KizaruRole.class, new ItemStack(Material.GLOWSTONE_DUST)),
    FUJITORA("Amiral Fujitora", CampType.MARINE, FujitoraRole.class, new ItemStack(Material.STAINED_GLASS, 1, (short) 11)),
    AKAINU("Chef Akainu", CampType.MARINE, AkainuRole.class, new ItemStack(Material.LAVA_BUCKET)),
    SENGOKU("Sengoku", CampType.MARINE, SengokuRole.class, new ItemStack(Material.BONE)),
    KUMA("Bartholomew Kuma", CampType.MARINE, BartholomewKumaRole.class, new ItemStack(Material.ANVIL)),
    TSURU("Tsuru", CampType.MARINE, TsuruRole.class, new ItemStack(Material.SUGAR)),
    CROCODILE("Crocodile", CampType.MARINE, CrocodileRole.class, new ItemStack(Material.INK_SACK, 1, (short)4)),
    PACIFISTA("Pacifista", CampType.MARINE, PacifistaRole.class, new ItemStack(Material.INK_SACK)),


    KAIDO("Kaidö", CampType.BIGMOM_KAIDO, KaidoRole.class, new ItemStack(Material.STONE_AXE)),
    KING("King", CampType.BIGMOM_KAIDO, KingRole.class, new ItemStack(Material.FEATHER)),
    QUEEN("Queen", CampType.BIGMOM_KAIDO, null, null),
    JACK("Jack", CampType.BIGMOM_KAIDO, null, new ItemStack(Material.GOLDEN_APPLE)), // TODO NOT FINISHED ON THE DOC
    KATAKURI("Charlotte Katakuri", CampType.BIGMOM_KAIDO, CharlotteKatakuriRole.class, new ItemStack(Material.SNOW_BALL)),
    BIG_MOM("Big Mom", CampType.BIGMOM_KAIDO, BigMomRole.class, new ItemStack(Material.CAKE)),

    KUZAN("Kuzan", CampType.SOLO, KuzanRole.class, new ItemStack(Material.PACKED_ICE)),
    SABO("Sabo", CampType.SOLO, SaboRole.class, new ItemStack(Material.BLAZE_POWDER)),
    TEACH("Marshall D. Teach", CampType.SOLO, TeachRole.class, new ItemStack(Material.OBSIDIAN)),

    ;

    private final String name;
    private final CampType campType;
    private final Class<? extends MURole> roleClass;
    private final ItemStack display;

    @Getter
    @Setter
    public static abstract class MURole extends Role {

        private List<Power> powers;
        private long prime;
        private int inWater;

        public MURole(List<Power> powers, long prime) {
            this.powers = new ArrayList<>(powers);
            this.prime = prime;
        }

        public abstract RolesType getRole();

        public void onClaim() {
            getPlayer().sendMessage(ChatUtil.prefix("&cVous n'avez aucun item à claim..."));
        }

        public double getStrengthBuffer() {
            return 1.0;
        }

        public double getResistanceBuffer() {
            return 1.0;
        }

        public void onAllUse(Player player, Player use) {

        }

        @Override
        public void onSecond(Player player) {
            if(hasFruit()) onWaterNausea(player);
        }

        @Override
        public String[] getDescription() {
            return Messages.DESCRIPTION.get(getRole()).toArray(new String[0]);
        }

        @Override
        public String getName() {
            return getRole().getName();
        }

        @Override
        public Camp getStartCamp() {
            return getRole().getCampType().getCamp();
        }

        public boolean isRole(Player player) {
            return (MUPlayer.get(player).getRole().getName().equalsIgnoreCase(getName()));
        }

        @Override
        public ItemStack getItem() {
            return getRole().getDisplay();
        }

        public boolean hasFruit(){
            return false;
        }

        private void onWaterNausea(Player player){
            if(isInWater(player)) this.inWater++;
            else this.inWater = 0;

            if(this.inWater >= 5){
                player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 6 * 20, 2, false, false));
                //Messages.WATER.send(player);
                this.inWater = 0;
            }
        }

        public boolean isInWater(Player player) {
            return player.getLocation().clone().getBlock().getType().name().contains("WATER") || player.getLocation().clone().add(0, -1, 0).getBlock().getType().name().contains("WATER");
        }
    }
}

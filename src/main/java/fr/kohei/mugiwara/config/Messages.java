package fr.kohei.mugiwara.config;

import fr.kohei.mugiwara.Mugiwara;
import fr.kohei.utils.ChatUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

@Getter
@AllArgsConstructor
public enum Messages {

    COOLDOWN_EXPIRE("&fLe cooldown pour le pouvoir &c<name> &fvient d'expirer."),
    WATER("&cNe restez pas trop longtemps dans l'eau."),

    LUFFY_GOMUGOMUNOMI("&fVous avez utilisé votre pouvoir &6Gomu Gomu no Mi&f."),
    LUFFY_FIRSTPOWER("&fVous avez utilisé votre premier pouvoir."),
    LUFFY_SECONDPOWER("&fVous avez utilisé votre deuxième pouvoir."),
    LUFFY_THIRDPOWER("&fVous avez utilisé votre troisième pouvoir."),

    ZORO_FIRSTKILL("&fVous avez obtenu &bSpeed I &fpour votre kill."),
    ZORO_SECONDKILL("&fVous avez obtenu &bSpeed II &fpour votre kill."),
    ZORO_THIRDKILL("&fVous avez obtenu &cForce I &fpour votre kill."),
    ZORO_FOURTHDKILL("&fVous avez obtenu &c1 coeur &fet &6+20% d'enflammer un joueur en le tapant &fpour votre kill."),
    ZORO_DASH("&fVous avez utilisé votre &6dash&f."),
    ZORO_DASH_ONWAY("&fLe &cdash &fde zoro est passé &cà côté de vous&f."),
    ZORO_DASH_ONYOU("&cZoro &fa utilisé son &cdash &fsur vous."),

    NAMI_CLIMATTACT_USE("&fVous avez utilisé votre &bClimat-Tact &fsur &6<name>"),
    NAMI_CLIMATTACT_TARGET("&6Nami &fa utilisé son &6Climat-Tact &fsur vous."),
    NAMI_PISTER_NOTSEEN("&cVous devez croiser <name> pour le pister."),
    NAMI_PISTER_USE("&fVous avez pisté &a<name> &fpendant &a5 minutes&f."),
    NAMI_PISTER_END("&cVotre pouvoir de pistage vient d'expirer."),

    NAMI_VOL_USE("&fVous avez volé &6<amount> &fpommes d'ors à &6<name>&f."),
    NAMI_VOL_ONME("&cNami &fvous a volé &6<amount> &fpommes d'ors."),

    NAMI_ZEUS_USE("&fVous avez utilisé votre pouvoir &aZeus&f."),
    NAMI_ZEUS_FINISH("&cVotre pouvoir Zeus a expiré."),
    NAMI_ZEUS_LIGHTNING("&fVous avez fait spawn des &aéclairs&f."),

    USSOP_KABUTO_POISON_USE("&fVous avez &2empoisonné &6<name>&f."),
    USSOP_KABUTO_POISON_ONME("&fVous vous êtes fait &2empoisonné &fpar &6Ussop&f."),
    USSOP_KABUTO_FIRE_USE("&fVous avez &eenflammé &6<name>&f."),
    USSOP_KABUTO_FIRE_ONME("&fVous vous êtes fait &aenflammé &fpar &6Ussop&f."),
    USSOP_KABUTO_STUN_USE("&fVous avez &dimmobilisé &6<name>&f."),
    USSOP_KABUTO_STUN_ONME("&fVous vous êtes fait &dimmobilisé &fpar &6Ussop&f."),
    USSOP_KABUTO_EXPLOSION_USE("&fVous avez créé une &cexplosion &6<name>&f."),
    USSOP_KABUTO_EXPLOSION_ONME("&fVous vous êtes fait &cexplosé &fpar &6Ussop&f."),
    USSOP_KABUTO_AVEUGLE_USE("&fVous avez créé une &aaveuglé &6<name>&f."),
    USSOP_KABUTO_AVEUGLE_ONME("&fVous vous êtes fait &aaveuglé &fpar &6Ussop&f."),

    SANJI_KILLEDFEMALE("&fVous avez perdu &c3 coeurs &fcar vous avez tué un rôle &aféminin&f."),
    SANJI_STAYED1MINUTE("&fVous êtes resté &a1 minute &fà côté d'un rôle féminin, vous perdez &c1 coeur&f."),
    SANJI_DIABLEJAMBE_USE("&fVous avez utilisé votre pouvoir &aDiable Jambe&f."),
    SANJI_DIABLEJAMBE_END("&fVotre pouvoir &aDiable Jambe&f a été &cdésactivé&f."),
    SANJI_OSOBAMASK_USE("&fVous avez utilisé votre pouvoir &cO Soba Mask&f."),
    SANJI_OSOBAMASK_DESACTIVATE("&fVous avez &cdésactivé&f votre pouvoir &cO Soba Mask&f."),

    CHOPPER_FLAIRE_FIRSTLINE("&fEffets de potions de &a<name>&f:"),
    CHOPPER_FLAIRE_FORMAT(" &8- <name> &8(&7<amplifier>&8) &f&l» &f<duration>"),

    CHOPPER_FORME_ONE_DESC("Ligne 1 \nLigne 2"),
    CHOPPER_FORME_ONE_MATERIAL("GOLDEN_APPLE"),
    CHOPPER_FORME_TWO_DESC("Ligne 1 \nLigne 2"),
    CHOPPER_FORME_TWO_MATERIAL("GOLDEN_APPLE"),
    CHOPPER_FORME_THREE_DESC("Ligne 1 \nLigne 2"),
    CHOPPER_FORME_THREE_MATERIAL("GOLDEN_APPLE"),
    CHOPPER_FORME_FOUR_DESC("Ligne 1 \nLigne 2"),
    CHOPPER_FORME_FOUR_MATERIAL("GOLDEN_APPLE"),
    CHOPPER_FORME_USE("&fVous avez selectionné le pouvoir &a<name>&f."),

    ROBIN_OEIL_USE("&fVous avez placé un &aoeil&f."),
    ROBIN_OEIL_JOIN("&a<name> &fvient de rentrer dans une de vos zones."),
    ROBIN_OEIL_POWER("&c<name> &fa utilisé un pouvoir dans une de vos zones."),
    ROBIN_OEIL_GAPPLE("&c<name> &fa mangé une pomme d'or dans une de vos zones."),
    ROBIN_OEIL_COMBAT("&c<name> &fa attaqué un joueur."),

    ROBIN_CLUTCH_USE("&fVous avez utilisé votre pouvoir &aClutch &fsur &a<name>&f."),
    ROBIN_CLUTCH_ONME("&aNico Robin &fvous a retourné. &8(&710 secondes&8)"),

    FRANKY_VISION_USE("&fVous traquez désormais &c<name>&f."),
    FRANKY_GENERAL_USE("&fVous avez utilisé votre pouvoir &aGénéral Franky&f."),
    FRANKY_GENERAL_END("&fVotre pouvoir &aGénéral Franky&f a été &cdésactivé&f."),
    FRANKY_GENERAL_DEATHEND("&fVous avez perdu &c5 coeurs&f comme vous êtes restez plus de 5 secondes à côté de la mort de Franky."),

    FRANKY_GENERAL_NEWPOWER_TNT_GET("&fVous avez obtenu le pouvoir &cTnT&f."),
    FRANKY_GENERAL_NEWPOWER_TNT_USE("&fVous avez utilisé le pouvoir &cTnT&f."),
    FRANKY_GENERAL_NEWPOWER_ASHIMOTO_GET("&fVous avez obtenu le pouvoir &cGénéral Ashimoto Dangereux&f."),
    FRANKY_GENERAL_NEWPOWER_ASHIMOTO_USE("&fVous avez utilisé le pouvoir &cGénéral Ashimoto Dangereux&f."),
    FRANKY_GENERAL_NEWPOWER_BOUCLIER_GET("&fVous avez obtenu le pouvoir &cTeinture grise ( Bouclier )&f."),
    FRANKY_GENERAL_NEWPOWER_BOUCLIER_USE("&fVous avez utilisé le pouvoir &cTeinture grise ( Bouclier )&f."),

    BROOK_DEATH("&fVous êtes mort a allez &arespawn&f dans 5 minutes."),
    BROOK_RESPAWN("&FHAHAHHAHAH JE VIENS DE RESPWAN EZ HAHAHAHAHAH :slurp:."),
    BROOK_AME_USE("&fVous avez utilisé votre pouvoir &aAme&f."),
    BROOK_AME_END("&fVous avez regagné votre forme &cnormale&f."),
    BROOK_AME_DEATH("&fVotre squelette est mort docn tu meurs&f."),
    BROOK_AME_CANNOTMOVE("&fVous avez été &cimmobilisé &fpar &cBrook&f."),

    BROOK_SLOW_USE("&fVous avez utilisé votre pouvoir de &cslow &fsur &c<name>&f."),
    BROOK_SLOW_ATTACK("&fVous avez frappé votre cible &c<name>&f."),
    BROOK_SLOW_ATTACKONME("&cBrook&f a utilisé son pouvoir de &7Slow &fsur vous."),
    BROOK_SLOW_ATTACKONME_END("&cVous perdez la moitié de votre vie."),

    JIMBE_SEIKEN_USE("&fVous avez utilisé votre &9Seiken&f sur &c<name>&f."),
    JIMBE_SEIKEN_ONME("&cJimbe &fa utilisé son &9Seiken &fsur vous."),

    GARP_DEATH_PVE("&cSengoku &fest mort de PVE."),
    GARP_DEATH_PLAYER("&cSengoku &fa été tué par &c<name>&f."),
    GARP_TNT_USE("&fVous avez fait créé une &cexplosion&f."),

    XDRAKE_ALLOSAURUS_ACTIVATE("&fVous avez activé votre pouvoir &aAllosaurus&f."),
    XDRAKE_MORSURE_VICTIM("&c<name>, &fvous avez été mordu par &cX-Drake&f."),

    LIEUTENANT_COMMANDANT_DEATH("&c<name> &fest mort, c'était le commandant."),
    LIEUTENANT_FIVE_ROLES("<name> est <role>"),


    COBY_PHASE_CHANGE("&fVous êtes passé à la phase &c<phase>&f."),
    COBY_DEATH_COORDINATES("&c<name> &fest mort. Voici ses coordonnées approximatives: &c<x>&f, &c<y>&f, &c<z>&f."),
    COBY_DEATHER_KILLER_ROLE("&fLe rôle du tueur de &c<name> &fest &c<role>&f."),
    COBY_DEATHER_KILLER_NAME("&fLe nom du tueur de &c<name> &fest &c<killer>&f."),

    COMMANDANT_GUERRE_USE("&fVous avez utilisé votre pouvoir &aGuerre&f."),
    COMMANDANT_GUERRE_COORDINATES("&cCommandant &fse trouve aux coordonnées &c<x>&f, &c<y>&f, &c<z>&f."),
    COMMANDANT_WANTED_TARGET("&fVous avez été recherché par le &cCommandant&f."),
    COMMANDANT_WANTED_NOTPIRATE("&fVotre joueur recherché n'est pas un pirate. Vous perdez &c2 coeurs&f."),
    COMMANDANT_WANTED_FIRST("&fVous obtenez &c3 coeurs&f permanent et l'effet &cforce 1 &fpermament."),
    COMMANDANT_WANTED_SECOND("&fVous obtenez &c2 coeurs&f permanent et l'effet &cforce 1 &fle jour."),
    COMMANDANT_WANTED_THIRD("&fVous obtenez &c2 coeurs&f permanent."),

    SMOKER_WHITESPARKS_USE("&fVous avez utilisé votre pouvoir &aWhitespark&f."),
    SMOKER_WHITESPARKS_END("&fVotre pouvoir &aWhitespark&f a été &cdésactivé&f."),
    SMOKER_WHITESPARKS_TARGET("&cSmoker &fa utilisé son pouvoir &aWhitespark&f sur vous."),
    SMOKER_GAZ_USE("&fVous avez utilisé votre pouvoir &cSmoke &fsur &c<name>&f."),
    SMOKER_GAZ_TARGET("&cSmoker &fa utilisé son pouvoir &cSmoke &fsur vous."),

    ;

    @Setter
    private String display;

    @SneakyThrows
    public static void init() {
        Mugiwara.getInstance().getConfig();

        FileConfiguration config = Mugiwara.getInstance().getConfig();
        config.load(new File(Mugiwara.getInstance().getDataFolder() + "/config.yml"));

        for (Messages value : values()) {
            if (config.get("messages." + value.name().replace("_", ".")) == null) {
                config.set("messages." + value.name().replace("_", "."), value.getDisplay());
                Mugiwara.getInstance().saveConfig();
            }

            value.setDisplay(config.getString("messages." + value.name().replace("_", ".")));
        }
    }

    public void send(Player player, Replacement... replacements) {
        if (getDisplay().equals("false")) return;

        String message = ChatUtil.prefix(getDisplay());
        for (Replacement replacement : replacements) {
            message = message.replace(replacement.getIndex(), String.valueOf(replacement.getReplace()));
        }

        if (display.equalsIgnoreCase("false") || display.equalsIgnoreCase("rien")) return;

        player.sendMessage(message);
    }

    public void sendNP(Player player, Replacement... replacements) {
        if (getDisplay().equals("false")) return;

        String message = ChatUtil.translate(getDisplay());
        for (Replacement replacement : replacements) {
            message = message.replace(replacement.getIndex(), String.valueOf(replacement.getReplace()));
        }

        if (display.equalsIgnoreCase("false") || display.equalsIgnoreCase("rien")) return;

        player.sendMessage(message);
    }

}

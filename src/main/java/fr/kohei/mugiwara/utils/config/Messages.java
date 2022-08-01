package fr.kohei.mugiwara.utils.config;

import fr.kohei.mugiwara.Mugiwara;
import fr.kohei.mugiwara.roles.RolesType;
import fr.kohei.utils.ChatUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Getter
@AllArgsConstructor
public enum Messages {

    COOLDOWN_EXPIRE("&fLe cooldown pour le pouvoir &c<name> &fvient d'expirer."),
    WATER("&cNe restez pas trop longtemps dans l'eau."),

    TRESOR_SPAWN("&fDes &atrésors &fsont apparus sur la carte."),

    SECTION_GIVE_GARP("&fVous avez reçu le badge de section &0&lGarp&f."),
    SECTION_GIVE_KIZARU("&fVous avez reçu le badge de section &e&lKizaru&f."),
    SECTION_GIVE_SMOKER("&fVous avez reçu le badge de section &7&lSmoker&f."),
    SECTION_GIVE_AKAINU("&fVous avez reçu le badge de section &c&lAkainu&f."),

    VIVECARD_LUFFY_DEATH("&cLuffy &fest mort, de ce fait vous perdez votre &aVive Card&f."),
    VIVECARD_LUFFY_GIVE("&fVous avez donné votre &cVivre Card&f à &c<name>&f."),
    VIVECARD_LUFFY_RECEIVE("&cLuffy &fvous a donné sa &aVivre Card&f."),
    VIVECARD_LUFFY_HEALTH("&cLuffy &fest à moins de &c5 coeurs&f."),

    VIVECARD_BIGMOM_DEATH("&cBigMom &fest mort, de ce fait vous perdez votre &aVive Card&f."),
    VIVECARD_BIGMOM_GIVE("&fVous avez donné votre &cVivre Card&f à &c<name>&f."),
    VIVECARD_BIGMOM_RECEIVE("&cBigMom &fvous a donné sa &aVivre Card&f."),
    VIVECARD_BIGMOM_HEALTH("&cBigMom &fest à moins de &c5 coeurs&f."),

    PONEGLYPHE_SPAWN("&fUn &aponéglyphe &fvient d'apparaître."),
    PONEGLYPHE_TOOMUCHPLAYERS("&cVous ne pouvez pas lire le ponéglyphe, il y a plus de 5 joueurs autour."),
    PONEGLYPHE_FAR("&cVous avez arrêté de lire le ponéglyphe numéro <number>, vous êtes parti trop loin."),
    PONEGLYPHE_CAPTURE("&fVous avez &acapturé le ponéglyphe numéro &f<number>&f."),

    DENDEN_MUSHI_SEND("&fVous avez envoyé une demande &cd'appel&f à &c<name>&f."),
    DENDEN_MUSHI_RECEIVE("&c<name> &fvous a fait un appel avec son &aDenDenMushi&f."),
    DENDEN_MUSHI_ACCEPT("&a&l[ACCEPTER]"),
    DENDEN_MUSHI_NO_REQUEST("&cVous n'avez pas d'appel en attente de ce joueur."),

    DENDEN_MUSHI_ACCEPTED_ASKER("&c<name> &fa accepté votre demande d'appel."),
    DENDEN_MUSHI_ACCEPTED_TARGET("&fVous avez &aaccepté &fla demande d'appel de &c<name>&f."),
    DENDEN_MUSHI_REFUSED_ASKER("&c<name> &fa refusé votre demande d'appel."),
    DENDEN_MUSHI_REFUSED_TARGET("&fVous avez &crefusé &fla demande d'appel de &c<name>&f."),

    DENDEN_MUSHI_CHAT_END("&fVous ne pouvez désormais plus &ccommuniquer &favec la commande &a/mu chat&f."),
    DENDEN_MUSHI_CHAT_FORMAT("&7[&aChat&7] &a<name>&f: &f<message>"),

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
    NAMI_ZEUS_FINISH("&cVotre pouvoir a expiré."),
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

    KAIDO_SBIRE_USE("&fVous avez utilisé votre pouvoir de &csbire &fsur &c<name>&f."),
    KAIDO_SBIRE_TRANSFORMED("&fKaido vous a fait migré dans Alliance."),
    KAIDO_SBIRE_3K_REACHED("&fProgression désormais à 3000 points."),
    KAIDO_SBIRE_BORO_USED("&fVous venez d'utiliser 1 balle de feu."),

    BROOK_DEATH("&fVous êtes mort et allez &arespawn&f dans 5 minutes."),
    BROOK_RESPAWN("&fjviens de respawn"),
    BROOK_AME_USE("&fVous avez utilisé votre pouvoir &aAme&f."),
    BROOK_AME_END("&fVous avez regagné votre forme &cnormale&f."),
    BROOK_AME_DEATH("&fVotre squelette est mort docn tu meurs&f."),
    BROOK_AME_CANNOTMOVE("&fVous avez été &cimmobilisé &fpar &cBrook&f."),

    BROOK_SLOW_USE("&fVous avez utilisé votre pouvoir de &cslow &fsur &c<name>&f."),
    BROOK_SLOW_ATTACK("&fVous avez frappé votre cible &c<name>&f."),
    BROOK_SLOW_ATTACKONME("&cBrook&f a utilisé son pouvoir de &7Slow &fsur vous."),
    BROOK_SLOW_ATTACKONME_END("&cVous perdez la moitié de votre vie."),

    KUMA_BIBLE_PLAYER_USE("&fVous avez utilisé votre pouvoir &aBible&f."),
    KUMA_BIBLE_TELEPORT_TARGET_USE("&fVous avez été &atéléporté &fpar &cKuma&f."),

    KUMA_VOYAGE_PLAYER_USE("&fVous avez utilisé votre pouvoir &aVoyage&f."),
    KUMA_VOYAGE_TARGET_TELEPORTED("&fVous avez été &atéléporté &fpar &cKuma&f."),

    JIMBE_SEIKEN_USE("&fVous avez utilisé votre &9Seiken&f sur &c<name>&f."),
    JIMBE_SEIKEN_ONME("&cJimbe &fa utilisé son &9Seiken &fsur vous."),

    GARP_DEATH_PVE("&cSengoku &fest mort de PVE."),
    GARP_DEATH_PLAYER("&cSengoku &fa été tué par &c<name>&f."),
    GARP_TNT_USE("&fVous avez fait créé une &cexplosion&f."),
    GARP_COBY_PHASE("&cCoby &fvient de changer de phase."),

    KING_PTERANODON_USE("&fVous avez utilisé votre pouvoir &aPteranodon&f."),
    KING_PTERANODON_END("&fVotre pouvoir &aPteranodon&f a été &cdésactivé&f."),
    KING_PTERANODON_ONME("&cKing &fa utilisé son pouvoir &aPteranodon&f sur vous. Vous êtes donc enflammé."),
    KING_FLY_USE("&fVous avez utilisé votre pouvoir &aFly&f."),

    XDRAKE_ALLOSAURUS_ACTIVATE("&fVous avez activé votre pouvoir &aAllosaurus&f."),
    XDRAKE_MORSURE_VICTIM("&c<name>, &fvous avez été mordu par &cX-Drake&f."),

    LIEUTENANT_COMMANDANT_DEATH("&fLe &acommandant &fvient de mourir. Vous obtenez &cRésistance&f."),
    LIEUTENANT_RECHERCHE_TARGETS("&fVoici la liste des 3 pseudos: &c<names>&f."),
    LIEUTENANT_RECHERCHE_KILLER("&fCe joueur &cn'est pas &fle tueur du dernier mort."),
    LIEUTENANT_RECHERCHE_NOTKILLER("&fCe joueur &aest &fle tueur du dernier mort."),

    KATAKURI_ROLES_REVEAL("&fLe rôle de <name> est : &a<role>&f."),

    COBY_PHASE_CHANGE("&fVous êtes passé à la phase &c<phase>&f."),
    COBY_DEATH_COORDINATES("&c<name> &fest mort. Voici ses coordonnées approximatives: &c<x>&f, &c<y>&f, &c<z>&f."),
    COBY_DEATHER_KILLER_ROLE("&fLe rôle du tueur de &c<name> &fest &c<role>&f."),
    COBY_DEATHER_KILLER_NAME("&fLe nom du tueur de &c<name> &fest &c<killer>&f."),
    COBY_DENDENMUSHI_ENABLE("&fUn joueur vient d'utiliser son pouvoir de &aDenDen Mushi&f en &c<x>&f, &c<y>&f, &c<z>&f."),
    COBY_DENDENMUSHI_SEE("&a&l[VOIR LA CONVERSATION]"),
    COBY_DENDENMUSHI_WILLSEE("&fVous allez voir la conversation du &cjoueur&f."),

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

    AKAINU_FIOLE_USE("&fVous avez utilisé votre pouvoir &aFiole&f."),
    AKAINU_FIOLE_END("&fVotre pouvoir &aFiole&f a été &cdésactivé&f."),
    AKAINU_DAI_USE("&fVous avez utilisé votre pouvoir &aDai&f."),
    AKAINU_DAI_END("&fVotre pouvoir &aDai&f a été &cdésactivé&f."),
    AKAINU_FIRE_ENABLE("&fVous avez activé votre pouvoir &cFire&f."),
    AKAINU_FIRE_DISABLE("&fVotre pouvoir &cFire&f a été &cdésactivé&f."),

    KIZARU_YATA_USE("&fVous avez utilisé votre pouvoir &aYata&f sur &c<name>&f."),
    KIZARU_YASAKANI_USE("&fVous avez utilisé votre pouvoir &aYasakani&f."),
    KIZARU_YASAKANI_END("&fVotre pouvoir &aYasakani&f a été &cdésactivé&f."),

    SOLDAT_CHOICE_ONE_MATERIAL("DIAMOND_SWORD"),
    SOLDAT_CHOICE_ONE_LORE("ligne 1 \nligne 2"),
    SOLDAT_CHOICE_ONE_SELECT("&fVous avez &achoisi&f le choix numéro 1."),
    SOLDAT_CHOICE_TWO_MATERIAL("IRON_CHESTPLATE"),
    SOLDAT_CHOICE_TWO_LORE("ligne 1 \nligne 2"),
    SOLDAT_CHOICE_TWO_SELECT("&fVous avez &achoisi&f le choix numéro 2."),
    SOLDAT_CHOICE_THREE_MATERIAL("PAPER"),
    SOLDAT_CHOICE_THREE_LORE("ligne 1 \nligne 2"),
    SOLDAT_CHOICE_THREE_SELECT("&fVous avez &achoisi&f le choix numéro 3."),

    JACK_MAMMOUTH_USE("&fVous avez utilisé votre pouvoir &aMammouth&f."),
    JACK_MAMMOUTH_TARGET("&fVous avez été &cpropulsé&f par &c<name>&f."),

    JACK_EXTERMIN_END("&fVotre pouvoir &aExtermin&f a été &cdésactivé&f."),
    JACK_EXTERMIN_USE("&fVous avez &aactivé votre pouvoir &aExtermin&f."),
    JACK_EXTERMIN_TARGET("&fVous êtes rentré dans une zone de &cJack&f."),
    JACK_EXTERMIN_POISON_TARGET("&fVous avez été empoisonné par &cJack&f."),

    LAW_HEAL_USE("&fVous avez &asoigné &fle joueur &a<name>&f."),
    LAW_HEAL_TARGET("&fVous avez été &asoigné &fpar &cLaw&f."),

    LAW_MES_USE("&fVous avez utilisé votre pouvoir &aMes&f sur &c<name>&f."),
    LAW_MES_TARGET("&cLaw &fvous a fait perdre &c3 coeurs &fpermanents."),
    LAW_MES_DEATH("&cLaw &fest mort. De ce fait vous récupérez &c3 coeurs &fpermanents."),

    LAW_ROOM_CREATE("&fVous avez &créé&f une room de &c<block> blocks&f."),

    HANCOCK_FEMUR_ONME("&cBoa Hancock&f vous a attaqué avec son &cFemur&f."),
    HANCOCK_FEMUR_TORSE("&fVous avez frappé &c<name>&f avec votre &aFemur&f sur son &aTorse&f."),
    HANCOCK_FEMUR_JAMBES("&fVous avez frappé &c<name>&f avec votre &aFemur&f sur son &aJambe&f."),
    HANCOCK_FEMUR_TETE("&fVous avez frappé &c<name>&f avec votre &aFemur&f sur sa &aTête&f."),
    HANCOCK_FEMUR_SANJI_USE("&fVous avez attaqué &cSanji &fet avez fait apparaître une boule de pierre pendant &c15 secondes&f."),
    HANCOCK_FEMUR_SANJI_TARGET("&cBoa Hancock &fvous a attaqué avec son &cFemur&f et a fait apparaître une boule de pierre pendant &c15 secondes&f."),

    HANCOCK_AMOUR_USE("&fVous avez rendu amoureux &c<name>&f de vous."),
    HANCOCK_AMOUR_TARGET("&fVous êtes désormais amoureux de &c<name>&f."),
    HANCOCK_AMOUR_SANJI("&fVous ne pouvez plus taper &cBoa Hancock&f jusqu'à la fin de la partie."),

    MIHAWK_SWORD_USE("&fVous avez envoyé une &alame d'air&f."),
    MIHAWK_SWORD_TARGET("&fVous avez été touché par une lame d'air de &aMihawk&f."),
    MIHAWK_YORU_USE("&fVous avez utilisé votre pouvoir &aYoru&f."),
    MIHAWK_YORU_CANHIT("&fLa prochaine personne que vous toucherez dans les &c30 &fprochaine seconds prendra &c<damage> &fpoints de dégâts."),
    MIHAWK_YORU_THEHIT("&fVous avez infligé &c<damage> &fpoints de dégâts à &c<name>&f."),

    TEACH_KAISHIN_EVERYONE("&fLe sol tremble sous vos pieds."),
    TEACH_KAISHIN_USE("&fVous avez utilisé votre pouvoir &aKaishin&f."),
    TEACH_BLACKHOLE_USE("&fVous avez utilisé votre pouvoir &aBlackhole&f."),
    TEACH_BLACKHOLE_END("&fVotre pouvoir &aBlackhole&f a été &cdésactivé&f."),
    TEACH_BLACKHOLE_TARGET("&fVous avez été touché par un &cblackhole&f."),

    TSURU_KILLER_COORDINATES("&fVoici les coordonnées du tueur de &c<role>&f : &c<x>&f, &c<y>&f, &c<z>&f."),
    TSURU_WHOSHU_USE("&fVous avez jeté un &aWhoshu&f."),
    TSURU_WHOSHU_HIT("&fVous avez été touché par le &aWhoshu&f de &cTsuru."),
    TSURU_WOSHI_USE("&fVous avez utilisé votre pouvoir &aWoshi&f."),
    TSURU_WOSHI_HIT("&fVous avez été touché par le &aWoshi&f de &cTsuru."),

    SABO_FIRE_ON("&fVous avez activé votre pouvoir &aFire&f."),
    SABO_FIRE_OFF("&fVotre pouvoir &aFire&f a été &cdésactivé&f."),

    SABO_PACTE_ONE_MATERIAL("DIAMOND_SWORD"),
    SABO_PACTE_ONE_NAME("&cPacte N°1"),
    SABO_PACTE_ONE_LORE("ligne 1 \nligne 2"),
    SABO_PACTE_ONE_SELECT("&fVous avez &achoisi&f le pacte numéro 1."),

    SABO_PACTE_TWO_MATERIAL("DIAMOND_SWORD"),
    SABO_PACTE_TWO_NAME("&cPacte N°2"),
    SABO_PACTE_TWO_LORE("ligne 1 \nligne 2"),
    SABO_PACTE_TWO_SELECT("&fVous avez &achoisi&f le pacte numéro 2."),

    SABO_PACTE_THREE_MATERIAL("DIAMOND_SWORD"),
    SABO_PACTE_THREE_NAME("&cPacte N°3"),
    SABO_PACTE_THREE_LORE("ligne 1 \nligne 2"),
    SABO_PACTE_THREE_SELECT("&fVous avez &achoisi&f le pacte numéro 3."),

    SABO_PACTE_LUFFYCOMBAT("&cLuffy &fvient de rentrer en combat."),
    SABO_PACTE_AKAINUDEATH("&cAkaín &fvient de mourir. Vous obtenez force permanent."),
    SABO_PACTE_KUMA("&fVous voyez désormais la &cvie &fdes joueurs."),
    SABO_PACTE_SABO("&fVous voyez désormais la &cvie &fdes joueurs."),
    SABO_BLAZE_USE("&fVous avez utilisé votre pouvoir &aBlaze&f."),
    SABO_BLAZE_TARGET("&fVous avez été touché par le &aBlaze&f de &cSabo."),
    SABO_BLAZE_SMOKER("&cVotre pouvoir a été annulé car vous avez utilisé votre pouvoir sur Smoker."),
    SABO_DUO_CHAT("&c<role>&7: &f<message>"),

    SENGOKU_GARPDEATH_KILLER("&cGarp &fa été tué par &c<name>&f."),
    SENGOKU_GARPDEATH_PVE("&cGarp &fest mort tout seul."),

    FUJITORA_MOKO_USE("&fVous avez utilisé votre pouvoir &aMoko&f."),
    FUJITORA_MOKO_TARGET("&fVous avez été touché par le &aMoko&f de &cFujitora."),
    FUJITORA_METEORITE_USE("&fVous avez utilisé votre pouvoir &aMeteorite&f."),
    FUJITORA_METEORITE_TARGET("&fVous avez été touché par le &aMeteorite&f de &cFujitora."),
    FUJITORA_AURA_BLEU("&fCette personne a une aura &9bleue&f."),
    FUJITORA_AURA_ROUGE("&fCette personne a une aura &crouge&f."),

    BIGMOM_NAMI_ZEUS("&fVous avez &arécupéré &fle &cZeus &fde Nami."),
    BIGMOM_SOUL_USE("&fVous avez utilisé votre pouvoir &aSoul&f."),
    BIGMOM_SOUL_TARGET("&fVous avez été touché par le &aSoul&f de &cBigMom."),
    BIGMOM_SOULEND_BIGMOM("&fVous avez &asélectionné &fle même &cgateaux &fque &a<name>&f."),
    BIGMOM_SOULEND_TARGET("&fVous avez &asélectionné &fle même &cgateaux &fque &aBigMom&f."),
    BIGMOM_SOULEND_BIGMOMDIDNTSELECT("&fBig mom n'a pas selectionné une gateau"),
    BIGMOM_PROMETHEE_USE("&fVous avez utilisé votre pouvoir &aPromethee&f."),
    ;

    @Setter
    private String display;

    public static final HashMap<RolesType, List<String>> DESCRIPTION = new HashMap<>();

    @SneakyThrows
    public static void init() {
        Mugiwara.getInstance().getConfig();

        FileConfiguration config = Mugiwara.getInstance().getConfig();
        config.load(new File(Mugiwara.getInstance().getDataFolder() + "/config.yml"));

        for (RolesType role : RolesType.values()) {
            if (config.get("roles." + role.name() + ".display") == null) {
                config.set("roles." + role.name() + ".display", new ArrayList<>(Arrays.asList("&aLigne 1", "&cLigne 2", "&betc")));
                Mugiwara.getInstance().saveConfig();
            }
            List<String> display = config.getStringList("roles." + role.name() + ".display");
            DESCRIPTION.put(role, display);
        }

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
            message = message.replace(replacement.getIndex(), ChatUtil.translate(String.valueOf(replacement.getReplace())));
        }

        if (display.equalsIgnoreCase("false") || display.equalsIgnoreCase("rien")) return;

        player.sendMessage(message);
    }

    public void sendNP(Player player, Replacement... replacements) {
        if (getDisplay().equals("false")) return;

        String message = ChatUtil.translate(getDisplay());
        for (Replacement replacement : replacements) {
            message = message.replace(replacement.getIndex(), ChatUtil.translate(String.valueOf(replacement.getReplace())));
        }

        if (display.equalsIgnoreCase("false") || display.equalsIgnoreCase("rien")) return;

        player.sendMessage(message);
    }

}

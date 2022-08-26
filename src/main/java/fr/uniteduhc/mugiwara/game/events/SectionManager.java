package fr.uniteduhc.mugiwara.game.events;

import fr.uniteduhc.mugiwara.Mugiwara;
import fr.uniteduhc.mugiwara.camp.CampType;
import fr.uniteduhc.mugiwara.game.player.MUPlayer;
import fr.uniteduhc.mugiwara.roles.RolesType;
import fr.uniteduhc.mugiwara.utils.config.Messages;
import fr.uniteduhc.mugiwara.utils.utils.Utils;
import fr.uniteduhc.mugiwara.utils.utils.packets.Damage;
import fr.uniteduhc.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class SectionManager {

    private static final ItemBuilder BADGE_SECTION_AKAINU = new ItemBuilder(Material.INK_SACK).setDurability((short) 14).setName(Utils.notClickItem("&c&lSection Akainu"));
    // black dye
    private static final ItemBuilder BADGE_SECTION_GARP = new ItemBuilder(Material.INK_SACK).setDurability((short) 10).setName(Utils.notClickItem("&0&lSection Garp"));
    // yellow dye
    private static final ItemBuilder BADGE_SECTION_KIZARU = new ItemBuilder(Material.INK_SACK).setDurability((short) 11).setName(Utils.notClickItem("&e&lSection Kizaru"));
    // gray dye
    private static final ItemBuilder BADGE_SECTION_SMOKER = new ItemBuilder(Material.INK_SACK).setDurability((short) 8).setName(Utils.notClickItem("&7&lSection Smoker"));

    public void giveBadge() {
        final List<Player> players = this.getBadgePlayers();
        final List<ItemBuilder> items = new ArrayList<>(Arrays.asList(BADGE_SECTION_AKAINU, BADGE_SECTION_GARP, BADGE_SECTION_KIZARU, BADGE_SECTION_SMOKER));
        Collections.shuffle(items);
        final Player pseudoPlayer = players.get(new Random().nextInt(players.size()));
        for (final Player player : players) {
            final RolesType.MURole role = MUPlayer.get(player).getRole();
            if (items.isEmpty()) {
                break;
            }
            for (ItemBuilder item = null; item == null || (item == BADGE_SECTION_SMOKER && role.getRole() == RolesType.HANCOCK); item = items.get(0)) {
                Collections.shuffle(items);
            }
            ItemBuilder item = items.remove(0);
            player.getInventory().addItem(item.toItemStack());
            if (pseudoPlayer.getUniqueId().equals(player.getUniqueId())) {
                if (item == BADGE_SECTION_GARP) {
                    Mugiwara.knowsRole(player, RolesType.GARP);
                } else if (item == BADGE_SECTION_KIZARU) {
                    Mugiwara.knowsRole(player, RolesType.KIZARU);
                } else if (item == BADGE_SECTION_SMOKER) {
                    Mugiwara.knowsRole(player, RolesType.SMOKER);
                } else {
                    Mugiwara.knowsRole(player, RolesType.AKAINU);
                }
            }
            if (item == BADGE_SECTION_GARP) {
                Messages.SECTION_GIVE_GARP.send(player);

            } else if (item == BADGE_SECTION_KIZARU) {
                Messages.SECTION_GIVE_KIZARU.send(player);
                player.setWalkSpeed(0.21F);
            } else if (item == BADGE_SECTION_SMOKER) {
                Damage.addNoDamage(player.getUniqueId(), EntityDamageEvent.DamageCause.FALL);
                Messages.SECTION_GIVE_SMOKER.send(player);
            } else {
                player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
                Messages.SECTION_GIVE_AKAINU.send(player);
            }
        }
    }

    private List<Player> getBadgePlayers() {
        final List<Player> list = new ArrayList<>();
        if (new Random().nextInt(100) < 35) {
            list.addAll(Utils.getPlayersWithRole(RolesType.SABO));
        }
        final List<Player> players = Utils.getPlayersInCamp(CampType.MARINE);
        final List<Player> notPlayers = Utils.getPlayersWithRole(RolesType.DRAKE, RolesType.GARP, RolesType.AKAINU, RolesType.SMOKER, RolesType.KIZARU, RolesType.SENGOKU);
        players.removeIf(notPlayers::contains);
        while (list.size() < 4 && !players.isEmpty()) {
            list.add(players.remove(0));
        }
        return list;
    }

}

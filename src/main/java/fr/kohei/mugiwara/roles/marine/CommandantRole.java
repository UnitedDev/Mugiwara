package fr.kohei.mugiwara.roles.marine;

import fr.kohei.mugiwara.Mugiwara;
import fr.kohei.mugiwara.camp.CampType;
import fr.kohei.mugiwara.utils.config.Messages;
import fr.kohei.mugiwara.game.player.MUPlayer;
import fr.kohei.mugiwara.power.impl.ChecklistPower;
import fr.kohei.mugiwara.power.impl.GuerrePower;
import fr.kohei.mugiwara.roles.RolesType;
import fr.kohei.mugiwara.utils.utils.Utils;
import fr.kohei.uhc.game.player.UPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;

public class CommandantRole extends RolesType.MURole implements Listener {
    private boolean strengthAtDay;

    public CommandantRole() {
        super(Arrays.asList(
                new GuerrePower(),
                new ChecklistPower()
        ));
    }

    @Override
    public RolesType getRole() {
        return RolesType.COMMANDANT;
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(Material.DIAMOND);
    }

    @Override
    public void onDistribute(Player player) {
        // give the player an iron sword with damage_all enchantment 4 and a bow with arrow_damage enchantment 2
        ItemStack is = new ItemStack(Material.IRON_SWORD);
        is.addUnsafeEnchantment(org.bukkit.enchantments.Enchantment.DAMAGE_ALL, 4);
        ItemStack bow = new ItemStack(Material.BOW);
        bow.addUnsafeEnchantment(org.bukkit.enchantments.Enchantment.ARROW_DAMAGE, 2);
        player.getInventory().addItem(is, bow);

        // give player damage_resistance effect permanenty
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
    }

    @Override
    public void onEpisode(Player player) {
        // random between 5*60 and 15*60 seconds
        int random = (int) (Math.random() * (15 * 60 - 5 * 60)) + 5 * 60;
        // run task later with the value of the random number
        new BukkitRunnable() {
            @Override
            public void run() {
                int count = 0;
                // get the nearest players with a 50 blocks radius (with the roles Kaido, big mom, teach, luffy, queen, katakuri, sabo, law, jimbe, sanji, zoro, x drake, ussop, kuma, jack and kid) and loop them
                for (Player p : Utils.getNearPlayersWithRole(player, 50, RolesType.KAIDO, RolesType.BIG_MOM, RolesType.TEACH,
                        RolesType.LUFFY, RolesType.QUEEN, RolesType.KATAKURI, RolesType.SABO, RolesType.LAW, RolesType.JIMBE,
                        RolesType.SANJI, RolesType.ZORO, RolesType.DRAKE, RolesType.USSOP, RolesType.KUMA, RolesType.JACK, RolesType.EUSTASS)) {
                    count++;

                    RolesType role = MUPlayer.get(p).getRole().getRole();
                    // if the role is luffy, zoro or sanji, make the player p know the role of Commandant (Mugiwara.knowsRole to know a role)
                    if (role == RolesType.LUFFY || role == RolesType.ZORO || role == RolesType.SANJI) {
                        Mugiwara.knowsRole(p, RolesType.COMMANDANT);
                    }

                    // if the role is luffy, all the players with the camp MUGIWARA_HEART will know the role of Commandant
                    if (role == RolesType.LUFFY) {
                        for (Player p2 : Utils.getPlayersInCamp(CampType.MUGIWARA_HEART)) {
                            Mugiwara.knowsRole(p2, RolesType.COMMANDANT);
                        }
                    }
                }
            }
        }.runTaskLater(Mugiwara.getInstance(), random);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        // get the ChecklistPower by filtering the powers of the player
        ChecklistPower checklistPower = (ChecklistPower) getPowers().stream()
                .filter(power -> power instanceof ChecklistPower)
                .findFirst()
                .orElse(null);

        // if the checklist power is null return
        if (checklistPower == null) return;

        // if the target uuid of the checklist power is null return
        if (checklistPower.getTargetUuid() == null) return;

        // if the target uuid of the checklist power is not the same as the player uuid return
        if (!checklistPower.getTargetUuid().equals(event.getEntity().getUniqueId())) return;

        // get the target of the checklist power
        Player target = Bukkit.getPlayer(checklistPower.getTargetUuid());

        // variable with the camp type of the target
        CampType campType = ((CampType.MUCamp) UPlayer.get(target).getCamp()).getCampType();

        // player is the player of the superclass
        Player player = getPlayer();

        // if player is null return
        if (player == null) return;

        // if the camp is marine remove 2 permanent hearts from the player and send him the commandant wanted not pirate message and return
        if (campType == CampType.MARINE) {
            player.setHealth(player.getHealth() - 2);
            Messages.COMMANDANT_WANTED_NOTPIRATE.send(player);
            return;
        }

        // rolestype of the target
        RolesType rolesType = MUPlayer.get(target).getRole().getRole();

        // if the role is kaido, big mom, teach or luffy, give the player 3 permament hearts, give him increase_damage potion effect and send him the commandant wanted first message
        if (rolesType == RolesType.KAIDO || rolesType == RolesType.BIG_MOM || rolesType == RolesType.TEACH || rolesType == RolesType.LUFFY) {
            player.setHealth(player.getHealth() + (3 * 2));
            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));
            Messages.COMMANDANT_WANTED_FIRST.send(player);
        }

        // if the role is zoro, sanji, jimbe, law, king, queen, katakuri, sabo give the player 2 permanent hearts, enable the strength at day power and send him the commandant second message
         else if (rolesType == RolesType.ZORO || rolesType == RolesType.SANJI || rolesType == RolesType.JIMBE || rolesType == RolesType.LAW || rolesType == RolesType.KING || rolesType == RolesType.QUEEN || rolesType == RolesType.KATAKURI || rolesType == RolesType.SABO) {
            player.setHealth(player.getHealth() + (2 * 2));
            strengthAtDay = true;
            Messages.COMMANDANT_WANTED_SECOND.send(player);
        } else {
             // add the player 2 hearts and send him the commandant wanted third message
            player.setHealth(player.getHealth() + (2 * 2));
            Messages.COMMANDANT_WANTED_THIRD.send(player);
        }
    }

    @Override
    public void onDay(Player player) {
        // if the strength at day power is enabled, give the player the strength effect
        if (strengthAtDay) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));
        }
    }

    @Override
    public void onNight(Player player) {
        // if the strength at day power is enabled, remove the strength potion effect
        if (strengthAtDay) {
            player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
        }
    }
}

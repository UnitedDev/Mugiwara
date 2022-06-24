package fr.kohei.mugiwara.roles.impl.marine;

import fr.kohei.mugiwara.Mugiwara;
import fr.kohei.mugiwara.camp.CampType;
import fr.kohei.mugiwara.utils.config.Messages;
import fr.kohei.mugiwara.game.player.MUPlayer;
import fr.kohei.mugiwara.power.impl.ChoiceCommand;
import fr.kohei.mugiwara.roles.RolesType;
import fr.kohei.mugiwara.utils.utils.Utils;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class SoldatRole extends RolesType.MURole {
    private Choices choice = null;

    public SoldatRole() {
        super(Arrays.asList(
                new ChoiceCommand()
        ));
    }

    @Override
    public RolesType getRole() {
        return RolesType.SOLDAT;
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(Material.IRON_INGOT);
    }

    @Override
    public String[] getDescription() {
        return new String[0];
    }

    @Override
    public void onDistribute(Player player) {
        // give to the player an iron sword with the enchantement damage_all 3 and a bow arrow_damage 2
        ItemStack sword = new ItemStack(Material.IRON_SWORD);
        sword.addUnsafeEnchantment(org.bukkit.enchantments.Enchantment.DAMAGE_ALL, 3);
        player.getInventory().addItem(sword);

        ItemStack bow = new ItemStack(Material.BOW);
        bow.addUnsafeEnchantment(org.bukkit.enchantments.Enchantment.ARROW_DAMAGE, 2);
        player.getInventory().addItem(bow);

        // new bukkit runnable run task later 5 minutes after
        new BukkitRunnable() {
            @Override
            public void run() {
                // if the choice is null get a random choice from the values
                if (choice == null) {
                    choice = Choices.values()[(int) (Math.random() * Choices.values().length)];
                    onSelect(getPlayer(), choice);
                }
            }
        }.runTaskLater(Mugiwara.getInstance(), 5 * 60 * 20);
    }

    public void onSelect(Player player, Choices choice) {
        // if the choice is one
        if (choice == Choices.ONE) {
            // send the one message select to the player
            Messages.SOLDAT_CHOICE_ONE_SELECT.send(player);
        } else if (choice == Choices.TWO) {
            // send the two message select to the player
            Messages.SOLDAT_CHOICE_TWO_SELECT.send(player);
        } else {
            // send the three message select to the player
            Messages.SOLDAT_CHOICE_THREE_SELECT.send(player);
            // add 2 permanent hearts to the player
            player.setMaxHealth(player.getMaxHealth() + (2 * 2));
            // get a random player from the marine camp players (using Utils.getPlayersInCamp)
            Player randomPlayer = Utils.getPlayersInCamp(CampType.MARINE).get((int) (Math.random() * Utils.getPlayersInCamp(CampType.MARINE).size()));
            // get the role of the random Player
            RolesType role = MUPlayer.get(randomPlayer).getRole().getRole();
            // player knows the role
            Mugiwara.knowsRole(player, role);
        }
    }

    @Override
    public void onDay(Player player) {
        if (choice == Choices.ONE) {
            // add strength potion effect to the player
            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));
        }

        // if the choice is two, remove resistance potion effect to the player
        if (choice == Choices.TWO) {
            player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
        }
    }

    @Override
    public void onNight(Player player) {
        // if the choice is one, remove strength potion effect to the player
        if (choice == Choices.ONE) {
            player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
        }

        // if the choice is two, add resistance potion effect to the player
        if (choice == Choices.TWO) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
        }
    }

    @Getter
    public enum Choices {
        ONE("&cChoix 1", Messages.SOLDAT_CHOICE_ONE_MATERIAL.getDisplay(), Messages.SOLDAT_CHOICE_ONE_LORE.getDisplay()),
        TWO("&cChoix 2", Messages.SOLDAT_CHOICE_TWO_MATERIAL.getDisplay(), Messages.SOLDAT_CHOICE_TWO_LORE.getDisplay()),
        THREE("&cChoix 3", Messages.SOLDAT_CHOICE_THREE_MATERIAL.getDisplay(), Messages.SOLDAT_CHOICE_THREE_LORE.getDisplay());

        private final String display;
        private final Material material;
        private final List<String> lore;

        Choices(String display, String material, String lore) {
            this.display = display;
            this.material = Material.getMaterial(material);
            this.lore = Arrays.asList(lore.split("\n"));
        }
    }
}

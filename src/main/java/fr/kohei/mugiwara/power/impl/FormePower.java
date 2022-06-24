package fr.kohei.mugiwara.power.impl;

import fr.kohei.mugiwara.utils.config.Messages;
import fr.kohei.mugiwara.game.menu.ChooseFormeMenu;
import fr.kohei.mugiwara.power.CommandPower;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FormePower extends CommandPower {
    @Override
    public String getArgument() {
        return "forme";
    }

    @Override
    public boolean onEnable(Player player, String[] args) {
        new ChooseFormeMenu().openMenu(player);

        return true;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public Integer getCooldownAmount() {
        return null;
    }

    @Getter
    public enum FormeTypes {
        ONE("Forme 1", Messages.CHOPPER_FORME_ONE_DESC.getDisplay(), Material.getMaterial(Messages.CHOPPER_FORME_ONE_MATERIAL.getDisplay()),
                new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 3 * 20, 0, false, false),
                new PotionEffect(PotionEffectType.SPEED, 3 * 20, 0, false, false)
        ),
        TWO("Forme 2", Messages.CHOPPER_FORME_TWO_DESC.getDisplay(), Material.getMaterial(Messages.CHOPPER_FORME_TWO_MATERIAL.getDisplay()),
                new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 3 * 20, 1, false, false),
                new PotionEffect(PotionEffectType.SLOW, 3 * 20, 0, false, false)
        ),
        THREE("Forme 3", Messages.CHOPPER_FORME_THREE_DESC.getDisplay(), Material.getMaterial(Messages.CHOPPER_FORME_THREE_MATERIAL.getDisplay()),
                new PotionEffect(PotionEffectType.SPEED, 3 * 20, 2, false, false),
                new PotionEffect(PotionEffectType.JUMP, 3 * 20, 5, false, false)
        ),
        FOUR("Forme 4", Messages.CHOPPER_FORME_FOUR_DESC.getDisplay(), Material.getMaterial(Messages.CHOPPER_FORME_FOUR_MATERIAL.getDisplay()),
                new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 3 * 20, 0, false, false),
                new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 3 * 20, 0, false, false)
        );

        private final String name;
        private final Material material;
        private final List<String> description;
        private final PotionEffect[] effects;

        FormeTypes(String name, String description, Material material, PotionEffect... effects) {
            this.name = name;
            this.material = material;
            this.description = Arrays.stream(description.split("\n")).collect(Collectors.toList());
            this.effects = effects;
        }
    }
}

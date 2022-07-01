package fr.kohei.mugiwara.power.impl;

import fr.kohei.mugiwara.utils.config.Messages;
import fr.kohei.mugiwara.utils.config.Replacement;
import fr.kohei.mugiwara.power.CommandPower;
import fr.kohei.utils.ChatUtil;
import fr.kohei.utils.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import static org.bukkit.potion.PotionEffectType.*;

public class FlairePower extends CommandPower {
    private int uses;

    @Override
    public String getArgument() {
        return "flairer";
    }

    @Override
    public boolean onEnable(Player player, String[] args) {
        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            player.sendMessage(ChatUtil.prefix("&cCe joueur n'est pas connecté."));
            return false;
        }

        if (target.getLocation().distance(player.getLocation()) > 25) {
            player.sendMessage(ChatUtil.prefix("&cVous devez être à moins de 15 blocs du joueur."));
            return false;
        }

        if (uses >= 2) {
            player.sendMessage(ChatUtil.prefix("&cVous avez déjà utilisé ce pouvoir 2 fois."));
            return false;
        }

        Messages.CHOPPER_FLAIRE_FIRSTLINE.send(player, new Replacement("<name>", target.getName()));
        if (target.getActivePotionEffects().isEmpty())
            player.sendMessage(ChatUtil.translate("&cCe joueur n'a aucun effet"));
        else for (PotionEffect activePotionEffect : target.getActivePotionEffects()) {
            Messages.CHOPPER_FLAIRE_FORMAT.sendNP(player,
                    new Replacement("<name>", getDisplay(activePotionEffect.getType())),
                    new Replacement("<amplifier>", "" + activePotionEffect.getAmplifier()),
                    new Replacement("<duration>", "" + TimeUtil.getDuration(activePotionEffect.getDuration() * 50L))
            );
        }

        uses++;
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

    private String getDisplay(PotionEffectType type) {
        if (type == SPEED) return "&bSpeed";
        else if (type == SLOW) return "&7Slowness";
        else if (type == FAST_DIGGING) return "&eHaste";
        else if (type == SLOW_DIGGING) return "&7Mining Fatigue";
        else if (type == INCREASE_DAMAGE) return "&cForce";
        else if (type == HEAL) return "&cInstant Health";
        else if (type == HARM) return "&9Instant Damage";
        else if (type == JUMP) return "&aJump Boost";
        else if (type == CONFUSION) return "&2Nausée";
        else if (type == REGENERATION) return "&dRégénération";
        else if (type == DAMAGE_RESISTANCE) return "&8Résistance";
        else if (type == FIRE_RESISTANCE) return "&6Fire Résistance";
        else if (type == WATER_BREATHING) return "&9Water Breathing";
        else if (type == INVISIBILITY) return "&7Invisibilitée";
        else if (type == BLINDNESS) return "&0Blindness";
        else if (type == NIGHT_VISION) return "&2Night Vision";
        else if (type == HUNGER) return "&2Hunger";
        else if (type == WEAKNESS) return "&7Weakness";
        else if (type == POISON) return "&2Poison";
        else if (type == WITHER) return "&0Wither";
        else if (type == HEALTH_BOOST) return "&eHealth Boost";
        else if (type == ABSORPTION) return "&eAbsorption";
        else if (type == SATURATION) return "&7Saturation";
        else return "&cUnknown";
    }
}

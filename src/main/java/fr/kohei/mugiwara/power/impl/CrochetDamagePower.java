package fr.kohei.mugiwara.power.impl;

import fr.kohei.mugiwara.power.DamagePlayerPower;
import fr.kohei.mugiwara.utils.config.Messages;
import fr.kohei.mugiwara.utils.config.Replacement;
import fr.kohei.mugiwara.utils.utils.Utils;
import fr.kohei.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class CrochetDamagePower extends DamagePlayerPower {

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.GOLD_HOE).setName(Utils.itemFormat("&6&lCrochet")).setInfinityDurability().toItemStack();
    }

    @Override
    public boolean onEnable(Player player, Player target, EntityDamageByEntityEvent damage) {

        if(getCooldown().isCooldownNoMessage(player)) return false;

        target.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 5 * 20, 1, false, false));
        Messages.CROCODILE_CROCHET_USE.send(player, new Replacement(target.getName(), "<name>"));

        return true;
    }

    @Override
    public String getName() {
        return "Crochet";
    }

    @Override
    public Integer getCooldownAmount() {
        return 300;
    }
}

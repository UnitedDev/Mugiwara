package fr.kohei.mugiwara.roles.impl.marine;

import fr.kohei.mugiwara.Mugiwara;
import fr.kohei.mugiwara.camp.impl.MarineCamp;
import fr.kohei.mugiwara.utils.config.Messages;
import fr.kohei.mugiwara.utils.config.Replacement;
import fr.kohei.mugiwara.game.player.MUPlayer;
import fr.kohei.mugiwara.roles.RolesType;
import fr.kohei.uhc.game.player.UPlayer;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;

public class CobyRole extends RolesType.MURole implements Listener {
    private int phase = 0;
    private int hits = 0;
    private int goal = 20;
    private int randomDistance = 50;
    private int killerDistance;

    public CobyRole() {
        super(Arrays.asList());
    }

    @Override
    public RolesType getRole() {
        return RolesType.COBY;
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(Material.INK_SACK, 1, (short) 10);
    }

    @Override
    public String[] getDescription() {
        return new String[0];
    }

    @Override
    public void onDistribute(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, Integer.MAX_VALUE, 0, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false));
        Mugiwara.knowsRole(player, RolesType.GARP);

        ItemStack is = new ItemStack(Material.ENCHANTED_BOOK);
        EnchantmentStorageMeta im = (EnchantmentStorageMeta) is.getItemMeta();
        im.addEnchant(Enchantment.DEPTH_STRIDER, 3, true);
        is.setItemMeta(im);

        player.getInventory().addItem(is);
    }

    @Override
    public void onSecond(Player player) {
        Mugiwara.getInstance().addActionBar(player, "&cPhase 1 &8» &f" + hits + "&8/&f" + goal, "phase");

        Player garp = Mugiwara.findRole(RolesType.GARP);
        if (garp == null) return;

        if (phase > 2) return;
        if (garp.getLocation().distance(player.getLocation()) <= 25) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 3 * 20, 0, false, false));
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;
        if (!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();
        Player damager = (Player) event.getDamager();

        if (!isRole(damager)) return;
        Player garp = Mugiwara.findRole(RolesType.GARP);
        if (garp == null) return;
        if (garp.getLocation().distance(damager.getLocation()) > 25) return;

        if (phase == 0 && hits >= 20) {
            phase = 1;
            sendMessageToGarp();
            damager.removePotionEffect(PotionEffectType.WEAKNESS);
            Messages.COBY_PHASE_CHANGE.send(damager, new Replacement("<phase>", phase));
        }

        if (phase == 1 && hits >= 45) {
            phase = 2;
            sendMessageToGarp();
            player.setMaxHealth(player.getMaxHealth() + 4);
            randomDistance = 30;
            killerDistance = 50;
        }

        if (phase == 2 && hits >= 75) {
            phase = 3;
            sendMessageToGarp();
            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));
            randomDistance = 0;
            killerDistance = 75;
            Messages.COBY_PHASE_CHANGE.send(damager, new Replacement("<phase>", phase));
        }

        if (phase == 3 && hits >= 115) {
            phase = 4;
            sendMessageToGarp();
            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));

            Player luffy = Mugiwara.findRole(RolesType.LUFFY);
            if (luffy != null) {
                Mugiwara.knowsRole(luffy, RolesType.COBY);
            }
            Mugiwara.getInstance().removeActionBar(player, "phase");
            Messages.COBY_PHASE_CHANGE.send(damager, new Replacement("<phase>", phase));
        }

        hits++;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if (getPlayer() == null) return;
        Player death = event.getEntity();
        Player killer = death.getKiller();

        int random = (int) (Math.random() * randomDistance);
        int x = death.getLocation().getBlockX() + random;
        int y = death.getLocation().getBlockY() + random;
        int z = death.getLocation().getBlockZ() + random;

        if (phase == 4) {
            if (UPlayer.get(death).getCamp() instanceof MarineCamp) {
                Messages.COBY_DEATHER_KILLER_NAME.send(getPlayer(),
                        new Replacement("<killer>", killer.getName()),
                        new Replacement("<name>", death.getName())
                );
            } else {
                Messages.COBY_DEATHER_KILLER_ROLE.send(getPlayer(),
                        new Replacement("<role>", MUPlayer.get(death).getRole().getName()),
                        new Replacement("<name>", death.getName())
                );
            }

            Messages.COBY_DEATH_COORDINATES.send(getPlayer(),
                    new Replacement("<name>", death.getName()),
                    new Replacement("<x>", x),
                    new Replacement("<y>", y),
                    new Replacement("<z>", z)
            );
            return;
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                Player player = getPlayer();
                if (player == null) return;

                Messages.COBY_DEATH_COORDINATES.send(player,
                        new Replacement("<name>", death.getName()),
                        new Replacement("<x>", x),
                        new Replacement("<y>", y),
                        new Replacement("<z>", z)
                );
            }
        }.runTaskLater(Mugiwara.getInstance(), 10 * 20);

        if (killer == null) return;
        if (killer.getLocation().distance(getPlayer().getLocation()) > killerDistance) return;

        Messages.COBY_DEATHER_KILLER_ROLE.send(getPlayer(),
                new Replacement("<role>", MUPlayer.get(death).getRole().getName()),
                new Replacement("<name>", death.getName())
        );
    }

    private void sendMessageToGarp() {
        Player garp = Mugiwara.findRole(RolesType.GARP);
        if (garp == null) return;
        Messages.GARP_COBY_PHASE.send(garp);
    }
}

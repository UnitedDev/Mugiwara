package fr.kohei.mugiwara.roles.impl.mugiwara;

import fr.kohei.mugiwara.Mugiwara;
import fr.kohei.mugiwara.camp.CampType;
import fr.kohei.mugiwara.config.Messages;
import fr.kohei.mugiwara.game.MUPlayer;
import fr.kohei.mugiwara.roles.RolesType;
import fr.kohei.mugiwara.utils.Utils;
import fr.kohei.uhc.UHC;
import fr.kohei.uhc.game.player.UPlayer;
import fr.kohei.utils.ChatUtil;
import fr.kohei.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;
import java.util.stream.Collectors;

public class ZoroRole extends RolesType.MURole implements Listener {

    private final List<UUID> hits = new ArrayList<>();
    private int kills = 0;
    private int fireChance = 0;
    private boolean found;

    public ZoroRole() {
        super(Arrays.asList(

        ));
    }

    @Override
    public RolesType getRole() {
        return RolesType.ZORO;
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(Material.IRON_SWORD);
    }

    @Override
    public String[] getDescription() {
        return new String[0];
    }

    @Override
    public void onDistribute(Player player) {
        player.getInventory().addItem(new ItemBuilder(Material.DIAMOND_SWORD).setName(Utils.notClickItem("&d&lEnma")).addEnchant(Enchantment.DAMAGE_ALL, 4).toItemStack());
        Mugiwara.knowsRole(player, RolesType.LUFFY);

        int random = (int) (Math.random() * 3);
        if (random == 1) randomRole(player);
    }

    private void randomRole(Player player) {
        List<RolesType> roles = Arrays.asList(RolesType.SANJI, RolesType.LAW, RolesType.EUSTASS, RolesType.PIRATE);

        List<RolesType> players = Bukkit.getOnlinePlayers().stream()
                .filter(p -> UHC.getGameManager().getPlayers().contains(p.getUniqueId()))
                .filter(p -> MUPlayer.get(player).getRole() instanceof RolesType.MURole)
                .map(p -> ((RolesType.MURole) MUPlayer.get(player).getRole()).getRole())
                .filter(rolesType -> !roles.contains(rolesType))
                .collect(Collectors.toList());
        if (player.isEmpty()) return;
        Collections.shuffle(players);

        Mugiwara.knowsRole(player, players.get(0));
    }

    private void checkKills(Player player) {
        switch (kills) {
            case 1:
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false));
                Messages.ZORO_FIRSTKILL.send(player);
                break;
            case 2:
                player.removePotionEffect(PotionEffectType.SPEED);
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1, false, false));
                Messages.ZORO_FIRSTKILL.send(player);
                break;
            case 3:
                player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));
                Messages.ZORO_THIRDKILL.send(player);
                break;
            case 4:
            case 5:
            case 6:
                player.setMaxHealth(player.getMaxHealth() + 2);
                this.fireChance += 20;
                Messages.ZORO_FOURTHDKILL.send(player);
                break;
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;
        if (!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();

        if (!isRole(player)) return;

        hits.add(player.getUniqueId());
        Bukkit.getScheduler().runTaskLater(Mugiwara.getInstance(), () -> hits.remove(player.getUniqueId()), 6 * 20);

        this.checkHitFire(player);
    }

    private void checkHitFire(Player player) {
        int random = (int) (Math.random() * 100);
        if (random > fireChance) return;

        player.setFireTicks(60);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (!hits.contains(player.getUniqueId())) return;
        Player killer = getPlayer();
        ItemStack item = killer.getItemInHand();

        UPlayer uPlayer = UPlayer.get(player);
        UPlayer uKiller = UPlayer.get(killer);

        CampType uPCamp = ((CampType.MUCamp) uPlayer.getCamp()).getCampType();
        CampType uKCamp = ((CampType.MUCamp) uKiller.getCamp()).getCampType();

        if (uPCamp != null && uPCamp.equals(uKCamp)) return;

        if (item == null || !item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) return;
        if (!item.getItemMeta().getDisplayName().equals(ChatUtil.translate(Utils.notClickItem("&d&lEnma")))) return;


        kills++;
        this.checkKills(killer);
    }

    @Override
    public void onSecond(Player player) {
        Player sanji = Mugiwara.findRole(RolesType.SANJI);

        if (sanji == null || sanji.getLocation().distance(player.getLocation()) > 25 || found) return;

        found = true;
    }
}

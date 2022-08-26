package fr.uniteduhc.mugiwara.power.impl;

import fr.uniteduhc.mugiwara.Mugiwara;
import fr.uniteduhc.mugiwara.power.RightClickPower;
import fr.uniteduhc.mugiwara.utils.config.Messages;
import fr.uniteduhc.mugiwara.utils.config.Replacement;
import fr.uniteduhc.mugiwara.utils.utils.packets.Damage;
import fr.uniteduhc.mugiwara.utils.utils.packets.MathUtil;
import fr.uniteduhc.mugiwara.utils.utils.Utils;
import fr.uniteduhc.utils.ItemBuilder;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.EnumParticle;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class RoomPower extends RightClickPower {
    private Location center;
    private int radius;
    private List<UUID> inZone;
    private int timer;

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.BRICK).setName(Utils.itemFormat("&6&lShambles")).toItemStack();
    }

    @Override
    public String getName() {
        return "Room";
    }

    @Override
    public Integer getCooldownAmount() {
        return 8 * 60;
    }

    @Override
    public boolean onEnable(Player player, boolean rightClick) {
        if(this.getCenter() != null) {
            return false;
        }

        new BukkitRunnable() {

            public void run() {
                if (!player.isOnline()) {
                    Mugiwara.getInstance().removeActionBar(player, "shamblesTimer");
                    this.cancel();
                    return;
                }

                if (timer >= 6 * 20) {
                    room(player, timer / 20);
                    Mugiwara.getInstance().removeActionBar(player, "shamblesTimer");
                    this.cancel();
                    return;
                }

                if (!player.getItemInHand().isSimilar(getItem())) {
                    if (timer > 20) {
                        room(player, timer / 20);
                    }
                    Mugiwara.getInstance().removeActionBar(player, "shamblesTimer");
                    this.cancel();
                    return;
                }

                Mugiwara.getInstance().addActionBar(player, "&cTemps &8» &f" + (timer / 20), "shamblesTimer");
                timer++;
            }
        }.runTaskTimer(Mugiwara.getInstance(), 1L, 1L);

        return true;
    }

    private void room(Player player, int seconds) {
        timer = 60;
        radius = 50;
        if (seconds < 2) {
            radius = 10;
        } else if (seconds < 3) {
            radius = 20;
        } else if (seconds < 4) {
            radius = 30;
        } else if (seconds < 5) {
            radius = 40;
        }
        inZone = new ArrayList<>();

        center = player.getLocation();

        Messages.LAW_ROOM_CREATE.send(player, new Replacement("<block>", radius));
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 60 * 20, 0, false, false));
        Damage.addTempNoDamage(player, EntityDamageEvent.DamageCause.FALL, 60);
        new BukkitRunnable() {
            private int seconds = 120;

            @Override
            public void run() {
                    if (seconds == 0) {
                    inZone = null;
                    radius = 50;
                    center = null;
                    this.cancel();
                    return;
                }

                Utils.getNearPlayers(player, 32).forEach(player1 -> {
                    if (inZone.contains(player1.getUniqueId())) return;

                    inZone.add(player1.getUniqueId());
                });

                MathUtil.sendCircleParticle(EnumParticle.SPELL_WITCH, center, radius, 32);
                seconds--;
            }
        }.runTaskTimer(Mugiwara.getInstance(), 10L, 10L);
    }
}

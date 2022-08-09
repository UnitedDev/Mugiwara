package fr.kohei.mugiwara.game.menu;

import fr.kohei.menu.Button;
import fr.kohei.menu.Menu;
import fr.kohei.mugiwara.Mugiwara;
import fr.kohei.mugiwara.game.player.MUPlayer;
import fr.kohei.mugiwara.roles.marine.KuzanRole;
import fr.kohei.mugiwara.utils.config.Messages;
import fr.kohei.mugiwara.utils.utils.Cooldown;
import fr.kohei.mugiwara.utils.utils.Utils;
import fr.kohei.mugiwara.utils.utils.packets.MathUtil;
import fr.kohei.mugiwara.utils.utils.packets.PlayerUtils;
import fr.kohei.mumble.core.mumble.Message;
import fr.kohei.uhc.UHC;
import fr.kohei.uhc.game.player.UPlayer;
import fr.kohei.utils.ChatUtil;
import fr.kohei.utils.ItemBuilder;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class IceRightPowerMenu extends Menu {

    @Override
    public String getTitle(Player player) {
        return "Hie Hie no Mi";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {

        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(2, new IceCapacityButton(IceCapacityType.ICE_AGE));
        buttons.put(4, new IceCapacityButton(IceCapacityType.ICE_SABER));
        buttons.put(6, new IceCapacityButton(IceCapacityType.ICE_TIME_CAPSULE));

        return buttons;
    }

    private class IceCapacityButton extends Button {

        private final IceCapacityType type;

        private IceCapacityButton(IceCapacityType type) {
            this.type = type;
        }

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(type.getMaterial()).setName("&c" + type.getName()).setLore(type.getLore()).toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {

            MUPlayer muPlayer = MUPlayer.get(player);

            KuzanRole kuzanRole = (KuzanRole) muPlayer.getRole();

            if(type == IceCapacityType.ICE_AGE){

                if(kuzanRole.getEndurence() < 45){
                    Messages.KUZAN_HAS_NOT_OF_ENDURENCE.send(player);
                    return;
                }

                Messages.KUZAN_ICE_AGE_USE.send(player);
                kuzanRole.setEndurence(kuzanRole.getEndurence() - 45);

                for(Location sphere : getSphere(player.getLocation(), 30)){
                    Block block = sphere.getBlock();
                    Bukkit.getOnlinePlayers().stream()
                            .filter(players -> UPlayer.get(players.getUniqueId()).isAlive())
                            .filter(players -> players != players)
                            .filter(players -> players.getLocation().distance(player.getLocation()) <= 30)
                            .forEach(players -> {

                        players.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 15 * 20, 2, false, false));
                        players.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 15 * 20, 1, false, false));

                        players.getLocation().clone().add(0, 2, 0).getBlock().setType(Material.PACKED_ICE);

                        for(int i = -1; i != 2; i++){
                            players.getLocation().clone().add(i, 1, 0).getBlock().setType(Material.PACKED_ICE);
                            players.getLocation().clone().add(0, 1, i).getBlock().setType(Material.PACKED_ICE);

                            players.getLocation().clone().add(i, 0, 0).getBlock().setType(Material.PACKED_ICE);
                            players.getLocation().clone().add(0, 0, i).getBlock().setType(Material.PACKED_ICE);

                        }

                        players.getLocation().clone().add(0, -1, 0).getBlock().setType(Material.PACKED_ICE);

                    });
                    if(block.getType() != Material.BEDROCK && block.getType() != Material.AIR){
                        block.setType(Material.PACKED_ICE);
                    }

                }

                return;
            }

            if(type == IceCapacityType.ICE_SABER){

                if(kuzanRole.getEndurence() < 35){
                    Messages.KUZAN_HAS_NOT_OF_ENDURENCE.send(player);
                    return;
                }

                if(kuzanRole.isSaberMode()){
                    Messages.KUZAN_IS_IN_SABER_MODE.send(player);
                    return;
                }

                Messages.KUZAN_ICE_AGE_USE.send(player);
                kuzanRole.setEndurence(kuzanRole.getEndurence() - 35);

                player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 1, false, false));
                player.getInventory().addItem(kuzanRole.getIceSaber());
                kuzanRole.setSaberMode(true);

                return;
            }

            if(type == IceCapacityType.ICE_TIME_CAPSULE){

                if(kuzanRole.getDomeCooldown().isCooldown(player)) return;

                if(kuzanRole.getEndurence() < 60){
                    Messages.KUZAN_HAS_NOT_OF_ENDURENCE.send(player);
                    return;
                }

                kuzanRole.setEndurence(kuzanRole.getEndurence() - 60);

                if(kuzanRole.getLastHit() == null){
                    Messages.KUZAN_LAST_HIT_NULL.send(player);
                    return;
                }

                World world = player.getWorld();
                int x = (int) player.getLocation().getX();
                int z = (int) player.getLocation().getZ();

                Location location = world.getHighestBlockAt(x, z).getLocation();

                Location centerDome = new Location(location.getWorld(), location.getX(), location.getY() + 10, location.getZ());

                List<Location> sphereLoc = MathUtil.getSphere(centerDome, 15, true);
                kuzanRole.setDomeLocation(sphereLoc);

                for(Location locBlock : sphereLoc){
                    Block block = locBlock.getBlock();
                    if(block.getType() != Material.BEDROCK){
                        block.setType(Material.PACKED_ICE);
                    }
                }

                player.teleport(new Location(location.getWorld(), location.getX() + 10, location.getY() + 1, location.getZ()));

                kuzanRole.getDomeCooldown().setCooldown(10);
                kuzanRole.getDomeCooldown().task();

                Player target = kuzanRole.getLastHit();

                target.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20 * 10, 0, false, false));
                target.teleport(new Location(location.getWorld(), location.getX() - 10, location.getY() + 1, location.getZ()));

            }


        }
    }

    @Getter
    public enum IceCapacityType {

        ICE_AGE("Ice Age", Material.ICE, new String[]{""}),

        ICE_SABER("Ice Saber", Material.DIAMOND_SWORD, new String[]{""}),

        ICE_TIME_CAPSULE("Ice Time Capsule", Material.BUCKET, new String[]{""}),

        ;

        private String name;
        private Material material;
        private String[] lore;

        IceCapacityType(String name, Material material, String[] lore) {
            this.name = name;
            this.material = material;
            this.lore = lore;
        }
    }

    private List<Location> getSphere(Location centerBlock, int radius) {
        List<Location> circleBlocks = new ArrayList<>();
        int bX = centerBlock.getBlockX();
        int bY = centerBlock.getBlockY();
        int bZ = centerBlock.getBlockZ();
        for (int x = bX - radius; x <= bX + radius; x++) {
            for (int y = bY - radius; y <= bY + radius; y++) {
                for (int z = bZ - radius; z <= bZ + radius; z++) {
                    Location block = new Location(centerBlock.getWorld(), x, y, z);
                    circleBlocks.add(block);
                }
            }
        }
        return circleBlocks;
    }

}

package fr.kohei.mugiwara.game.events;

import fr.kohei.mugiwara.Mugiwara;
import fr.kohei.mugiwara.game.menu.BusterCallTresorMenu;
import fr.kohei.mugiwara.roles.mugiwara.LuffyRole;
import fr.kohei.mugiwara.utils.config.Messages;
import fr.kohei.mugiwara.utils.utils.Utils;
import fr.kohei.uhc.UHC;
import fr.kohei.utils.ItemBuilder;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Setter
@Getter
public class TresorManager implements Listener {
    private final List<Location> tresors;
    private int timer;

    public TresorManager(Plugin plugin) {
        this.tresors = new ArrayList<>();
        this.timer = 50 * 60;

        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void onStart() {
        Bukkit.getScheduler().runTaskLater(Mugiwara.getInstance(), this::init, 20 * timer);
    }

    public void init() {
        boolean busterCall = false;

        for (int i = 0; i < 12; i++) {
            Location randomBlock = getRandomLocation();

            randomBlock.getBlock().setType(Material.CHEST);

            Chest chest = (Chest) randomBlock.getBlock().getState();
            final int random = new Random().nextInt(100);
            if (!busterCall) {
                chest.getInventory().addItem(this.getBusterCallItem());
                busterCall = true;
            } else if (random < 5) {
                chest.getInventory().addItem(LuffyRole.LUFFY_VIVE_CARD.toItemStack());
            } else if (random < 10) {
                chest.getInventory().addItem(new ItemStack(Material.BONE));
            } else if (random < 35) {
                chest.getInventory().addItem(Mugiwara.getInstance().getDenMushiManager().getDenDenMushi());
            } else {
                chest.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE, 2));
            }
            chest.update(true);
            Bukkit.getOnlinePlayers().forEach(Messages.TRESOR_SPAWN::send);
        }
    }

    private Location getRandomLocation() {
        World world = UHC.getInstance().getGameManager().getUhcWorld();

        int x = (int) (Math.random() * 350);
        int z = (int) (Math.random() * 350);
        int y = world.getHighestBlockYAt(x, z);

        return new Location(world, x, y, z);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        if (tresors.contains(event.getBlock().getLocation())) event.setCancelled(true);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        ItemStack clickedItem = event.getItem();

        if (!clickedItem.isSimilar(getBusterCallItem())) return;

        event.setCancelled(true);
        new BusterCallTresorMenu().openMenu(event.getPlayer());
    }

    public ItemStack getBusterCallItem() {
        return new ItemBuilder(Material.NETHER_STAR).setName(Utils.itemFormat("Buster Call")).toItemStack();
    }
}

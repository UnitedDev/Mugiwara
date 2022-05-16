package fr.kohei.mugiwara.utils.tornado;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;


@SuppressWarnings("unused")
public class WaterShoot implements Listener, CommandExecutor {
    public Tornado plugin;
    ItemStack water = new ItemStack(Material.NOTE_BLOCK);

    public WaterShoot(Tornado plugin) {
        this.plugin = plugin;
    }

    public static ItemStack createItem(Material material, String displayname, String lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(displayname);
        ArrayList<String> Lore = new ArrayList<String>();
        Lore.add(lore);
        meta.setLore(Lore);


        item.setItemMeta(meta);
        return item;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (cmd.getName().equalsIgnoreCase("waterfountain") || (cmd.getName().equalsIgnoreCase("wf")) && sender.hasPermission("Tornado.admin")) {
                player.getInventory().addItem(water);
                player.sendMessage(ChatColor.AQUA + "The water fountain block has been added! Be sure to power it!");
            }
        }
        return false;
    }

    @SuppressWarnings("deprecation")
    public void onRedstone(BlockRedstoneEvent event) {
        Bukkit.broadcastMessage("Test");
        if (event.getNewCurrent() > 0) {
            Bukkit.broadcastMessage("Test 1");
            if (event.getBlock().getType() == Material.NOTE_BLOCK) {
                Bukkit.broadcastMessage("Final test");
                final Location l = event.getBlock().getLocation();
                final Material mat = l.getBlock().getType();
                final FallingBlock b = l.getWorld().spawnFallingBlock(l, 25, l.getBlock().getData());
                b.setVelocity(new Vector(0.0D, 0.75D, 0.0D));
                l.getBlock().setType(Material.AIR);
                new BukkitRunnable() {
                    public void run() {
                        if (!b.isDead()) {
                            b.remove();
                        }
                        l.getBlock().setType(mat);
                    }
                }
                        .runTaskLater(plugin, 40L);
            }
        }

    }
}

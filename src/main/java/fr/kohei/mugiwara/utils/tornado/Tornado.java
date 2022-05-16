package fr.kohei.mugiwara.utils.tornado;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.HashSet;

public class Tornado extends JavaPlugin {
    public void onEnable() {
        getServer().getLogger().info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        getServer().getLogger().info(getDescription().getName() + " Version: " + getDescription().getVersion() + " By: 97WaterPolo " + " has been enabled!");
        getServer().getLogger().info("Please suggest ideas by sending a PM at: " + getDescription().getWebsite());
        getServer().getLogger().info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        getServer().getPluginManager().registerEvents(new WaterShoot(this), this);
        getCommand("wf").setExecutor(new WaterShoot(this));
        getCommand("waterfountain").setExecutor(new WaterShoot(this));
    }

    public void onDisable() {
        getServer().getLogger().info(getDescription().getName() + " Version: " + getDescription().getVersion() + " Has been disabled!");
    }

    @SuppressWarnings("deprecation")
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player) sender;
        if (cmd.getName().equalsIgnoreCase("tornado") && sender.hasPermission("tornado.use")) {
            if (args.length == 0) {
                player.sendMessage(ChatColor.RED + "Invalid Usage!" + ChatColor.YELLOW + " Please use " + ChatColor.GOLD + "/tornado <blockid> <speed> <block amount> <spew> <explode>");
                player.sendMessage(ChatColor.DARK_BLUE + "You can also use the demo tornado," + ChatColor.WHITE + " /tornado demo");
            } else {
                if (args[0].equals("demo")) {
                    player.sendMessage(ChatColor.GREEN + "Demo tornado uses " + ChatColor.AQUA + "/tornado 1 0.3 200 false false");
                    TornadoResource.spawnTornado(this, player.getTargetBlock((HashSet<Byte>) null, 25).
                                    getLocation(), Material.WEB, (byte) 0, new Vector(100, 10, 0), 0.3,
                            200, (long) 30 * 20, false, false);
                } else {
                    String text0 = args[0];
                    int id = 0;
                    int data = 0;
                    if (text0.contains(":")) {
                        String[] str = text0.split(":");
                        id = Integer.parseInt(str[0]);
                        data = Integer.parseInt(str[1]);
                    } else {
                        id = Integer.parseInt(text0);
                        Material material = Material.getMaterial(id);
                    }

                    String text = args[1];
                    double value = Double.parseDouble(text);

                    String text1 = args[2];
                    int integer1 = Integer.parseInt(text1);

                    String text2 = args[3];
                    boolean boolean1 = Boolean.parseBoolean(text2);

                    String text3 = args[4];
                    boolean boolean2 = Boolean.parseBoolean(text3);
                    try {
                    } catch (Exception e) {
                        e.printStackTrace();
                        player.sendMessage(ChatColor.RED + "Error created! " + ChatColor.YELLOW + "Please make sure command is right. " + ChatColor.DARK_PURPLE + "/tornado <blockid> <speed> <block amount> <spew> <explode>");
                    }
                }
            }
        } else {
            player.sendMessage(ChatColor.RED + "Permission Error: Lacking the Tornado.use permission!");
        }
        return false;
    }
}



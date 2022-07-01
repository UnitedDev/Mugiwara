package fr.kohei.mugiwara.game.denmushi;

import fr.kohei.BukkitAPI;
import fr.kohei.command.Command;
import fr.kohei.command.param.Param;
import fr.kohei.mugiwara.Mugiwara;
import fr.kohei.mugiwara.game.player.MUPlayer;
import fr.kohei.mugiwara.power.impl.DenDenMushiChatPower;
import fr.kohei.mugiwara.power.impl.DenDenMushiPower;
import fr.kohei.mugiwara.roles.RolesType;
import fr.kohei.mugiwara.utils.config.Messages;
import fr.kohei.mugiwara.utils.config.Replacement;
import fr.kohei.mugiwara.utils.utils.Utils;
import fr.kohei.utils.ChatUtil;
import fr.kohei.utils.ItemBuilder;
import lombok.Getter;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class DenMushiManager implements Listener {
    private final Map<UUID, UUID> appel;
    private final Map<UUID, UUID> chat;

    public DenMushiManager(Plugin plugin) {
        this.appel = new HashMap<>();
        this.chat = new HashMap<>();

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        BukkitAPI.getCommandHandler().registerClass(this.getClass());
        this.registerDenDenMushiCraft(plugin);
    }

    private void registerDenDenMushiCraft(Plugin plugin) {
        ShapedRecipe recipe = new ShapedRecipe(getDenDenMushi());
        recipe.shape("$€$", "&@&", "@£@");
        recipe.setIngredient('$', Material.STRING);
        recipe.setIngredient('€', Material.IRON_INGOT);
        recipe.setIngredient('&', Material.REDSTONE);
        recipe.setIngredient('@', Material.GOLD_INGOT);
        recipe.setIngredient('£', Material.REDSTONE_BLOCK);

        plugin.getServer().addRecipe(recipe);
    }

    public void onDenDenMushiCraft(Player player) {
        if(MUPlayer.get(player).getRole().getPowers() == null) MUPlayer.get(player).getRole().setPowers(new ArrayList<>());
        MUPlayer.get(player).getRole().getPowers().add(new DenDenMushiPower());
    }

    @EventHandler
    public void onCraft(CraftItemEvent event) {
        ItemStack itemStack = event.getRecipe().getResult();
        Player player = (Player) event.getWhoClicked();

        if (!itemStack.isSimilar(getDenDenMushi())) return;

        onDenDenMushiCraft(player);
    }

    public ItemStack getDenDenMushi() {
        return new ItemBuilder(Material.SLIME_BALL).addEnchant(Enchantment.DAMAGE_ALL, 1).hideItemFlags()
                .setName(Utils.itemFormat("&a&lDen Den Mushi")).toItemStack();
    }

    public void onSelection(Player player, Player target) {
        Messages.DENDEN_MUSHI_RECEIVE.send(target,
                new Replacement("<name>", player.getName())
        );
        Messages.DENDEN_MUSHI_SEND.send(player,
                new Replacement("<name>", target.getName())
        );

        appel.put(player.getUniqueId(), target.getUniqueId());

        TextComponent textComponent = new TextComponent(ChatUtil.translate(Messages.DENDEN_MUSHI_ACCEPT.getDisplay()));
        textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mu rep " + player.getName()));
        target.spigot().sendMessage(textComponent);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (getAppel().containsKey(player.getUniqueId())) {
                    Messages.DENDEN_MUSHI_REFUSED_ASKER.send(player,
                            new Replacement("<name>", target.getName())
                    );
                    Messages.DENDEN_MUSHI_REFUSED_TARGET.send(target,
                            new Replacement("<name>", player.getName())
                    );
                }
            }
        }.runTaskLater(Mugiwara.getInstance(), 15 * 20);
    }

    private void accept(Player target, Player player) {
        player.getInventory().remove(getDenDenMushi());

        Messages.DENDEN_MUSHI_ACCEPTED_ASKER.send(player,
                new Replacement("<name>", target.getName())
        );
        Messages.DENDEN_MUSHI_ACCEPTED_TARGET.send(target,
                new Replacement("<name>", player.getName())
        );

        this.chat.put(player.getUniqueId(), target.getUniqueId());
        this.chat.put(target.getUniqueId(), player.getUniqueId());

        if(MUPlayer.get(player).getRole().getPowers() == null) MUPlayer.get(player).getRole().setPowers(new ArrayList<>());
        if(MUPlayer.get(target).getRole().getPowers() == null) MUPlayer.get(target).getRole().setPowers(new ArrayList<>());

        MUPlayer.get(player).getRole().getPowers().add(new DenDenMushiChatPower());
        MUPlayer.get(target).getRole().getPowers().add(new DenDenMushiChatPower());

        Bukkit.getScheduler().runTaskLater(Mugiwara.getInstance(), () -> {
            this.chat.remove(player.getUniqueId());
            this.chat.remove(target.getUniqueId());

            MUPlayer.get(player).getRole().getPowers().removeIf(power -> power instanceof DenDenMushiChatPower);
            MUPlayer.get(target).getRole().getPowers().removeIf(power -> power instanceof DenDenMushiChatPower);

            Messages.DENDEN_MUSHI_CHAT_END.send(player);
            Messages.DENDEN_MUSHI_CHAT_END.send(target);
        }, 20 * 60);

        Player coby = Mugiwara.findRole(RolesType.COBY);
        if (coby == null) return;

        Messages.COBY_DENDENMUSHI_ENABLE.send(coby,
                new Replacement("<x>", player.getLocation().getBlockX()),
                new Replacement("<y>", player.getLocation().getBlockY()),
                new Replacement("<z>", player.getLocation().getBlockZ())
        );
        TextComponent textComponent = new TextComponent(ChatUtil.translate(Messages.COBY_DENDENMUSHI_SEE.getDisplay()));
        textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mu seedendenmushi " + player.getName()));
        coby.spigot().sendMessage(textComponent);
    }

    @Command(names = "mu rep")
    public static void answer(Player player, @Param(name = "player") Player target) {
        DenMushiManager denDenMushi = Mugiwara.getInstance().getDenMushiManager();

        if (!denDenMushi.getAppel().containsValue(player.getUniqueId())
                || denDenMushi.getAppel().get(target.getUniqueId()) != player.getUniqueId()) {
            Messages.DENDEN_MUSHI_NO_REQUEST.send(player);
            return;
        }

        denDenMushi.getAppel().remove(player.getUniqueId());
        denDenMushi.accept(player, target);
    }
}

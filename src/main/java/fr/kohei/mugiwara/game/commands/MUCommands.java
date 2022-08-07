package fr.kohei.mugiwara.game.commands;

import fr.kohei.command.Command;
import fr.kohei.command.param.Param;
import fr.kohei.mugiwara.Mugiwara;
import fr.kohei.mugiwara.game.menu.SeePowersMenu;
import fr.kohei.mugiwara.game.player.MUPlayer;
import fr.kohei.mugiwara.power.ClickPower;
import fr.kohei.mugiwara.power.CommandPower;
import fr.kohei.mugiwara.power.Power;
import fr.kohei.mugiwara.power.impl.KaishinPower;
import fr.kohei.mugiwara.roles.RolesType;
import fr.kohei.uhc.UHC;
import fr.kohei.uhc.game.GameManager;
import fr.kohei.uhc.game.player.UPlayer;
import fr.kohei.uhc.module.manager.Camp;
import fr.kohei.utils.ChatUtil;
import fr.kohei.utils.Cuboid;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MUCommands {

    @Command(names = {"claim", "mu claim"})
    public static void onCommand(Player player) {
        UPlayer uPlayer = UPlayer.get(player);

        if (!uPlayer.isAlive()) {
            player.sendMessage(ChatUtil.prefix("&cVous n'êtes pas en vie."));
            return;
        }

        if (uPlayer.getRole() == null) {
            player.sendMessage(ChatUtil.prefix("&cVous n'avez pas de rôle."));
            return;
        }

        if (uPlayer.getRole() instanceof RolesType.MURole) {
            ((RolesType.MURole) uPlayer.getRole()).onClaim();
        }
    }

    @Command(names = {"mu", "mu help"})
    public static void commandHelp(Player player) {
        player.sendMessage(ChatUtil.translate("&7&m--------------------------------"));
        player.sendMessage(ChatUtil.translate("&8┃ &6&lCommandes Utiles"));
        player.sendMessage(ChatUtil.translate("  &e/mu help &7(&fToutes les Commandes&7)"));
        player.sendMessage(ChatUtil.translate("  &e/mu me &7(&fVoir son rôle&7)"));
        player.sendMessage(ChatUtil.translate("  &e/mu roles &7(&fVoir la composition&7)"));

        RolesType.MURole role = MUPlayer.get(player).getRole();
        if(role != null && role.getPowers().stream().anyMatch(power -> power instanceof CommandPower)) {
            player.sendMessage(ChatUtil.translate(" "));
            player.sendMessage(ChatUtil.translate("&8┃ &6&lCommandes de Rôle"));
            for (Power power : role.getPowers()) {
                if (!(power instanceof CommandPower)) continue;
                CommandPower commandPower = (CommandPower) power;
                player.sendMessage(ChatUtil.translate("  &e/mu " + commandPower.getArgument()));
            }
        }
        player.sendMessage(ChatUtil.translate("&7&m--------------------------------"));
    }

    @SneakyThrows
    @Command(names = {"setrole"})
    public static void setRole(Player player, @Param(name = "player") Player target, @Param(name = "role") String roleName) {
        RolesType role = Arrays.stream(RolesType.values()).filter(rolesType -> rolesType.name().equalsIgnoreCase(roleName))
                .findFirst().orElse(null);

        UPlayer uPlayer = UPlayer.get(target);
        if (role == null) {
            player.sendMessage(ChatUtil.prefix("&cCe rôle n'existe pas."));
            return;
        }

        RolesType.MURole muRole = role.getRoleClass().newInstance();

        uPlayer.setRole(muRole);
        uPlayer.setCamp(role.getCampType().getCamp());

        muRole.onDistribute(target);
        if (muRole instanceof Listener) {
            Bukkit.getPluginManager().registerEvents((Listener) muRole, Mugiwara.getInstance());
        }
        target.setHealth(target.getMaxHealth());
        for (Power power : muRole.getPowers()) {
            if (power instanceof ClickPower && ((ClickPower) power).isGive()) {
                target.getInventory().addItem(((ClickPower) power).getItem());
            }
        }
    }

    @Command(names = "mu")
    public static void allCommands(Player player, @Param(name = "args", wildcard = true) String arg) {
        String[] args = arg.split(" ");

        if (args.length < 1) return;

        String arg1 = args[0];

        UPlayer uPlayer = UPlayer.get(player);

        if (uPlayer.getRole() == null) return;
        if (!(uPlayer.getRole() instanceof RolesType.MURole)) return;

        RolesType.MURole role = (RolesType.MURole) uPlayer.getRole();

        CommandPower commandPower = null;

        for (Power power : role.getPowers()) {
            if (!(power instanceof CommandPower)) continue;

            CommandPower cmdPower = (CommandPower) power;

            if (cmdPower.getArgument().equalsIgnoreCase(arg1)) {
                commandPower = cmdPower;
                break;
            }
        }
        if (commandPower == null) return;

        boolean worked = commandPower.onEnable(player, args);
        if (commandPower.getCooldown() != null && worked) {
            if (commandPower.getCooldown().isCooldown(player)) return;
            commandPower.getCooldown().setCooldown(commandPower.getCooldownAmount());
        }
        if (worked)
            Power.onUse(player);
    }

    @Command(names = {"mu me", "mu role"})
    public static void showRole(Player sender) {
        UPlayer uPlayer = UPlayer.get(sender);
        if (uPlayer.getRole() == null) {
            sender.sendMessage(ChatUtil.prefix("&cVous n'avez pas de rôle"));
            return;
        }

        for (String s : uPlayer.getRole().getDescription()) {
            sender.sendMessage(ChatUtil.translate(s));
        }

        if (Mugiwara.getInstance().getKnows().containsKey(sender.getUniqueId())) {
            for (RolesType rolesType : Mugiwara.getInstance().getKnows().get(sender.getUniqueId())) {
                Mugiwara.knowsRole(sender, rolesType);
            }
        }
    }

    @Command(names = {"mu show"}, power = 39)
    public static void modSeeRole(Player sender, @Param(name = "player") Player target) {
        UPlayer uPlayer = UPlayer.get(target);

        if (uPlayer.getRole() == null) {
            sender.sendMessage(ChatUtil.prefix("&cCe joueur n'a pas de rôle."));
            return;
        }

        sender.sendMessage(ChatUtil.prefix("&fLe rôle de &a" + target.getName() + " &fest &c" + uPlayer.getRole().getName()));
    }

    @Command(names = {"mu list"}, power = 39)
    public static void listRoles(Player sender) {
        sender.sendMessage(ChatUtil.translate("&7&m----------------------"));
        for (Camp value : UHC.getInstance().getModuleManager().getModule().getCamps()) {
            GameManager gameManager = UHC.getInstance().getGameManager();
            if (gameManager.getPlayers().stream()
                    .filter(uuid -> UPlayer.get(uuid).getRole() != null)
                    .anyMatch(p -> UPlayer.get(p).getCamp() == value)) {
                sender.sendMessage(ChatUtil.translate("&8❘ " + value.getColor() + value.getName()));
                gameManager.getPlayers().stream().filter(uuid -> UPlayer.get(uuid).getRole() != null).forEach(uuid -> {
                    UPlayer uPlayer1 = UPlayer.get(uuid);
                    if (uPlayer1.getCamp() == value) {
                        sender.sendMessage(ChatUtil.translate(" &f&l» &c" + uPlayer1.getRole().getName() + " &f(&7" + uPlayer1.getName() + "&f)"));
                    }
                });
            }
        }
        sender.sendMessage(ChatUtil.translate("&7&m----------------------"));
    }

    @Command(names = {"mu powers", "mu items"}, power = 39)
    public static void seePowers(Player sender, @Param(name = "player") Player target) {
        UPlayer uPlayer = UPlayer.get(target);

        if (uPlayer.getRole() == null) {
            sender.sendMessage(ChatUtil.prefix("&cCe joueur n'a pas de rôle."));
            return;
        }

        new SeePowersMenu(target).openMenu(sender);
    }

    @Command(names = {"mu op"})
    public static void showOnePiece(Player player) {

        if (!Mugiwara.getInstance().getOnePieceManager().getCoordinates().containsKey(player.getUniqueId())) {
            player.sendMessage(ChatUtil.prefix("&cVous n'avez pas capturé 4 ponéglyphes."));
            return;
        }
        List<Location> locations = Mugiwara.getInstance().getOnePieceManager().getCoordinates().get(player.getUniqueId());
        player.sendMessage(ChatUtil.translate("&7&l-----------------------------------"));
        locations.forEach(location -> player.sendMessage(ChatUtil.prefix(" &f&l» " +
                "&a" + location.getBlockX() + "&f, " +
                "&a" + location.getBlockY() + "&f, " +
                "&a" + location.getBlockZ()
        )));
        player.sendMessage(ChatUtil.translate("&7&l-----------------------------------"));
    }

    @Command(names = {"mu roles", "mu compo"})
    public static void showComposition(Player sender) {
        if (UHC.getInstance().getGameManager().getGameConfiguration().isHideComposition()) {
            sender.sendMessage(ChatUtil.prefix("&cLa composition est cachée."));
            return;
        }

        sender.sendMessage(ChatUtil.translate("&7&m----------------------"));
        for (Camp value : UHC.getInstance().getModuleManager().getModule().getCamps()) {
            GameManager gameManager = UHC.getInstance().getGameManager();
            if (gameManager.getPlayers().stream()
                    .filter(uuid -> UPlayer.get(uuid).getRole() != null)
                    .anyMatch(p -> UPlayer.get(p).getCamp() == value)) {
                sender.sendMessage(ChatUtil.translate("&8❘ " + value.getColor() + value.getName()));
                gameManager.getPlayers().stream().filter(uuid -> UPlayer.get(uuid).getRole() != null).forEach(uuid -> {
                    UPlayer uPlayer1 = UPlayer.get(uuid);
                    if (uPlayer1.getCamp() == value) {
                        sender.sendMessage(ChatUtil.translate(" &f&l» &c" + uPlayer1.getRole().getName()));
                    }
                });
            }
        }
        sender.sendMessage(ChatUtil.translate("&7&m----------------------"));
    }
}

package fr.kohei.mugiwara.game;

import fr.kohei.command.Command;
import fr.kohei.command.param.Param;
import fr.kohei.mugiwara.power.CommandPower;
import fr.kohei.mugiwara.power.Power;
import fr.kohei.mugiwara.roles.RolesType;
import fr.kohei.uhc.UHC;
import fr.kohei.uhc.game.GameManager;
import fr.kohei.uhc.game.player.UPlayer;
import fr.kohei.uhc.module.manager.Camp;
import fr.kohei.utils.ChatUtil;
import org.bukkit.entity.Player;

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

    @Command(names = "mu")
    public static void allCommands(Player player, @Param(name = "args", wildcard = true) String arg) {
        String[] args = arg.split(" ");

        if (args.length < 2) return;

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

        commandPower.onEnable(player, args);
        Power.onUse();
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
    }

    @Command(names = {"mu mod showrole"}, power = 50)
    public static void modSeeRole(Player sender, @Param(name = "player") Player target) {
        UPlayer uPlayer = UPlayer.get(target);

        if (uPlayer.getRole() == null) {
            sender.sendMessage(ChatUtil.prefix("&cCe joueur n'a pas de rôle."));
            return;
        }

        sender.sendMessage(ChatUtil.prefix("&fLe rôle de &a" + target.getName() + " &fest &c" + uPlayer.getRole().getName()));
    }

    @Command(names = {"mu roles", "mu compo"})
    public static void showComposition(Player sender) {
        if (UHC.getGameManager().getGameConfiguration().isHideComposition()) {
            sender.sendMessage(ChatUtil.prefix("&cLa composition est cachée."));
            return;
        }

        sender.sendMessage(ChatUtil.translate("&7&m----------------------"));
        for (Camp value : UHC.getModuleManager().getModule().getCamps()) {
            GameManager gameManager = UHC.getGameManager();
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

package fr.kohei.mugiwara.power.impl;

import fr.kohei.mugiwara.Mugiwara;
import fr.kohei.mugiwara.game.player.MUPlayer;
import fr.kohei.mugiwara.power.CommandPower;
import fr.kohei.mugiwara.utils.config.Messages;
import fr.kohei.mugiwara.utils.config.Replacement;
import fr.kohei.uhc.game.player.UPlayer;
import fr.kohei.utils.ChatUtil;
import fr.kohei.utils.TimeUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

@Getter
@Setter
public class AnalysePower extends CommandPower {

    private boolean isUsed = false;
    private boolean isCanUse = true;
    private Player cible = null;

    @Override
    public String getArgument() {
        return "analyse";
    }

    @Override
    public boolean onEnable(Player player, String[] args) {

        if (args.length != 2) {
            Messages.PACIFISTA_ANALYSE_SYNTAX.send(player);
            return false;
        }

        Player target = Bukkit.getPlayer(args[1]);

        if (!this.isCanUse) {
            Messages.PACIFISTA_ANALYSE_CANT.send(player);
            return false;
        }

        if (this.isUsed) {
            Messages.PACIFISTA_ANALYSE_CANT.send(player);
            return false;
        }

        if (target == null) {
            player.sendMessage(ChatUtil.prefix("&cCette personne n'est actuellement pas connecté."));
            return false;
        }

        if (player.getLocation().distance(target.getLocation()) > 20) {
            Messages.PACIFISTA_ANALYSE_NEAR.send(player, new Replacement("<name>", target.getName()));
            return false;
        }

        this.cible = target;
        this.isUsed = true;
        this.isCanUse = false;
        Messages.PACIFISTA_ANALYSE_USE.send(player, new Replacement("<name>", target.getName()));

        new BukkitRunnable() {
            int timer = 0;

            @Override
            public void run() {

                if (isCanUse) {
                    Mugiwara.getInstance().removeActionBar(player, "analyse");
                    cancel();
                    return;
                }


                if (player == null) return;
                if (target == null) return;

                /**
                 * TODO COMBAT_LOG
                 */

                if (player.getLocation().distance(target.getLocation()) > 20) return;

                timer++;
                Mugiwara.getInstance().addActionBar(player, "&cAnalyse &8» &f " + TimeUtil.getReallyNiceTime2(timer * 1000L), "analyse");

                if (timer == 180) {
                    UPlayer uTarget = UPlayer.get(target);
                    Messages.PACIFISTA_ANALYSE_KILL.send(player,
                            new Replacement("<name>", target.getName()),
                            new Replacement("<kill>", uTarget.getKills()));
                } else if (timer == 300) {
                    StringBuilder stringBuilder = new StringBuilder();
                    target.getActivePotionEffects().stream()
                            .forEach(potionEffect -> stringBuilder.append(potionEffect.getType().getName()).append(" "));
                    Messages.PACIFISTA_ANALYSE_EFFETS.send(player,
                            new Replacement("<name>", target.getName()),
                            new Replacement("<effets>", (target.getActivePotionEffects().isEmpty() ? "Aucun" : stringBuilder.toString())));
                } else if (timer == 420) {
                    /**
                     * TODO GOLDEN_APPLE
                     */
                    Messages.PACIFISTA_ANALYSE_APPLE.send(player, new Replacement("<name>", target.getName()));
                } else if (timer == 600) {
                    /**
                     * TODO HEALTH
                     */
                    Messages.PACIFISTA_ANALYSE_HEALTH.send(player, new Replacement("<name>", target.getName()));
                } else if (timer == 900) {
                    /**
                     * TODO PRIME SYSTEM
                     */
                    Messages.PACIFISTA_ANALYSE_PRIME.send(player, new Replacement("<name>", target.getName()), new Replacement("<prime>", 0));
                    isUsed = false;
                    isCanUse = true;
                    Mugiwara.getInstance().removeActionBar(player, "analyse");
                    cancel();
                }

            }

        }.runTaskTimer(Mugiwara.getInstance(), 0L, 20L);

        return true;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public Integer getCooldownAmount() {
        return null;
    }
}

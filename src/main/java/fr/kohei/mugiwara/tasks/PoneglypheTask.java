package fr.kohei.mugiwara.tasks;

import fr.kohei.mugiwara.Mugiwara;
import fr.kohei.mugiwara.config.Messages;
import fr.kohei.mugiwara.config.Replacement;
import fr.kohei.mugiwara.poneglyphe.Poneglyphe;
import fr.kohei.mugiwara.poneglyphe.PoneglypheManager;
import fr.kohei.uhc.UHC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class PoneglypheTask extends BukkitRunnable {
    private HashMap<Integer, List<UUID>> reading;
    private HashMap<UUID, Integer> readingTime;

    public PoneglypheTask() {
        this.reading = new HashMap<>();
        this.readingTime = new HashMap<>();

        this.reading.put(1, new ArrayList<>());
        this.reading.put(2, new ArrayList<>());
        this.reading.put(3, new ArrayList<>());
        this.reading.put(4, new ArrayList<>());

        this.runTaskTimer(Mugiwara.getInstance(), 0, 20);
    }

    @Override
    public void run() {
        PoneglypheManager manager = Mugiwara.getInstance().getPoneglypheManager();
        for (Poneglyphe poneglyphe : new Poneglyphe[]{
                manager.getFirstPoneglyphe(), manager.getSecondPoneglyphe(), manager.getThirdPoneglyphe(), manager.getFourthPoneglyphe()
        }) {
            for (Player nearPlayer : getPlayers(poneglyphe.getId())) {
                if (nearPlayer.getLocation().distance(poneglyphe.getInitialLocation()) > 15) {
                    readingTime.remove(nearPlayer.getUniqueId());

                    List<UUID> readingPlayers = reading.get(poneglyphe.getId());
                    readingPlayers.remove(nearPlayer.getUniqueId());
                    reading.put(poneglyphe.getId(), readingPlayers);

                    Messages.PONEGLYPHE_FAR.send(nearPlayer, new Replacement("<number>", poneglyphe.getId()));
                }
            }


            List<Player> nearPlayers = Bukkit.getOnlinePlayers().stream()
                    .filter(player -> UHC.getGameManager().getPlayers().contains(player.getUniqueId()))
                    .filter(player -> player.getLocation().distance(poneglyphe.getInitialLocation()) <= 15)
                    .collect(Collectors.toList());

            if (nearPlayers.size() >= 5) {
                getPlayers(poneglyphe.getId()).forEach(Messages.PONEGLYPHE_TOOMUCHPLAYERS::send);
                return;
            }

            for (Player nearPlayer : nearPlayers) {
                int i = readingTime.getOrDefault(nearPlayer.getUniqueId(), 0);
                i++;
                Mugiwara.getInstance().addActionBar(nearPlayer, "&cLecture &8» &f" + i + "/90", "poneglyphe" + poneglyphe.getId());

                if(i >= 90) {
                    Mugiwara.getInstance().removeActionBar(nearPlayer, "poneglyphe" + poneglyphe.getId());
                    Messages.PONEGLYPHE_CAPTURE.send(nearPlayer, new Replacement("<number>", poneglyphe.getId()));
                }
            }
        }
    }

    private List<Player> getPlayers(int poneglypheId) {
        return this.reading.get(poneglypheId).stream()
                .filter(uuid -> Bukkit.getPlayer(uuid) != null)
                .map(Bukkit::getPlayer)
                .collect(Collectors.toList());
    }
}

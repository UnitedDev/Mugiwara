package fr.kohei.mugiwara.game.tasks;

import fr.kohei.mugiwara.Mugiwara;
import fr.kohei.mugiwara.utils.config.Messages;
import fr.kohei.mugiwara.utils.config.Replacement;
import fr.kohei.mugiwara.game.poneglyphe.Poneglyphe;
import fr.kohei.mugiwara.game.poneglyphe.PoneglypheManager;
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
    private final HashMap<Integer, List<UUID>> reading;
    private final HashMap<UUID, Integer> readingTime;
    private final HashMap<UUID, List<Integer>> read;

    public PoneglypheTask() {
        this.reading = new HashMap<>();
        this.read = new HashMap<>();
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
                    Mugiwara.getInstance().removeActionBar(nearPlayer, "poneglyphe" + poneglyphe.getId());
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
                if(read.getOrDefault(nearPlayer.getUniqueId(), new ArrayList<>()).contains(poneglyphe.getId())) {
                    continue;
                }
                if(!reading.get(poneglyphe.getId()).contains(nearPlayer.getUniqueId())) {
                    List<UUID> readingPlayers = reading.get(poneglyphe.getId());
                    readingPlayers.add(nearPlayer.getUniqueId());
                    reading.put(poneglyphe.getId(), readingPlayers);
                }

                int i = readingTime.getOrDefault(nearPlayer.getUniqueId(), 0) + 1;

                readingTime.put(nearPlayer.getUniqueId(), i);
                Mugiwara.getInstance().addActionBar(nearPlayer, "&cLecture &8» &f" + i + "/90", "poneglyphe" + poneglyphe.getId());

                if(i == 90) {
                    Mugiwara.getInstance().removeActionBar(nearPlayer, "poneglyphe" + poneglyphe.getId());
                    Messages.PONEGLYPHE_CAPTURE.send(nearPlayer, new Replacement("<number>", poneglyphe.getId()));

                    List<Integer> list = read.getOrDefault(nearPlayer.getUniqueId(), new ArrayList<>());
                    list.add(poneglyphe.getId());
                    read.put(nearPlayer.getUniqueId(), list);

                    int captures = Mugiwara.getInstance().getPoneglypheManager().getCaptures().getOrDefault(
                            nearPlayer.getUniqueId(),
                            0
                    );

                    captures++;
                    Mugiwara.getInstance().getPoneglypheManager().getCaptures().put(
                            nearPlayer.getUniqueId(),
                            captures
                    );

                    if(captures == 8) {
                        Mugiwara.getInstance().getPoneglypheManager().onAllCapture(nearPlayer);
                    }
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

package fr.uniteduhc.mugiwara.game.tasks;

import fr.uniteduhc.mugiwara.Mugiwara;
import fr.uniteduhc.mugiwara.game.player.MUPlayer;
import fr.uniteduhc.mugiwara.roles.RolesType;
import fr.uniteduhc.mugiwara.utils.config.Messages;
import fr.uniteduhc.mugiwara.utils.config.Replacement;
import fr.uniteduhc.mugiwara.game.events.poneglyphe.Poneglyphe;
import fr.uniteduhc.mugiwara.game.events.poneglyphe.PoneglypheManager;
import fr.uniteduhc.mugiwara.utils.utils.Utils;
import fr.uniteduhc.uhc.UHC;
import fr.uniteduhc.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
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
    private boolean captured;

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
            if(!poneglyphe.isSpawned()) continue;
            for (Player nearPlayer : getPlayers(poneglyphe.getId())) {
                if (nearPlayer.getLocation().distance(poneglyphe.getInitialLocation()) > 15) {
                    readingTime.remove(nearPlayer.getUniqueId());

                    List<UUID> readingPlayers = reading.get(poneglyphe.getId());
                    readingPlayers.remove(nearPlayer.getUniqueId());
                    Mugiwara.getInstance().removeActionBar(nearPlayer, "poneglyphe" + poneglyphe.getId());
                    reading.put(poneglyphe.getId(), readingPlayers);

                    if(!read.getOrDefault(nearPlayer.getUniqueId(), new ArrayList<>()).contains(poneglyphe.getId())) {
                        Messages.PONEGLYPHE_FAR.send(nearPlayer, new Replacement("<number>", poneglyphe.getId()));
                    }
                }
            }

            List<Player> nearPlayers = Utils.getPlayers().stream()
                    .filter(player -> UHC.getInstance().getGameManager().getPlayers().contains(player.getUniqueId()))
                    .filter(player -> player.getLocation().distance(poneglyphe.getInitialLocation()) <= 15)
                    .collect(Collectors.toList());

            if (nearPlayers.size() > 5) {
                getPlayers(poneglyphe.getId()).forEach(Messages.PONEGLYPHE_TOOMUCHPLAYERS::send);
                return;
            }

            if (Mugiwara.getInstance().getPoneglypheManager().getRemoved() != null &&
                    Mugiwara.getInstance().getPoneglypheManager().getRemoved() == poneglyphe.getId()) continue;

            for (Player nearPlayer : nearPlayers) {
                if (read.getOrDefault(nearPlayer.getUniqueId(), new ArrayList<>()).contains(poneglyphe.getId())) {
                    continue;
                }
                if (!reading.get(poneglyphe.getId()).contains(nearPlayer.getUniqueId())) {
                    List<UUID> readingPlayers = reading.get(poneglyphe.getId());
                    readingPlayers.add(nearPlayer.getUniqueId());
                    reading.put(poneglyphe.getId(), readingPlayers);
                }

                int i = readingTime.getOrDefault(nearPlayer.getUniqueId(), 0) + 1;

                readingTime.put(nearPlayer.getUniqueId(), i);
                RolesType role = MUPlayer.get(nearPlayer).getRole().getRole();
                Mugiwara.getInstance().addActionBar(nearPlayer, "&cLecture &8» &f" + ((role == RolesType.ROBIN ? 30 : 90) - i), "poneglyphe" + poneglyphe.getId());

                if (i >= 90 && role != RolesType.ROBIN) {
                    capture(nearPlayer, poneglyphe);
                } else if (i >= 30 && role == RolesType.ROBIN) {
                    capture(nearPlayer, poneglyphe);
                }
            }
        }
    }

    private void capture(Player player, Poneglyphe poneglyphe) {
        if (Mugiwara.getInstance().getPoneglypheManager().getRemoved() != null &&
                Mugiwara.getInstance().getPoneglypheManager().getRemoved() == poneglyphe.getId()) return;


        Mugiwara.getInstance().removeActionBar(player, "poneglyphe" + poneglyphe.getId());
        Messages.PONEGLYPHE_CAPTURE.send(player, new Replacement("<number>", poneglyphe.getId()));

        List<Integer> list = read.getOrDefault(player.getUniqueId(), new ArrayList<>());
        list.add(poneglyphe.getId());
        read.put(player.getUniqueId(), list);

        int captures = Mugiwara.getInstance().getPoneglypheManager().getCaptures().getOrDefault(
                player.getUniqueId(),
                0
        );

        captures++;
        Mugiwara.getInstance().getPoneglypheManager().getCaptures().put(
                player.getUniqueId(),
                captures
        );

        if (captures == 4) {
            if (!captured) {
                Mugiwara.getInstance().getOnePieceManager().startOnePiece();
                captured = true;
            }

            RolesType role = MUPlayer.get(player).getRole().getRole();
            if (role == RolesType.NAMI) {
                realCoordinates(player);
            } else {
                if (role == RolesType.ZORO) {
                    player.sendMessage(ChatUtil.prefix("&fVous allez recevoir &a8 &fcoordonnées dont une " +
                            "exacte pour le &aOnePiece&f."));
                    falseCoordinates(player);
                    falseCoordinates(player);
                    falseCoordinates(player);
                    realCoordinates(player);
                    falseCoordinates(player);
                    falseCoordinates(player);

                } else {
                    player.sendMessage(ChatUtil.prefix("&fVous allez recevoir &a4 &fcoordonnées dont une " +
                            "exacte pour le &aOnePiece&f."));
                    falseCoordinates(player);
                    realCoordinates(player);
                }
                falseCoordinates(player);
                falseCoordinates(player);
            }
        }

        if (captures == 8) {
            Mugiwara.getInstance().getPoneglypheManager().onAllCapture(player);
        }
    }

    private List<Player> getPlayers(int poneglypheId) {
        return this.reading.get(poneglypheId).stream()
                .filter(uuid -> Bukkit.getPlayer(uuid) != null)
                .map(Bukkit::getPlayer)
                .collect(Collectors.toList());
    }

    private void falseCoordinates(Player player) {
        Location falseLoc = new Location(UHC.getInstance().getGameManager().getUhcWorld(), (int) (Math.random() * 200), (40 + (int) (Math.random() * 10)), (int) (Math.random() * 200));
        player.sendMessage(ChatUtil.prefix(" &f&l» " +
                "&a" + falseLoc.getBlockX() + "&f, " +
                "&a" + falseLoc.getBlockY() + "&f, " +
                "&a" + falseLoc.getBlockZ()
        ));

        List<Location> locations = Mugiwara.getInstance().getOnePieceManager().getCoordinates().getOrDefault(player.getUniqueId(), new ArrayList<>());
        locations.add(falseLoc);
        Mugiwara.getInstance().getOnePieceManager().getCoordinates().put(player.getUniqueId(), locations);
    }

    private void realCoordinates(Player player) {
        Location onePiece = Mugiwara.getInstance().getOnePieceManager().getOnePiece().getLocation();
        player.sendMessage(ChatUtil.prefix(" &f&l» " +
                "&a" + onePiece.getBlockX() + "&f, " +
                "&a" + onePiece.getBlockY() + "&f, " +
                "&a" + onePiece.getBlockZ()
        ));

        List<Location> locations = Mugiwara.getInstance().getOnePieceManager().getCoordinates().getOrDefault(player.getUniqueId(), new ArrayList<>());
        locations.add(onePiece);
        Mugiwara.getInstance().getOnePieceManager().getCoordinates().put(player.getUniqueId(), locations);
    }
}

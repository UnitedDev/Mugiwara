package fr.kohei.mugiwara.utils;

import fr.kohei.uhc.UHC;
import fr.kohei.uhc.game.EpisodeManager;
import fr.kohei.uhc.game.GameManager;

public class Utils {

    public static int getTimeBeforeEpisode() {
        GameManager gameManager = UHC.getGameManager();
        EpisodeManager episodeManager = gameManager.getEpisodeManager();

        int episodeTime = (episodeManager.getEpisode() - 1) * 20 * 60;

        if (episodeManager.getEpisode() == 1) {
            return 20 * 60 - gameManager.getDuration();
        }

        return episodeTime - (gameManager.getDuration() - episodeTime);
    }

    public static String itemFormat(String name) {
        return "&8❘ &c&l" + name + " &8(&7Clic-droit&8)";
    }

    public static String notClickItem(String name) {
        return "&8❘ &c&l" + name;
    }

}

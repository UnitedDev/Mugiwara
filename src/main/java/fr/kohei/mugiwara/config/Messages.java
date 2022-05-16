package fr.kohei.mugiwara.config;

import fr.kohei.mugiwara.Mugiwara;
import fr.kohei.utils.ChatUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

@Getter
@AllArgsConstructor
public enum Messages {

    COOLDOWN_EXPIRE("&fLe cooldown pour le pouvoir &c<name> &fvient d'expirer."),

    LUFFY_WATER("&cNe restez pas trop longtemps dans l'eau."),
    LUFFY_GOMUGOMUNOMI("&fVous avez utilisé votre pouvoir &6Gomu Gomu no Mi&f."),
    LUFFY_FIRSTPOWER("&fVous avez utilisé votre premier pouvoir."),
    LUFFY_SECONDPOWER("&fVous avez utilisé votre deuxième pouvoir."),
    LUFFY_THIRDPOWER("&fVous avez utilisé votre troisième pouvoir."),

    ZORO_FIRSTKILL("&fVous avez obtenu &bSpeed I &fpour votre kill."),
    ZORO_SECONDKILL("&fVous avez obtenu &bSpeed II &fpour votre kill."),
    ZORO_THIRDKILL("&fVous avez obtenu &cForce I &fpour votre kill."),
    ZORO_FOURTHDKILL("&fVous avez obtenu &c1 coeur &fet &6+20% d'enflammer un joueur en le tapant &fpour votre kill."),

    ;

    @Setter
    private String display;

    @SneakyThrows
    public static void init() {
        FileConfiguration config = Mugiwara.getInstance().getConfig();
        config.load(new File(Mugiwara.getInstance().getDataFolder() + "/config.yml"));

        for (Messages value : values()) {
            if (config.get("messages." + value.name().replace("_", ".")) == null) {
                config.set("messages." + value.name().replace("_", "."), value.getDisplay());
                Mugiwara.getInstance().saveConfig();
            }

            value.setDisplay(config.getString("messages." + value.name().replace("_", ".")));
        }
    }

    public void send(Player player, Replacement... replacements) {
        String message = ChatUtil.prefix(getDisplay());
        for (Replacement replacement : replacements) {
            message = message.replace(replacement.getIndex(), replacement.getReplace());
        }

        if (display.equalsIgnoreCase("false")) return;

        player.sendMessage(message);
    }

}

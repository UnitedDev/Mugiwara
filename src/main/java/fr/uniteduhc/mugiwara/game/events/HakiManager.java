package fr.uniteduhc.mugiwara.game.events;

import fr.uniteduhc.mugiwara.Mugiwara;
import fr.uniteduhc.mugiwara.game.events.haki.AbstractHaki;
import fr.uniteduhc.mugiwara.game.events.haki.HakiListener;
import fr.uniteduhc.mugiwara.game.events.haki.HakiType;
import fr.uniteduhc.mugiwara.game.player.MUPlayer;
import fr.uniteduhc.mugiwara.roles.RolesType;
import fr.uniteduhc.mugiwara.utils.config.Messages;
import fr.uniteduhc.mugiwara.utils.config.Replacement;
import fr.uniteduhc.mugiwara.utils.utils.Utils;
import fr.uniteduhc.utils.ItemBuilder;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public class HakiManager {
    private final HashMap<UUID, AbstractHaki> playersHaki;
    private final HashMap<UUID, List<AbstractHaki>> alreadyInstancied;

    public HakiManager(Plugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(new HakiListener(), plugin);

        this.alreadyInstancied = new HashMap<>();
        playersHaki = new HashMap<>();
    }

    public void onRoles(Player player) {
        RolesType.MURole role = MUPlayer.get(player).getRole();
        if (role == null) return;

        List<HakiType> hakis = getAllHakisForPlayer(player);
        if (hakis == null || hakis.isEmpty()) return;

        HakiType newHaki = hakis.get(0);
        setHaki(player, newHaki);
        player.getInventory().addItem(this.getHakiItem());
        Messages.HAKI_NEW_ONE.send(player, new Replacement("<haki>", newHaki.getDisplayName()));
    }

    @Nullable
    public AbstractHaki getHaki(Player player) {
        return playersHaki.get(player.getUniqueId());
    }

    @SneakyThrows
    public void setHaki(Player player, HakiType hakiType) {
        AbstractHaki abstractHaki;
        List<AbstractHaki> instancied = alreadyInstancied.getOrDefault(player.getUniqueId(), new ArrayList<>());
        if (instancied.stream().anyMatch(ab -> ab.getHakiType().equals(hakiType))) {
            abstractHaki = instancied.stream().filter(ab -> ab.getHakiType().equals(hakiType)).findFirst().orElse(null);
        } else {
            abstractHaki = hakiType.getHaki().newInstance();
            instancied.add(abstractHaki);
            alreadyInstancied.put(player.getUniqueId(), instancied);
        }
        if (abstractHaki instanceof Listener)
            Bukkit.getPluginManager().registerEvents((Listener) abstractHaki, Mugiwara.getInstance());
        this.playersHaki.put(player.getUniqueId(), abstractHaki);
    }

    public List<HakiType> getAllHakisForPlayer(Player player) {
        RolesType.MURole role = MUPlayer.get(player).getRole();
        if (role == null) return new ArrayList<>();

        return getAllHakisForRole(role.getRole());
    }

    public List<HakiType> getAllHakisForRole(RolesType role) {
        return Arrays.stream(HakiType.values())
                .filter(hakiType -> hakiType.getRoles().contains(role))
                .collect(Collectors.toList());
    }

    public ItemStack getHakiItem() {
        return new ItemBuilder(Material.NETHER_STAR).setName(Utils.itemFormat("&c&lHaki")).toItemStack();
    }

    public static final List<RolesType> HAKI_ARMEMENT = Arrays.asList(RolesType.LUFFY, RolesType.ZORO, RolesType.SANJI,
            RolesType.JIMBE, RolesType.LAW, RolesType.EUSTASS, RolesType.GARP, RolesType.AKAINU, RolesType.FUJITORA,
            RolesType.SENGOKU, RolesType.TSURU, RolesType.DRAKE, RolesType.SMOKER, RolesType.MIHAWK, RolesType.HANCOCK,
            RolesType.BIG_MOM, RolesType.KATAKURI, RolesType.KAIDO, RolesType.QUEEN, RolesType.SABO, RolesType.KUZAN,
            RolesType.TEACH /*TODO RAJOUTER TASHIGI ET OVEN*/);
    public static final List<RolesType> HAKI_OBSERVATION = Arrays.asList(RolesType.LUFFY, RolesType.ZORO, RolesType.USSOP,
            RolesType.SANJI, RolesType.JIMBE, RolesType.LAW, RolesType.GARP, RolesType.AKAINU, RolesType.FUJITORA,
            RolesType.SENGOKU, RolesType.TSURU, RolesType.DRAKE, RolesType.SMOKER, RolesType.MIHAWK, RolesType.HANCOCK,
            RolesType.BIG_MOM, RolesType.KATAKURI, RolesType.KAIDO, RolesType.SABO, RolesType.KUZAN, RolesType.TEACH
            /*TODO RAJOUTER TASHIGI ET OVEN*/);
    public static final List<RolesType> HAKI_ROYAL = Arrays.asList(RolesType.LUFFY, RolesType.ZORO, RolesType.EUSTASS,
            RolesType.SENGOKU, RolesType.HANCOCK, RolesType.BIG_MOM, RolesType.KATAKURI, RolesType.KAIDO);
}

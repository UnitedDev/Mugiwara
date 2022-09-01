package fr.uniteduhc.mugiwara.game.events.haki;

import fr.uniteduhc.mugiwara.Mugiwara;
import lombok.Getter;
import org.bukkit.entity.Player;

@Getter
public abstract class AbstractHaki {
    public void onRightClick(Player player) {}

    public void onDamageWithHaki(Player player) {}

    public abstract HakiType getHakiType();

    public boolean hasHaki(Player player) {
        AbstractHaki hakiType = Mugiwara.getInstance().getHakiManager().getHaki(player);

        if (hakiType == null) return false;
        return hakiType.equals(this);
    }

}

package fr.uniteduhc.mugiwara.game.events.poneglyphe;

import fr.uniteduhc.utils.Cuboid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;

@Setter
@Getter
@AllArgsConstructor
public class Poneglyphe {

    private int id;
    private Location initialLocation;
    private Cuboid cuboid;
    private int timer;
    private boolean spawned;

    public Poneglyphe(int id, Location initialLocation, Cuboid cuboid, int timer) {
        this.id = id;
        this.initialLocation = initialLocation;
        this.cuboid = cuboid;
        this.timer = timer;
        this.spawned = false;
    }
}

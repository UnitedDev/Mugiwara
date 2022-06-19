package fr.kohei.mugiwara.poneglyphe;

import fr.kohei.utils.Cuboid;
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

}

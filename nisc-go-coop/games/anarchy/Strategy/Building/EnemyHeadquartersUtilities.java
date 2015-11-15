package games.anarchy.Strategy.Building;

import games.anarchy.Building;
import games.anarchy.Warehouse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by squeaky on 11/14/15.
 */
public class EnemyHeadquartersUtilities {
    private Warehouse enemyHeadquarters;
    public EnemyHeadquartersUtilities(Warehouse enemyHeadquarters) {
        this.enemyHeadquarters = enemyHeadquarters;
    }

    public Warehouse getEnemyHeadquarters() {
        return enemyHeadquarters;
    }

    public List<Building> getEnemyHeadquartersNeighbors() {
        List<Building> neighbors = new ArrayList<>();
        neighbors.add(enemyHeadquarters.buildingEast);
        neighbors.add(enemyHeadquarters.buildingNorth);
        neighbors.add(enemyHeadquarters.buildingSouth);
        neighbors.add(enemyHeadquarters.buildingWest);

        return neighbors;
    }
}

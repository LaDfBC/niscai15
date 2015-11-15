package games.anarchy.Strategy.Building;


import games.anarchy.Building;
import games.anarchy.Game;
import games.anarchy.Warehouse;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by squeaky on 11/15/15.
 */
public class FriendlyHeadquartersUtilities {
    private Warehouse friendlyHeadquarters;
    private Game game;
    public FriendlyHeadquartersUtilities(Warehouse friendlyHeadquarters, Game game) {
        this.friendlyHeadquarters = friendlyHeadquarters;
        this.game = game;
    }

    public Map<Building, WeatherStationUtilities.CardinalDirection> getFriendlyHeadquartersNeighbors() {
        Map<Building, WeatherStationUtilities.CardinalDirection> neighbors = new HashMap<>();
        if(friendlyHeadquarters.buildingEast != null) {
            neighbors.put(friendlyHeadquarters.buildingEast, WeatherStationUtilities.CardinalDirection.east);
        }
        if(friendlyHeadquarters.buildingNorth != null) {
            neighbors.put(friendlyHeadquarters.buildingNorth, WeatherStationUtilities.CardinalDirection.north);
        }
        if(friendlyHeadquarters.buildingSouth != null) {
            neighbors.put(friendlyHeadquarters.buildingSouth, WeatherStationUtilities.CardinalDirection.south);
        }
        if(friendlyHeadquarters.buildingWest != null) {
            neighbors.put(friendlyHeadquarters.buildingWest, WeatherStationUtilities.CardinalDirection.west);
        }

        return neighbors;
    }

}

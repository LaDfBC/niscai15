package games.anarchy.Strategy.Building;

import games.anarchy.Building;
import games.anarchy.Forecast;
import games.anarchy.Game;
import games.anarchy.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeffrey on 11/14/2015.
 */
public class WeatherStationUtilities {

    //Fire after wind
    public class FireAdded {
        public final Building building;
        public final int fireAdded;

        public FireAdded(Building building, int fireAdded) {
            this.building = building;
            this.fireAdded = fireAdded;
        }
    }

    public enum WeatherDirection {
        Forward,
        Clockwise,
        CounterClockwise,
        Backward
    }

    public enum CardinalDirection {
        north(0),
        east(1),
        south(2),
        west(3);

        public final int directionInt;

        CardinalDirection(int dir) {
            directionInt = dir;
        }

        public static CardinalDirection getDirectionFromInt(int dir) {
            int modDir = dir % 4;
            switch (modDir) {
                case 0:
                    return north;
                case 1:
                    return east;
                case 2:
                    return south;
                default:
                    return west;
            }
        }

        public CardinalDirection rotate180() {
            return getDirectionFromInt(directionInt+2);
        }

        public CardinalDirection rotateCounterClockwise() {
            return getDirectionFromInt(directionInt+4-1);
        }

        public CardinalDirection rotateClockwise() {
            return getDirectionFromInt(directionInt+1);
        }
    }

    private final Player player;
    private final Game game;

    public WeatherStationUtilities(Player player, Game game) {
        this.player = player;
        this.game = game;
    }

    /**
     * gets the fire added per building for a direction
     * can be used to go through each building
     * @param direction
     * @return
     */
    public List<FireAdded> getFireAddedForDirection(WeatherDirection direction) {
        CardinalDirection dir = Enum.valueOf(CardinalDirection.class, game.currentForecast.direction);
        if (direction == WeatherDirection.Clockwise) {
            dir = dir.rotateClockwise();
        }
        if (direction == WeatherDirection.CounterClockwise) {
            dir = dir.rotateCounterClockwise();
        }
        if (direction == WeatherDirection.Backward) {
            dir = dir.rotate180();
        }

        int intensity = game.currentForecast.intensity;

        List<FireAdded> fireAdded = new ArrayList<>();

        List<Building> buildingsOnFire = getBuildingsOnFire();
        for (Building b : buildingsOnFire) {
            Building adjacentBuilding = null;
            switch (dir) {
                case east:
                    adjacentBuilding = b.buildingEast;
                    break;
                case north:
                    adjacentBuilding = b.buildingNorth;
                    break;
                case south:
                    adjacentBuilding = b.buildingSouth;
                    break;
                case west:
                    adjacentBuilding = b.buildingWest;
                    break;
            }
            if (adjacentBuilding != null) {
                fireAdded.add(new FireAdded(adjacentBuilding, intensity));
            }
        }

        return fireAdded;
    }

    public List<Building> getBuildingsOnFire() {
        List<Building> onFire = new ArrayList<>();
        for (Building b : game.buildings) {
            if (b.fire > 0) {
                onFire.add(b);
            }
        }
        return onFire;
    }

    public Forecast getNextWeather() {
        return game.nextForecast;
    }
}

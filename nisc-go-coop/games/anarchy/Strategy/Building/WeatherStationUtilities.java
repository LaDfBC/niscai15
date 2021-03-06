package games.anarchy.Strategy.Building;

import games.anarchy.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
                int fireSpread = Math.min(intensity, b.fire);
                fireAdded.add(new FireAdded(adjacentBuilding, fireSpread));
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

    public boolean isWeatherOpposite(String current, String desired) {
        if(current.equals("west") && desired.equals("east")) {
            return true;
        }
        if(current.equals("east") && desired.equals("west")) {
            return true;
        }
        if(current.equals("north") && desired.equals("south")) {
            return true;
        }
        if(current.equals("south") && desired.equals("north")) {
            return true;
        }
        return false;
    }

    public WeatherDirection getDirection(String current, String desired) {
        if(current.equals("north")) {
            if(desired.equals("east")) {
                return WeatherDirection.Clockwise;
            }
            if(desired.equals("west")) {
                return WeatherDirection.CounterClockwise;
            }
            if(desired.equals("south")) {
                return WeatherDirection.Backward;
            }
            return WeatherDirection.Forward;
        } else if(current.equals("south")) {
            if(desired.equals("west")) {
                return WeatherDirection.Clockwise;
            }
            if(desired.equals("east")) {
                return WeatherDirection.CounterClockwise;
            }
            if(desired.equals("north")) {
                return WeatherDirection.Backward;
            }
            return WeatherDirection.Forward;
        } else if(current.equals("east")) {
            if(desired.equals("south")) {
                return WeatherDirection.Clockwise;
            }
            if(desired.equals("north")) {
                return WeatherDirection.CounterClockwise;
            }
            if(desired.equals("west")) {
                return WeatherDirection.Backward;
            }
            return WeatherDirection.Forward;
        } else if(current.equals("west")) {
            if(desired.equals("north")) {
                return WeatherDirection.Clockwise;
            }
            if(desired.equals("south")) {
                return WeatherDirection.CounterClockwise;
            }
            if(desired.equals("east")) {
                return WeatherDirection.Backward;
            }
            return WeatherDirection.Forward;
        }

        return null; // Shouldn't happen
    }

    public WeatherStation getNextBribeableWeatherStation() {
        for(WeatherStation weatherStation : player.weatherStations) {
            if(!weatherStation.bribed && weatherStation.health > 0) {
                return weatherStation;
            }
        }

        return null;
    }

    public String getOppositeOf(CardinalDirection value) {
        if(value.name().equals("south")) {
            return "north";
        }
        if(value.name().equals("north")) {
            return "south";
        }
        if(value.name().equals("east")) {
            return "west";
        }
        if(value.name().equals("west")) {
            return "east";
        }
        return "YAHOOOOOO!";
    }
}

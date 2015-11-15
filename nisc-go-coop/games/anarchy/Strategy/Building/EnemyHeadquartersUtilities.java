package games.anarchy.Strategy.Building;

import games.anarchy.*;
import games.anarchy.Strategy.Enum.SourceOfDamage;
import games.anarchy.Strategy.Heuristic.Pair;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by squeaky on 11/14/15.
 */
public class EnemyHeadquartersUtilities {
    private Warehouse enemyHeadquarters;
    private Game game;
    private WeatherStationUtilities weatherStationUtilities;

    public EnemyHeadquartersUtilities(Warehouse enemyHeadquarters, Game game) {
        this.enemyHeadquarters = enemyHeadquarters;
        this.game = game;
        this.weatherStationUtilities = weatherStationUtilities;
    }

    public Warehouse getEnemyHeadquarters() {
        return enemyHeadquarters;
    }

    public Map<Building, WeatherStationUtilities.CardinalDirection> getEnemyHeadquartersNeighbors() {
        Map<Building, WeatherStationUtilities.CardinalDirection> neighbors = new HashMap<>();
        if(enemyHeadquarters.buildingEast != null) {
            neighbors.put(enemyHeadquarters.buildingEast, WeatherStationUtilities.CardinalDirection.east);
        }
        if(enemyHeadquarters.buildingNorth != null) {
            neighbors.put(enemyHeadquarters.buildingNorth, WeatherStationUtilities.CardinalDirection.north);
        }
        if(enemyHeadquarters.buildingSouth != null) {
            neighbors.put(enemyHeadquarters.buildingSouth, WeatherStationUtilities.CardinalDirection.south);
        }
        if(enemyHeadquarters.buildingWest != null) {
            neighbors.put(enemyHeadquarters.buildingWest, WeatherStationUtilities.CardinalDirection.west);
        }

        return neighbors;
    }

    public Map<SourceOfDamage, Pair<Integer, Integer>> getWaysToDamageEnemyHeadquarters(
            PoliceDepartmentUtilities policeDepartmentUtilities,
            WeatherStationUtilities weatherStationUtilities) {
        Map<SourceOfDamage, Pair<Integer, Integer>> sourcesOfDamage = new HashMap<>();

        //Current Burn
        sourcesOfDamage.put(SourceOfDamage.FIRE_ALONE, new Pair<>(enemyHeadquarters.fire, 0));

        //Police
        Integer possibleDamage = policeDepartmentUtilities.atLeastOnePoliceStationStanding() ? enemyHeadquarters.exposure : 0;
        sourcesOfDamage.put(SourceOfDamage.POLICE, new Pair<>(possibleDamage, 1));

        //Current fire + wind
        Pair<WeatherStationUtilities.CardinalDirection, Pair<Integer, Integer>> fireAndWindDamage = getMaxDamageWithCurrentFire(weatherStationUtilities);
        sourcesOfDamage.put(SourceOfDamage.EXISTING_PLUS_WIND, new Pair<>(fireAndWindDamage.getValue().getKey(), fireAndWindDamage.getValue().getValue()));

        //Ignite + wind
        //TODO:  Assuming worst case for now
        sourcesOfDamage.put(SourceOfDamage.IGNITE_PLUS_WIND, new Pair<>(10, 20));

        return sourcesOfDamage;
    }



    //<Damage, Clicks>
    public Pair<WeatherStationUtilities.CardinalDirection, Pair<Integer, Integer>> getMaxDamageWithCurrentFire(WeatherStationUtilities weatherStationUtilities) {
        Pair<WeatherStationUtilities.CardinalDirection, Pair<Integer, Integer>> damageToClicks = new Pair<>(WeatherStationUtilities.CardinalDirection.east, new Pair<>(0,0));

        for(Map.Entry<Building, WeatherStationUtilities.CardinalDirection> buildingEntry : getEnemyHeadquartersNeighbors().entrySet()) {
            Forecast desiredForecast = new Forecast();
            if (buildingEntry.getValue().equals(WeatherStationUtilities.CardinalDirection.east)) {
                desiredForecast.direction = "west";
            } else if (buildingEntry.getValue().equals(WeatherStationUtilities.CardinalDirection.west)) {
                desiredForecast.direction = "east";
            } else if (buildingEntry.getValue().equals(WeatherStationUtilities.CardinalDirection.north)) {
                desiredForecast.direction = "south";
            } else {
                desiredForecast.direction = "north";
            }
            int damage = Math.min(enemyHeadquarters.health, game.WINDMAX);
            desiredForecast.intensity = damage;
            if(damage > damageToClicks.getValue().getKey()) {
                damageToClicks = new Pair<>(buildingEntry.getValue(), new Pair<>(Math.min(enemyHeadquarters.health, game.WINDMAX),
                        actionsRequiredToChangeToMinimumDesiredWeather(weatherStationUtilities.getNextWeather(), desiredForecast)));
            }
        }

        return damageToClicks;
    }

    /**
     * If desired wind is higher than the actual, do nothing!  It'll still work!
     * @param actualNextForecast
     * @param desiredNextForecast
     * @return
     */
    private int actionsRequiredToChangeToMinimumDesiredWeather(Forecast actualNextForecast, Forecast desiredNextForecast) {
        int totalActionsRequired = 0;
        if(!actualNextForecast.direction.equals(desiredNextForecast.direction)) {
            totalActionsRequired++;
        } if(weatherStationUtilities.isWeatherOpposite(actualNextForecast.direction, desiredNextForecast.direction)) {
            totalActionsRequired++;
        }

        if(actualNextForecast.intensity > desiredNextForecast.intensity) {
            return totalActionsRequired;
        } else {
            totalActionsRequired += desiredNextForecast.intensity - actualNextForecast.intensity;
            return totalActionsRequired;
        }
    }
}

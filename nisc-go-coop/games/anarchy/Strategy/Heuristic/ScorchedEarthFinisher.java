package games.anarchy.Strategy.Heuristic;

import games.anarchy.Player;
import games.anarchy.Strategy.Building.EnemyHeadquartersUtilities;
import games.anarchy.Strategy.Building.WeatherStationUtilities;
import games.anarchy.Strategy.Enum.SourceOfDamage;
import games.anarchy.WeatherStation;

import java.util.List;
import java.util.Map;

/**
 * Created by squeaky on 11/14/15.
 */
public class ScorchedEarthFinisher implements ChosenStrategy {
    private List<SourceOfDamage> thingsThatKill;
    private Map<SourceOfDamage, Pair<Integer, Integer>> waysToKill;
    private Player player;
    private EnemyHeadquartersUtilities enemyHeadquartersUtilities;
    private WeatherStationUtilities weatherStationUtilities;

    public ScorchedEarthFinisher(List<SourceOfDamage> mapThingsThatKill,
                                 Map<SourceOfDamage, Pair<Integer, Integer>> waysToKill,
                                 Player player,
                                 EnemyHeadquartersUtilities enemyHeadquartersUtilities,
                                 WeatherStationUtilities weatherStationUtilities) {
        this.thingsThatKill = mapThingsThatKill;
        this.waysToKill = waysToKill;
        this.player = player;
        this.enemyHeadquartersUtilities = enemyHeadquartersUtilities;
        this.weatherStationUtilities = weatherStationUtilities;
    }
    @Override
    public int takeActions() {
        int actions = 0;
        for(SourceOfDamage killers : thingsThatKill) {
            if(killers == SourceOfDamage.FIRE_ALONE) {
                actions += 0; //Intentional No op
            } else if(killers == SourceOfDamage.POLICE) {
                player.policeDepartments.get(0).raid(enemyHeadquartersUtilities.getEnemyHeadquarters());
            } else if(killers == SourceOfDamage.EXISTING_PLUS_WIND) {
                Pair<WeatherStationUtilities.CardinalDirection, Pair<Integer, Integer>> warehouseOfDeliverance =
                        enemyHeadquartersUtilities.getMaxDamageWithCurrentFire(weatherStationUtilities);
                boolean needsToChangeDirection = true;
                if(warehouseOfDeliverance.getKey().equals(WeatherStationUtilities.CardinalDirection.east) &&
                        weatherStationUtilities.getNextWeather().direction.equals("west")) {
                    needsToChangeDirection = false;
                }
                if(warehouseOfDeliverance.getKey().equals(WeatherStationUtilities.CardinalDirection.west) &&
                        weatherStationUtilities.getNextWeather().direction.equals("east")) {
                    needsToChangeDirection = false;
                }
                if(warehouseOfDeliverance.getKey().equals(WeatherStationUtilities.CardinalDirection.north) &&
                        weatherStationUtilities.getNextWeather().direction.equals("south")) {
                    needsToChangeDirection = false;
                }
                if(warehouseOfDeliverance.getKey().equals(WeatherStationUtilities.CardinalDirection.south) &&
                        weatherStationUtilities.getNextWeather().direction.equals("north")) {
                    needsToChangeDirection = false;
                }
                boolean changedDirection = false;
                for(WeatherStation weatherStation : player.weatherStations) {
                    if(weatherStation.health > 0) {
                        if (!changedDirection && needsToChangeDirection) {
                            if(!(weatherStationUtilities.getDirection(warehouseOfDeliverance.getKey().name(),
                                    weatherStationUtilities.getNextWeather().direction) == WeatherStationUtilities.WeatherDirection.Backward)) {
                                changedDirection = true;
                            }
                            weatherStation.rotate();
                        } else {
                            weatherStation.intensify();
                        }
                    }
                }
            } else {
                for(WeatherStation weatherStation : player.weatherStations) {
                    weatherStation.intensify();
                }
//                weatherStationUtilities.getNextWeather().direction;
            }
        }

        return player.bribesRemaining;
    }
}

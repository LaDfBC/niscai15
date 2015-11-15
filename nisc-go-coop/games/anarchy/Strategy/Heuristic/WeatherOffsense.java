package games.anarchy.Strategy.Heuristic;

import games.anarchy.Game;
import games.anarchy.Player;
import games.anarchy.Strategy.Building.WeatherStationUtilities;
import games.anarchy.WeatherStation;
import javafx.util.Pair;

import java.util.List;
import java.util.Stack;

/**
 * Created by Jeffrey on 11/14/2015.
 */
public class WeatherOffsense {
    public static final int FRIENDLY_HQ_DAMAGE_WEIGHT = 5;
    public static final int ENEMY_HQ_DAMAGE_WEIGHT = 5;


    //This is our player
    private final Player player;
    private final WeatherStationUtilities weather;
    private final Game game;

    public WeatherOffsense(Player player, WeatherStationUtilities weather, Game game) {
        this.player = player;
        this.weather = weather;
        this.game = game;
    }

    /**
     *
     * @param maxPointsToChangeWeather The maximum number of points we will try to change the weather (up or down)
     */
    public void minMaxDamage(int maxPointsToChangeWeather) {
        //try to maximize the amount of damage we can do to
        // their buildings while minimizing the amount of damage done to our own

        //First case, if none of the directions are better for us than them, decrease the weather
        Pair<Integer,Integer> noDirectionChange = getFriendlyEnemyDamage(WeatherStationUtilities.WeatherDirection.Forward);
        Pair<Integer,Integer> clockwise = getFriendlyEnemyDamage(WeatherStationUtilities.WeatherDirection.Clockwise);
        Pair<Integer,Integer> counterClockwise = getFriendlyEnemyDamage(WeatherStationUtilities.WeatherDirection.CounterClockwise);

        //find the one with the largest difference between their damage and our damage
        int maxDamage = noDirectionChange.getValue()-noDirectionChange.getKey();
        WeatherStationUtilities.WeatherDirection maxDir = WeatherStationUtilities.WeatherDirection.Forward;


        int diffClockwise = clockwise.getValue()-clockwise.getKey();
        if (diffClockwise > maxDamage) {
            maxDamage = diffClockwise;
            maxDir = WeatherStationUtilities.WeatherDirection.Clockwise;
        }

        int diffCounterClockwise = counterClockwise.getValue()-counterClockwise.getKey();
        if (diffCounterClockwise > maxDamage) {
            maxDamage = diffCounterClockwise;
            maxDir = WeatherStationUtilities.WeatherDirection.CounterClockwise;
        }

        //first move if we can
        List<WeatherStation> weatherStations = player.weatherStations;
        Stack<WeatherStation> stationStack = new Stack<>();
        for (WeatherStation ws : weatherStations) {
            if (!ws.bribed) {
                stationStack.push(ws);
            }
        }

        if (stationStack.empty() || player.bribesRemaining == 0) {
            return;
        }

        if (maxDir == WeatherStationUtilities.WeatherDirection.Clockwise || maxDir == WeatherStationUtilities.WeatherDirection.CounterClockwise ) {
            WeatherStation ws = stationStack.pop();
            if (maxDir == WeatherStationUtilities.WeatherDirection.Clockwise) {
                ws.rotate(false);
            } else {
                ws.rotate(true);
            }
        }

        if (player.bribesRemaining == 0) {
            return;
        }

        //if we will do more damage than them,
        //rotate the weather and increase the intensity!
        if (maxDamage > 0) {
            int amountToInc = maxPointsToChangeWeather;
            amountToInc = Math.min(stationStack.size(), amountToInc);
            amountToInc = Math.min(10-game.currentForecast.intensity, amountToInc);
            amountToInc = Math.min(player.bribesRemaining, amountToInc);
            for (int i = 0; i < amountToInc; i++) {
                WeatherStation ws = stationStack.pop();
                ws.intensify();
            }
        } else {
            //Fuck! this is doing more damage to us
            //change the wind to minimize the pain
            int amountToDec = maxPointsToChangeWeather;
            amountToDec = Math.min(stationStack.size(), amountToDec);
            amountToDec = Math.min(game.currentForecast.intensity, amountToDec);
            amountToDec = Math.min(player.bribesRemaining, amountToDec);
            for (int i = 0; i < amountToDec; i++) {
                WeatherStation ws = stationStack.pop();
                ws.intensify(true);
            }
        }
    }


    public Pair<Integer,Integer> getFriendlyEnemyDamage(WeatherStationUtilities.WeatherDirection direction) {
        List<WeatherStationUtilities.FireAdded> fireAdded = weather.getFireAddedForDirection(direction);
        int friendlyDmg = evaluateFriendlyFire(fireAdded);
        int enemyDmg = evaluateEnemyFire(fireAdded);
        return new Pair<>(friendlyDmg,enemyDmg);
    }

    /**
     * Fire added to our buildings.  This is bad D:
     * @param fireSpreadByWind
     * @return
     */
    public int evaluateFriendlyFire(List<WeatherStationUtilities.FireAdded> fireSpreadByWind) {
        int damage = 0;

        for (WeatherStationUtilities.FireAdded fire : fireSpreadByWind) {
            if (fire.building.owner == player) {
                int countDmg = (fire.building.isHeadquarters ? FRIENDLY_HQ_DAMAGE_WEIGHT : 1)*fire.fireAdded;
                damage += countDmg;
            }
        }

        return damage;
    }

    /**
     * Fire added to enemy buildings.  This is good :D
     * @param fireSpreadByWind
     * @return
     */
    public int evaluateEnemyFire(List<WeatherStationUtilities.FireAdded> fireSpreadByWind) {
        int damage = 0;

        for (WeatherStationUtilities.FireAdded fire : fireSpreadByWind) {
            if (fire.building.owner != player) {
                int countDmg = (fire.building.isHeadquarters ? ENEMY_HQ_DAMAGE_WEIGHT : 1)*fire.fireAdded;
                damage += countDmg;
            }
        }

        return damage;
    }
}

package games.anarchy.Strategy;

import games.anarchy.*;
import games.anarchy.Strategy.Building.EnemyHeadquartersUtilities;
import games.anarchy.Strategy.Building.PoliceDepartmentUtilities;
import games.anarchy.Strategy.Building.WeatherStationUtilities;
import games.anarchy.Strategy.Heuristic.ChosenStrategy;
import games.anarchy.Strategy.Heuristic.LastGasp;
import games.anarchy.Strategy.Heuristic.WeatherDefense;

/**
 * Created by squeaky on 11/14/15.
 */
public class HeuristicSelector {
    private EnemyHeadquartersUtilities enemyHeadquartersUtilities;
    private PoliceDepartmentUtilities policeDepartmentUtilities;
    private WeatherStationUtilities weatherStationUtilities;

    public HeuristicSelector(Player player, Game game) {
        enemyHeadquartersUtilities = new EnemyHeadquartersUtilities(player.otherPlayer.headquarters);
        policeDepartmentUtilities = new PoliceDepartmentUtilities(player.policeDepartments);
        weatherStationUtilities = new WeatherStationUtilities(player, game);
    }

    public ChosenStrategy selectStrategyForThisTurn(Warehouse myHeadquarters, Warehouse enemyHeadquarters) {
        //SCORCHED EARTH FINISHER: Can the opponent's HQ be killed this turn by any possible means?
        if (enemyHeadquarters.health < enemyHeadquarters.exposure ||
                canFireKillEnemyHeadquarters()) {

        }

        //LAST GASP: Are we about to die an un-preventable death?  FORCE A TIE!
        if (myHeadquarters.health < myHeadquarters.fire) { //We WILL lose next turn.  Do EVERYTHING WE CAN to hurt the opponent's HQ and force a tie
            return new LastGasp();
        }

        //SURVIVOR: Are we about to die a preventable death? Stay alive!

        //FIRE: Light stuff on fire.

        //POLICE SMITE: Destroy buildings that can be killed by raid.

        return new WeatherDefense();
    }






    private boolean canFireKillEnemyHeadquarters() {
        Warehouse enemyHeadquarters = enemyHeadquartersUtilities.getEnemyHeadquarters();
        int healthAfterRaid = 300;
        Forecast upcomingForecast = weatherStationUtilities.getNextWeather();
        if(policeDepartmentUtilities.atLeastOnePoliceStationStandings()) {
            healthAfterRaid = enemyHeadquarters.health - enemyHeadquarters.exposure;
        } else {
            healthAfterRaid = enemyHeadquarters.health;
        }

        for(Building building : enemyHeadquartersUtilities.getEnemyHeadquartersNeighbors()) {
            //TODO:
//            if(healthAfterRaid < building.fire && actionsRequiredToChangeToDesiredWeather() < 9) {
//
//            }
        }

        return false;
    }

    private int actionsRequiredToChangeToDesiredWeather(Forecast actualNextForecast, Forecast desiredNextForecast) {
        return 0; //TODO
    }
}

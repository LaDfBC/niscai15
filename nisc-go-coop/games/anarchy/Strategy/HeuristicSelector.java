package games.anarchy.Strategy;

import games.anarchy.*;
import games.anarchy.Strategy.Building.EnemyHeadquartersUtilities;
import games.anarchy.Strategy.Building.PoliceDepartmentUtilities;
import games.anarchy.Strategy.Building.WeatherStationUtilities;
import games.anarchy.Strategy.Enum.SourceOfDamage;
import games.anarchy.Strategy.Heuristic.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by squeaky on 11/14/15.
 */
public class HeuristicSelector {
    private EnemyHeadquartersUtilities enemyHeadquartersUtilities;
    private PoliceDepartmentUtilities policeDepartmentUtilities;
    private WeatherStationUtilities weatherStationUtilities;
    private Player player;
    private Game game;

    public HeuristicSelector(Player player, Game game) {
        enemyHeadquartersUtilities = new EnemyHeadquartersUtilities(player.otherPlayer.headquarters, game);
        policeDepartmentUtilities = new PoliceDepartmentUtilities(player.policeDepartments);
        weatherStationUtilities = new WeatherStationUtilities(player, game);
        this.player = player;
        this.game = game;
    }



    public ChosenStrategy selectStrategyForThisTurn(Warehouse myHeadquarters, Warehouse enemyHeadquarters) {
        Map<SourceOfDamage, Pair<Integer, Integer>> waysToDamage = enemyHeadquartersUtilities.getWaysToDamageEnemyHeadquarters(policeDepartmentUtilities, weatherStationUtilities);

        //SCORCHED EARTH FINISHER: Can the opponent's HQ be killed this turn by any possible means?
        List<SourceOfDamage> killzone = calculateAbilityToKill(waysToDamage, enemyHeadquartersUtilities.getEnemyHeadquarters().health, player.bribesRemaining);
        if(killzone != null) {
            return new ScorchedEarthFinisher(killzone, waysToDamage, player, enemyHeadquartersUtilities, weatherStationUtilities);
        } else {

            //LAST GASP: Are we about to die an un-preventable death?  FORCE A TIE!
            if (myHeadquarters.health < myHeadquarters.fire) { //We WILL lose next turn.  Do EVERYTHING WE CAN to hurt the opponent's HQ and force a tie
                return new LastGasp();
            }

            //SURVIVOR: Are we about to die a preventable death? Stay alive!

            //FIRE: Light stuff on fire.

            //POLICE SMITE: Destroy buildings that can be killed by raid.

            return new WeatherDefense();
        }
    }

    private List<SourceOfDamage> calculateAbilityToKill(Map<SourceOfDamage, Pair<Integer, Integer>> waysToDamage, int health, int bribes) {
        List<SourceOfDamage> possibleWinners;
        int ONE = 1;
        int TWO = 2;
        int FOUR = 4;
        int EIGHT = 8;

        for(int i = 1; i < (Math.pow(2, SourceOfDamage.values().length)); i++) {
            possibleWinners = new ArrayList<>();
            if((i & ONE) > 0) {
                possibleWinners.add(SourceOfDamage.POLICE); //POLICE
            }
            if((i & TWO) > 0) {
                possibleWinners.add(SourceOfDamage.EXISTING_PLUS_WIND); //EXISTING + WIND
            }
            if((i & FOUR) > 0) {
                possibleWinners.add(SourceOfDamage.IGNITE_PLUS_WIND); //IGNITE + WIND
            }
            if((i & EIGHT) > 0) {
                possibleWinners.add(SourceOfDamage.FIRE_ALONE); //FIRE ALONE
            }

            int tempHealth = health;
            int tempBribes = bribes;
            for(SourceOfDamage used : possibleWinners) {
                if(waysToDamage.get(used) != null) {
                    tempHealth -= waysToDamage.get(used).getKey();
                    tempBribes -= waysToDamage.get(used).getValue();
                }
            }

            if(tempHealth <= 0 && tempBribes >= 0) {
                return possibleWinners;
            }
        }

        return null;
    }

}

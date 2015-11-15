package games.anarchy.Strategy.Heuristic;

import games.anarchy.Building;
import games.anarchy.Game;
import games.anarchy.Player;
import games.anarchy.Strategy.Building.WarehouseUtilities;
import games.anarchy.Strategy.Building.WeatherStationUtilities;

/**
 * Created by squeaky on 11/15/15.
 */
public class FriendlyHeadquartersStrategy {

    Player player;
    Game game;
    int distanceFromEnemyHq;
    Integer enemyRaidThreshold;
    WeatherStationUtilities weatherStationUtilities;
    WarehouseUtilities warehouseUtilities;

    private Building FRIENDLY_EAST_OF_HQ = player.headquarters.buildingEast;
    private Building FRIENDLY_NORTH_OF_HQ = player.headquarters.buildingNorth;
    private Building FRIENDLY_SOUTH_OF_HQ = player.headquarters.buildingSouth;
    private Building FRIENDLY_WEST_OF_HQ = player.headquarters.buildingWest;

    private Building ENEMY_EAST_OF_HQ =  player.otherPlayer.headquarters.buildingEast;
    private Building ENEMY_NORTH_OF_HQ = player.otherPlayer.headquarters.buildingNorth;
    private Building ENEMY_SOUTH_OF_HQ = player.otherPlayer.headquarters.buildingSouth;
    private Building ENEMY_WEST_OF_HQ =  player.otherPlayer.headquarters.buildingWest;


    public FriendlyHeadquartersStrategy(Player player, Game game) {
        this.player = player;
        this.game = game;
        this.warehouseUtilities = new WarehouseUtilities(player, game);
        this.weatherStationUtilities = new WeatherStationUtilities(player, game);
        //todo: this should look at building nearest to warehouse, but close enough
        this.distanceFromEnemyHq = warehouseUtilities.exposureAddedToIgnite(player.headquarters, player.otherPlayer.headquarters);
        this.enemyRaidThreshold = Integer.MAX_VALUE;
    }

    //returns null if no targets exist
    public Building getClosestBuildingAdjacentToEnemyHqIfExposureIsLessThan(int maxExposure){
        if(maxExposure < distanceFromEnemyHq) {
            Building buildingsAdjacentToTargetOppositeOfWind = warehouseUtilities.getBuildingAdjacentToTargetOppositeOfWind(player.otherPlayer.headquarters, weatherStationUtilities.getDirectionOfWindNextTurn());
            return buildingsAdjacentToTargetOppositeOfWind;
        }
        return null;
    }

    /**
     * Heuristic evaluation:
     * (1/Health) * 1000
     * +
     * (Fire intensity on cardinal connected buildings) * 100
     * +
     * (Fire intensity on off-by-two connected buildings) * 20
     */
    public int evaluateFriendlyHeadquarters() {

        int heuristic = 0;

        //(1/Health) * 1000
        heuristic += (1.0 / (float)player.headquarters.health) * 1000;

        //Fire intensity on cardinally connected buildings * 100
        heuristic += ((FRIENDLY_EAST_OF_HQ.fire + FRIENDLY_NORTH_OF_HQ.fire +
                FRIENDLY_SOUTH_OF_HQ.fire + FRIENDLY_WEST_OF_HQ.fire) * 100);

        //Fire intensity of off-by-two buildings
        heuristic += ((FRIENDLY_EAST_OF_HQ.buildingEast.fire + FRIENDLY_EAST_OF_HQ.buildingNorth.fire + FRIENDLY_EAST_OF_HQ.buildingSouth.fire) * 20);
        heuristic += ((FRIENDLY_WEST_OF_HQ.buildingNorth.fire + FRIENDLY_WEST_OF_HQ.buildingWest.fire + FRIENDLY_WEST_OF_HQ.buildingSouth.fire) * 20);
        heuristic += ((FRIENDLY_SOUTH_OF_HQ.buildingSouth.fire) * 20);
        heuristic += ((FRIENDLY_NORTH_OF_HQ.buildingNorth.fire) * 20);

        return heuristic;
    }

}
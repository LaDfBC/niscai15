package games.anarchy.Strategy.Heuristic;

import games.anarchy.Building;
import games.anarchy.Game;
import games.anarchy.Player;

/**
 * Created by squeaky on 11/15/15.
 */
public class FriendlyHeadquartersDefense {

    Player player;
    Game game;

    private Building EAST_OF_HQ = player.headquarters.buildingEast;
    private Building NORTH_OF_HQ = player.headquarters.buildingNorth;
    private Building SOUTH_OF_HQ = player.headquarters.buildingSouth;
    private Building WEST_OF_HQ = player.headquarters.buildingWest;

    public FriendlyHeadquartersDefense(Player player, Game game) {
        this.player = player;
        this.game = game;
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
        heuristic += ((EAST_OF_HQ.fire + NORTH_OF_HQ.fire +
                SOUTH_OF_HQ.fire + WEST_OF_HQ.fire) * 100);

        //Fire intensity of off-by-two buildings
        heuristic += ((EAST_OF_HQ.buildingEast.fire + EAST_OF_HQ.buildingNorth.fire + EAST_OF_HQ.buildingSouth.fire) * 20);
        heuristic += ((WEST_OF_HQ.buildingNorth.fire + WEST_OF_HQ.buildingWest.fire + WEST_OF_HQ.buildingSouth.fire) * 20);
        heuristic += ((SOUTH_OF_HQ.buildingSouth.fire) * 20);
        heuristic += ((NORTH_OF_HQ.buildingNorth.fire) * 20);

        return heuristic;
    }

}

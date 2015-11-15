/** 
 * Generated by Creer at 03:01PM on November 10, 2015 UTC, git hash: '1b69e788060071d644dd7b8745dca107577844e1'
 * A basic building. It does nothing besides burn down. Other Buildings inherit from this class.
 */
package games.anarchy;

import java.util.*;

import games.anarchy.Strategy.Building.WeatherStationUtilities;
import org.json.JSONObject;

import joueur.Client;
import joueur.BaseGame;
import joueur.BaseGameObject;
// <<-- Creer-Merge: imports -->> - Code you add between this comment and the end comment will be preserved between Creer re-runs.
// you can add addtional import(s) here
// <<-- /Creer-Merge: imports -->>
@SuppressWarnings("unused")

/**
 * A basic building. It does nothing besides burn down. Other Buildings inherit from this class.
 */
public class Building extends GameObject {
    /**
     * when true this building has already been bribed this turn and cannot be bribed again this turn.
     */
    public boolean bribed;

    /**
     * The Building directly to the east of this building, or null if not present.
     */
    public Building buildingEast;

    /**
     * The Building directly to the north of this building, or null if not present.
     */
    public Building buildingNorth;

    /**
     * The Building directly to the south of this building, or null if not present.
     */
    public Building buildingSouth;

    /**
     * The Building directly to the west of this building, or null if not present.
     */
    public Building buildingWest;

    /**
     * How much fire is currently burning the building, and thus how much damage it will take at the end of its owner's turn. 0 means no fire.
     */
    public int fire;

    /**
     * How much health this building currently has. When this reaches 0 the Building has been burned down
     */
    public int health;

    /**
     * true if this is the Headquarters of the owning player, false otherwise. Burning this down wins the game for the other Player.
     */
    public boolean isHeadquarters;

    /**
     * The player that owns this building. If it burns down (health reaches 0) that player gets an additional bribe(s).
     */
    public Player owner;

    /**
     * The location of the Building along the x-axis
     */
    public int x;

    /**
     * The location of the Building along the y-axis
     */
    public int y;


    // <<-- Creer-Merge: fields -->> - Code you add between this comment and the end comment will be preserved between Creer re-runs.
    // you can add addtional field(s) here. None of them will be tracked or updated by the server.
    // <<-- /Creer-Merge: fields -->>


    /**
     * Creates a new instance of Building. Used during game initialization, do not call directly.
     */
    public Building() {
        super();
    }

    // <<-- Creer-Merge: methods -->> - Code you add between this comment and the end comment will be preserved between Creer re-runs.
    // you can add addtional method(s) here.

    public List<Building> getBuildingsWithinDistance(int maxDistance) {
        List<Building> buildings = new ArrayList<>();
        if (maxDistance <= 0) {
            return buildings;
        }

        //perform a BFS
        Queue<Building> buildingQueue = new LinkedList<>();
        Map<Building,Integer> distanceMap = new HashMap<>();
        buildingQueue.add(this);
        distanceMap.put(this, 0);

        while (!buildingQueue.isEmpty()) {
            Building b = buildingQueue.poll();
            Integer dist = distanceMap.get(b);

            if (dist < maxDistance) {
                addDistNotNull(b.buildingEast, dist, buildingQueue, distanceMap,buildings);
                addDistNotNull(b.buildingNorth, dist, buildingQueue, distanceMap,buildings);
                addDistNotNull(b.buildingSouth, dist, buildingQueue, distanceMap,buildings);
                addDistNotNull(b.buildingWest, dist, buildingQueue, distanceMap,buildings);
            }
        }

        return buildings;
    }


    public Building getAdjacentBuilding(WeatherStationUtilities.CardinalDirection direction) {
        switch (direction) {
            case north:
                return buildingNorth;
            case east:
                return buildingEast;
            case south:
                return buildingSouth;
            case west:
                return buildingWest;
        }
        return null;
    }

    private void addDistNotNull(Building b, Integer fromDist, Queue<Building> bQueue, Map<Building, Integer> distMap, List<Building> buildingList) {
        if (b == null || distMap.containsKey(b)) {
            return;
        }
        distMap.put(b, fromDist + 1);
        buildingList.add(b);
        bQueue.add(b);
    }

    // <<-- /Creer-Merge: methods -->>
}

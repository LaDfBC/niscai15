/** 
 * Generated by Creer at 03:01PM on November 10, 2015 UTC, git hash: '1b69e788060071d644dd7b8745dca107577844e1'
 * This is where you build your AI for the Anarchy game.
 */
package games.anarchy;

import java.util.*;

import games.anarchy.Strategy.Building.BuildingUtilities;
import games.anarchy.Strategy.Building.EnemyHeadquartersUtilities;
import games.anarchy.Strategy.Building.WarehouseUtilities;
import games.anarchy.Strategy.Building.WeatherStationUtilities;
import games.anarchy.Strategy.Heuristic.WeatherOffsense;
import joueur.BaseAI;
// <<-- Creer-Merge: imports -->> - Code you add between this comment and the end comment will be preserved between Creer re-runs.
// you can add addtional import(s) here
// <<-- /Creer-Merge: imports -->>
@SuppressWarnings("unused")

/**
 * This is where you build your AI for the Anarchy game.
 */
public class AI extends BaseAI {
    /**
     * This is the Game object itself, it contains all the information about the current game
     */
    public Game game;

    /**
     * This is your AI's player. This AI class is not a player, but it should command this Player.
     */
    public Player player;



    // <<-- Creer-Merge: fields -->> - Code you add between this comment and the end comment will be preserved between Creer re-runs.
    public Warehouse myHeadquarters;
    public Warehouse enemyHeadquarters;
    List<Warehouse> myAttackers;
    List<Warehouse> enemyAttackers;

    // <<-- /Creer-Merge: fields -->>


    /**
     * This returns your AI's name to the game server. Just replace the string.
     * @return string of you AI's name
     */
    public String getName() {
        // <<-- Creer-Merge: get-name -->> - Code you add between this comment and the end comment will be preserved between Creer re-runs.
        return "NISC - GO COOP - JOE FIDDLE"; // REPLACE THIS WITH YOUR TEAM NAME!
        // <<-- /Creer-Merge: get-name -->>
    }

    /**
     * This is automatically called when the game first starts, once the Game object and all GameObjects have been initialized, but before any players do anything.
     * This is a good place to initialize any variables you add to your AI, or start tracking game objects.
     */
    public void start() {
        // <<-- Creer-Merge: start -->> - Code you add between this comment and the end comment will be preserved between Creer re-runs.
        super.start();
        for(Warehouse warehouse : player.warehouses) {
            if(warehouse.isHeadquarters) {
                myHeadquarters = warehouse;
                break;
            }
        }

        for(Warehouse warehouse : player.otherPlayer.warehouses) {
            if(warehouse.isHeadquarters) {
                enemyHeadquarters = warehouse;
                break;
            }
        }
        // <<-- /Creer-Merge: start -->>
    }

    /**
     * This is automatically called every time the game (or anything in it) updates.
     * If a function you call triggers an update this will be called before that function returns.
     */
    public void gameUpdated() {
        // <<-- Creer-Merge: game-updated -->> - Code you add between this comment and the end comment will be preserved between Creer re-runs.
        super.gameUpdated();
        // <<-- /Creer-Merge: game-updated -->>
    }

    /**
     * This is automatically called when the game ends.
     * You can do any cleanup of you AI here, or do custom logging. After this function returns the application will close.
     * @param  won  true if your player won, false otherwise
     * @param  reason">a string explaining why you won or lost
     */
    public void ended(boolean won, String reason) {
        // <<-- Creer-Merge: ended -->> - Code you add between this comment and the end comment will be preserved between Creer re-runs.
        super.ended(won, reason);
        // <<-- /Creer-Merge: ended -->>
    }

    public void weatherFiddle() {

    }

    public void joeFiddle(){
        EnemyHeadquartersUtilities enemyHeadquartersUtilities = new EnemyHeadquartersUtilities(enemyHeadquarters, game);
        Random random = new Random();
        int numRandomTargets = random.nextInt(10);
        List<Building> targets = new ArrayList<>();
        if(player.otherPlayer.buildings.size() <= numRandomTargets){
            targets = player.otherPlayer.buildings;
        }else {

            while (targets.size() < numRandomTargets) {
                targets.add(player.otherPlayer.buildings.get(random.nextInt(player.otherPlayer.buildings.size())));
            }
        }
        if(targets.isEmpty()){
            return;
        }else {
            igniteTargetsUsingClosestWarehouses(targets, myAttackers, player.bribesRemaining);
        }

    }


    public void jeffWeather() {
        WeatherStationUtilities weather = new WeatherStationUtilities(player,game);
        WeatherOffsense weatherAttack = new WeatherOffsense(player,weather,game);
        //ignite warehouses closest to their HQ
        igniteBuildingsCloseToHQ(6, 7);

        //delegate up to 4 turns for the weather
        weatherAttack.minMaxDamage(3);
        while (player.bribesRemaining > 0) {
            igniteBuildingsCloseToHQ(6, 7);
        }
    }

    public void igniteBuildingsCloseToHQ(int numBuildings, int maxDistance) {
        //ignite warehouses closest to their HQ
        List<Building> closeBuildings = enemyHeadquarters.getBuildingsWithinDistance(maxDistance);

        Queue<Building> closeEnemies = new ArrayDeque<>();
        for (Building b : closeBuildings) {
            if (b.owner != player) {
                closeEnemies.add(b);
            }
        }

        Set<Warehouse> bribeableWarehouses = new HashSet<>();
        bribeableWarehouses.addAll(WarehouseUtilities.getHealthyAndUnbribed(player.warehouses));

        //ignite
        int numTurns = numBuildings;
        while (numTurns > 0) {
            if (!closeEnemies.isEmpty()) {
                Building buildingToFire = closeEnemies.poll();
                attackUsingNearestWarehouse(buildingToFire);
            }
            numTurns--;
        }
    }

    public Boolean attackUsingNearestWarehouse(Building target){
        Warehouse myAttacker = WarehouseUtilities.getClosestWarehouse(target, myAttackers);
        if(myAttacker != null){
            myAttacker.ignite(target);
            myAttackers.remove(myAttacker);
            return true;
        }
        return false;
    }
    /**
     * This is called every time the AI is asked to respond with a command during their turn
     *
     * @return represents if you want to end your turn. true means end the turn, false means to keep your turn going and re-call runTurn()
     */
    public boolean runTurn() {
        myAttackers = WarehouseUtilities.getHealthyAndUnbribed(player.warehouses);
        enemyAttackers =  WarehouseUtilities.getHealthyAndUnbribed(player.otherPlayer.warehouses);
//        joeFiddle();
        jeffWeather();


//        // <<-- Creer-Merge: runTurn -->> - Code you add between this comment and the end comment will be preserved between Creer re-runs.
//        // Put your game logic here for runTurn
//
//        //Get my first warehouse
//        Warehouse warehouse = player.warehouses.get(0);
//        if(canBeBribed(warehouse)) {
//            //ignite the first enemy building unless it's a headquarters
//            Building target = player.otherPlayer.buildings.get(0);
//            if(!target.isHeadquarters) {
//                warehouse.ignite(target);
//            }
//        }
//
//        //Get my frst fire department
//        FireDepartment fireDept = player.fireDepartments.get(0);
//        if(canBeBribed(fireDept)) {
//            Building target = player.otherPlayer.buildings.get(0);
//            if(!target.isHeadquarters) {
//                fireDept.extinguish(target);
//            }
//        }
//
//        //Get my first police station
//        PoliceDepartment police = player.policeDepartments.get(0);
//        if(canBeBribed(police)) {
//            //pick an enemy warehouse and raid it
//            Warehouse target = player.otherPlayer.warehouses.get(0);
//            //only raid if it is alive
//            if(target.health > 0) {
//                police.raid(target);
//            }
//        }
//
//        //get first weather station
//        WeatherStation intensifier = player.weatherStations.get(0);
//        if(canBeBribed(intensifier)) {
//
//            if(game.nextForecast.intensity < game.maxForecastIntensity) {
//                //only increase if intensity of weather is currently less than max
//                intensifier.intensify();
//            } else {
//                //otherwise pass true to decrease
//                intensifier.intensify(true);
//            }
//        }
//
//        //get second weather station
//        WeatherStation rotater = player.weatherStations.get(1);
//        if(canBeBribed(rotater)) {
//            //rotate weather clockwise (pass false to go counterclockwise)
//            rotater.rotate();
//        }

        return true;
        // <<-- /Creer-Merge: runTurn -->>
    }

    public void igniteTargetsUsingClosestWarehouses(List<Building> targets, List<Warehouse> myWarehouses, int bribesToSpend){
        Map<Warehouse, Building> targetsForWarehouses = WarehouseUtilities.getTargetsForWarehouses(targets, myWarehouses, bribesToSpend);
        for(Map.Entry<Warehouse, Building> entry : targetsForWarehouses.entrySet()){
            entry.getKey().ignite(entry.getValue());
        }
    }
    // <<-- Creer-Merge: methods -->> - Code you add between this comment and the end comment will be preserved between Creer re-runs.
    // you can add additional methods here for your AI to call

    //add your own methods to the ai!
    public boolean canBeBribed(Building building) {
        return (building.health > 0) && !building.bribed && (building.owner == player);
    }
    // <<-- /Creer-Merge: methods -->>
}

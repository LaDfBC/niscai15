/**
 * Generated by Creer at 03:01PM on November 10, 2015 UTC, git hash: '1b69e788060071d644dd7b8745dca107577844e1'
 * This is where you build your AI for the Anarchy game.
 */
package games.anarchy;

import java.util.*;

import games.anarchy.Strategy.Building.*;
import games.anarchy.Strategy.Heuristic.FriendlyHeadquartersStrategy;
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
    WarehouseUtilities warehouseUtilities;
    WeatherStationUtilities weatherStationUtilities;
    EnemyHeadquartersUtilities enemyHeadquartersUtilities;
    FriendlyHeadquartersStrategy friendlyHqStrat;
    PoliceDepartmentUtilities policeDepartmentUtilities;


    // <<-- /Creer-Merge: fields -->>


    /**
     * This returns your AI's name to the game server. Just replace the string.
     * @return string of you AI's name
     */
    public String getName() {
        // <<-- Creer-Merge: get-name -->> - Code you add between this comment and the end comment will be preserved between Creer re-runs.
        return "NISC - GO COOP - JOE FIDDLE WITH WEATHER AND POLICE"; // REPLACE THIS WITH YOUR TEAM NAME!
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
        warehouseUtilities = new WarehouseUtilities(player, game);
        weatherStationUtilities = new WeatherStationUtilities(player, game);
        enemyHeadquartersUtilities = new EnemyHeadquartersUtilities(enemyHeadquarters, game);
        policeDepartmentUtilities = new PoliceDepartmentUtilities(player.policeDepartments, enemyHeadquartersUtilities);
        friendlyHqStrat = new FriendlyHeadquartersStrategy(player,game);

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

    public Map<Integer, Warehouse> sortMapByKey(Map<Integer, Warehouse> toSort){
    }

    public void joeFiddle(){
        System.out.println("TURN: " + game.currentTurn);

        Stack<Warehouse> killableWarehouses = policeDepartmentUtilities.canKill(player.otherPlayer.warehouses);

        for(PoliceDepartment policeDepartment : player.policeDepartments){
            if(player.bribesRemaining < 1){
                return;
            }
            if(!policeDepartment.bribed && policeDepartment.health > 0){
                if(killableWarehouses != null && !killableWarehouses.isEmpty()) {
                    System.out.println("  raiding and killing");
                    Warehouse targetWarehouse = killableWarehouses.pop();
                    policeDepartment.raid(targetWarehouse);
                }

            }
        }

        Map<Integer, Warehouse> dmgRequiredToKill = policeDepartmentUtilities.fireRequiredToBurnAfterRaid(player.otherPlayer.warehouses);
        TreeSet<Integer> treeSet = new TreeSet<>(dmgRequiredToKill.keySet());
        Iterator<Integer> keyIter = treeSet.iterator();
        while(keyIter.hasNext()){
            if(player.bribesRemaining < 1){
                return;
            }
            Warehouse target = dmgRequiredToKill.get(keyIter.next());
            for(Warehouse attacker : player.warehouses){
                if(attacker.health > 0 && !attacker.bribed){
                    System.out.println(" raiding to weaken");
                    attacker.ignite(target);
                }
            }
        }

        Building target = friendlyHqStrat.getClosestBuildingAdjacentToEnemyHqIfExposureIsLessThan(10000);
        if(target != null){
            player.headquarters.ignite(target);
            WeatherStationUtilities.CardinalDirection directionOfWindNextTurn = weatherStationUtilities.getDirectionOfWindNextTurn();
            int intensity = game.nextForecast.intensity;

            for(WeatherStation weatherStation : player.weatherStations){
                if(player.bribesRemaining > 0 && intensity < 10){
                    if(weatherStation.health > 0) {
                        System.out.println("  intensify");
                        weatherStation.intensify();
                        intensity++;
                    }
                }else{
                    return;
                }
            }
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
        myAttackers = warehouseUtilities.getHealthyAndUnbribed(player.warehouses);
        enemyAttackers =  warehouseUtilities.getHealthyAndUnbribed(player.otherPlayer.warehouses);
        joeFiddle();
//        jeffWeather2();
//        jeffWeather();
//        georgeFiddle();

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

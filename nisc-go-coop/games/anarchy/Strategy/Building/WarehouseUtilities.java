package games.anarchy.Strategy.Building;

import games.anarchy.Building;
import games.anarchy.Player;
import games.anarchy.Warehouse;

import java.util.*;

/**
 * Created by squeaky on 11/14/15.
 */
public class WarehouseUtilities {

    private Player player;

    public WarehouseUtilities(Player player) {
        this.player = player;
    }

    public int exposureAddedToIgnite(Warehouse ignitingWarehouse, Building buildingToBurn) {
        return Math.abs(ignitingWarehouse.x - buildingToBurn.x) + Math.abs(ignitingWarehouse.y - buildingToBurn.y);
    }

    public List<Warehouse> getHealthyAndUnbribed(List<Warehouse> buildings){
        return getUnbribed(getHealthy(buildings));
    }

    public List<Warehouse> getHealthy(List<Warehouse> buildings){
        List<Warehouse> ret = new ArrayList<>();
        for(Warehouse building : buildings){
            if(building.health > 0){
                ret.add(building);
            }
        }
        return ret;
    }

    public List<Warehouse> getUnbribed(List<Warehouse> buildings){
        List<Warehouse> ret = new ArrayList<>();
        for(Warehouse building : buildings){
            if(!building.bribed){
                ret.add(building);
            }
        }
        return ret;
    }

    /*make sure you check for null when you use this!*/
    public Warehouse getClosestWarehouse(Building building, Collection<Warehouse> warehouses){
        if(building == null || warehouses == null || warehouses.isEmpty()){
            return null;
        }
        Warehouse closest = (Warehouse)warehouses.toArray()[0];
        if(closest.isHeadquarters && warehouses.size() > 1){
            closest = (Warehouse)warehouses.toArray()[1];
        }
        for(Warehouse warehouse : warehouses){
            if(!warehouse.isHeadquarters && warehouse.health > 0 && exposureAddedToIgnite(warehouse, building) < exposureAddedToIgnite(closest, building)){
                closest = warehouse;
            }
        }
        return closest;
    }


    public Map<Warehouse, Building> getTargetsForWarehouses(List<Building> possibleTargets, List<Warehouse> myWarehouses, int bribesToSpend){
        Map<Warehouse, Building> targetsForWarehouses = new HashMap<>();
        if(myWarehouses == null || possibleTargets == null || bribesToSpend == 0){
            return new HashMap<>();
        }
        int bribesSpent = 0;
        while(!myWarehouses.isEmpty()) { //while we still have attackers
            for (Building target : possibleTargets) { //pick a target
                if(bribesSpent > bribesToSpend || myWarehouses.isEmpty()){
                    return targetsForWarehouses; //we've spent our max so return
                }
                else{ //we have warehouses and bribes available
                    Warehouse closestWarehouse = getClosestWarehouse(target, myWarehouses);//get the closest warehouse
                    targetsForWarehouses.put(closestWarehouse, target); //add it to the target list
                    bribesSpent++; // spent a bribe
                }
            }
        }
        return targetsForWarehouses;
    }

    public Set<Warehouse> getBribeableWarehouses() {
        Set<Warehouse> bribeableWarehouses = new HashSet<>();
        for (Warehouse w : player.warehouses) {
            if (w.health > 0 && !w.bribed) {
                bribeableWarehouses.add(w);
            }
        }

        return bribeableWarehouses;
    }
}

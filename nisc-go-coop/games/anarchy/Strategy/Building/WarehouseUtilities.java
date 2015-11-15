package games.anarchy.Strategy.Building;

import games.anarchy.Building;
import games.anarchy.Warehouse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by squeaky on 11/14/15.
 */
public class WarehouseUtilities {
    public static int exposureAddedToIgnite(Warehouse ignitingWarehouse, Building buildingToBurn) {
        return Math.abs(ignitingWarehouse.x - buildingToBurn.x) + Math.abs(ignitingWarehouse.y - buildingToBurn.y);
    }

    public static List<Warehouse> getHealthyAndUnbribed(List<Warehouse> buildings){
        return getUnbribed(getHealthy(buildings));
    }

    public static List<Warehouse> getHealthy(List<Warehouse> buildings){
        List<Warehouse> ret = new ArrayList<>();
        for(Warehouse building : buildings){
            if(building.health > 0){
                ret.add(building);
            }
        }
        return ret;
    }

    public static List<Warehouse> getUnbribed(List<Warehouse> buildings){
        List<Warehouse> ret = new ArrayList<>();
        for(Warehouse building : buildings){
            if(!building.bribed){
                ret.add(building);
            }
        }
        return ret;
    }

    /*make sure you check for null when you use this!*/
    public static Warehouse getClosestWarehouse(Building building, Collection<Warehouse> warehouses){
        if(building == null || warehouses == null || warehouses.isEmpty()){
            return null;
        }
        Warehouse closest = (Warehouse)warehouses.toArray()[0];
        for(Warehouse warehouse : warehouses){
            if(warehouse.health > 0 && exposureAddedToIgnite(warehouse, building) < exposureAddedToIgnite(closest, building)){
                closest = warehouse;
            }
        }
        return closest;
    }


    public static Map<Warehouse, Building> getTargetsForWarehouses(List<Building> possibleTargets, List<Warehouse> myWarehouses, int bribesToSpend){
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
}

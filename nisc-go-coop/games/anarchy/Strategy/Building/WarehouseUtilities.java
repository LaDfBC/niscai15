package games.anarchy.Strategy.Building;

import games.anarchy.Building;
import games.anarchy.Warehouse;

import java.util.ArrayList;
import java.util.List;

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
    public static Warehouse getClosestWarehouse(Building building, List<Warehouse> warehouses){
        if(building == null || warehouses == null || warehouses.isEmpty()){
            return null;
        }
        Warehouse closest = warehouses.get(0);
        for(Warehouse warehouse : warehouses){
            if(warehouse.health > 0 && exposureAddedToIgnite(warehouse, building) < exposureAddedToIgnite(closest, building)){
                closest = warehouse;
            }
        }
        return closest;
    }
}

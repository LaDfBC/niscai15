package games.anarchy.Strategy.Building;

import games.anarchy.Building;
import games.anarchy.Warehouse;

import java.util.List;

/**
 * Created by squeaky on 11/14/15.
 */
public class WarehouseUtilities {
    public static int exposureAddedToIgnite(Warehouse ignitingWarehouse, Building buildingToBurn) {
        return Math.abs(ignitingWarehouse.x - buildingToBurn.x) + Math.abs(ignitingWarehouse.y - buildingToBurn.y);
    }

    /*make sure you check for null when you use this!*/
    public static Warehouse getClosestWarehouse(Building building, List<Warehouse> warehouses){
        if(building == null || warehouses == null || warehouses.isEmpty()){
            return null;
        }
        Warehouse closest = warehouses.get(0);
        for(Warehouse warehouse : warehouses){
            if(exposureAddedToIgnite(warehouse, building) < exposureAddedToIgnite(closest, building)){
                closest = warehouse;
            }
        }
        return closest;
    }
}

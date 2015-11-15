package games.anarchy.Strategy.Building;

import com.sun.javafx.beans.annotations.NonNull;
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

    public static Warehouse getClosestWarehouse(@NonNull Building building, @NonNull List<Warehouse> warehouses){
        if(warehouses.isEmpty()){
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

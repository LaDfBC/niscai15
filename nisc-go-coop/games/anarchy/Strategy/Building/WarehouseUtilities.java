package games.anarchy.Strategy.Building;

import games.anarchy.Building;
import games.anarchy.Warehouse;

/**
 * Created by squeaky on 11/14/15.
 */
public class WarehouseUtilities {
    public static int exposureAddedToIgnite(Warehouse ignitingWarehouse, Building buildingToBurn) {
        return Math.abs(ignitingWarehouse.x - buildingToBurn.x) + Math.abs(ignitingWarehouse.y - buildingToBurn.y);
    }
}
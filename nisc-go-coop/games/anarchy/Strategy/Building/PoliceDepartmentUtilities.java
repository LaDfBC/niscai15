package games.anarchy.Strategy.Building;

import games.anarchy.PoliceDepartment;
import games.anarchy.Warehouse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by squeaky on 11/14/15.
 */
public class PoliceDepartmentUtilities {
    List<PoliceDepartment> myPoliceDepartments;

    public PoliceDepartmentUtilities(List<PoliceDepartment> myPoliceDepartments) {
        this.myPoliceDepartments = myPoliceDepartments;
    }

    /**
     * ONLY PASS IN THE LIST OF *ENEMY* BUILDINGS!!
     * @param enemyWarehouses
     * @return
     */
    public List<Warehouse> canKill(List<games.anarchy.Warehouse> enemyWarehouses) {
        List<Warehouse> killableWarehouses = new ArrayList<>();
        for(Warehouse warehouse : enemyWarehouses) {
            if((warehouse.health < (warehouse.exposure + warehouse.fire)) && warehouse.health > 0) {
                killableWarehouses.add(warehouse);
            }
        }

        return killableWarehouses;
    }

    public Map<Integer, Warehouse> fireRequiredToBurnAfterRaid(List<Warehouse> enemyWarehouses) {
        Map<Integer, Warehouse> fireRequiredMap = new HashMap<>();
        for(Warehouse warehouse: enemyWarehouses) {
            if(warehouse.health - warehouse.fire > 0) {
                fireRequiredMap.put(warehouse.health - warehouse.exposure, warehouse);
            }
        }

        return fireRequiredMap;
    }

    public boolean atLeastOnePoliceStationStandings() {
        for(PoliceDepartment policeDepartment : myPoliceDepartments) {
            if(policeDepartment.health > 0) {
                return true;
            }
        }

        return false;
    }


}

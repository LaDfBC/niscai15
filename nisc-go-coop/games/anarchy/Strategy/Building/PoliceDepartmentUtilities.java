package games.anarchy.Strategy.Building;

import games.anarchy.PoliceDepartment;
import games.anarchy.Warehouse;

import java.util.*;

/**
 * Created by squeaky on 11/14/15.
 */
public class PoliceDepartmentUtilities {
    List<PoliceDepartment> myPoliceDepartments;
    EnemyHeadquartersUtilities enemyHeadquartersUtilities;

    public PoliceDepartmentUtilities(List<PoliceDepartment> myPoliceDepartments,
                                     EnemyHeadquartersUtilities enemyHeadquartersUtilities) {
        this.myPoliceDepartments = myPoliceDepartments;
        this.enemyHeadquartersUtilities = enemyHeadquartersUtilities;
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

    public boolean atLeastOnePoliceStationStanding() {
        for(PoliceDepartment policeDepartment : myPoliceDepartments) {
            if(policeDepartment.health > 0) {
                return true;
            }
        }

        return false;
    }

    public boolean isEnemyHeadquartersExposed(EnemyHeadquartersUtilities enemyHeadquartersUtilities) {
        return enemyHeadquartersUtilities.getEnemyHeadquarters().exposure != 0;
    }

    public PriorityQueue<Warehouse> getSortedListOfRaidableWarehouses(List<Warehouse> enemyWarehouses) {
        PriorityQueue<Warehouse> raidableWarehouses = new PriorityQueue<Warehouse>(100,new Comparator<Warehouse>() {
            @Override
            public int compare(Warehouse o1, Warehouse o2) {
                if(o1.exposure > o2.exposure) {
                    return 1;
                } else if (o1.exposure < o2.exposure) {
                    return -1;
                }
                return 0;
            }
        });
        for(Warehouse warehouse : enemyWarehouses) {
            raidableWarehouses.add(warehouse);
        }

        return raidableWarehouses;
    }

}

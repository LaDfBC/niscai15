package games.anarchy.Strategy.Building;

import games.anarchy.Building;
import games.anarchy.FireDepartment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by squeaky on 11/14/15.
 */
public class FireStationUtilities {

    FireStationUtilities() {}

    public Map<Building, Integer> buildingsThatCanBeCompletelyExtinguished(List<Building> allBuildings,
                                                                          int numberMyRemainingFireDepartments) {
        Map<Building, Integer> extinguisherMap = new HashMap<>();
        for(Building building : allBuildings) {
            if(!building.isHeadquarters) { //Fire Stations cannot put out the HQ
                if(building.fire <= (numberMyRemainingFireDepartments * 2)) { //Do not rely on the extra fire decrease.
                    double ceilingVal = Math.ceil((double) building.fire / 2.0D);
                    extinguisherMap.put(building, (int)ceilingVal);
                } else if ((building.health > 1) && (building.fire == (numberMyRemainingFireDepartments * 2) + 1)) {
                    double ceilingVal = Math.ceil((double) building.fire / 2.0D);
                    extinguisherMap.put(building, (int)ceilingVal);
                }
            }
        }

        return extinguisherMap;
    }

    public int getNumberOfMyRemainingFireStations(List<FireDepartment> myFireDepartments) {
        int remaining = 0;
        for(FireDepartment fireDepartment : myFireDepartments) {
            if(fireDepartment.health > 0) {
                remaining++;
            }
        }

        return remaining;
    }

    public Map<Integer, Building> getBribesRequiredToPutOutBuildingsNearHQ() {
        return null;
    }
}

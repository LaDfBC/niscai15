package games.anarchy.Strategy.Building;

import games.anarchy.Building;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Joe Kurtz
 */
public class BuildingUtilities {
    public static List<? extends  Building> getHealthyAndUnbribed(List<? extends Building> buildings){
        return getUnbribed(getHealthy(buildings));
    }

    public static List<? extends Building> getHealthy(List<? extends Building> buildings){
        List<Building> ret = new ArrayList<>();
        for(Building building : buildings){
            if(building.health > 0){
                ret.add(building);
            }
        }
        return ret;
    }

    public static List<? extends  Building> getUnbribed(List<? extends Building> buildings){
        List<Building> ret = new ArrayList<>();
        for(Building building : buildings){
            if(!building.bribed){
                ret.add(building);
            }
        }
        return ret;
    }
}

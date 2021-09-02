package me.suchtpotenzial.claimsystem.claim;

import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;

public class RegionManager {

    private static Map<String, Cuboid> regions = new HashMap<>();

    public static Cuboid getRegion(Location l) {
        for (Map.Entry<String, Cuboid> entry : regions.entrySet()) {
            if (entry.getValue().contains(l)) {
                return entry.getValue();
            }
        }
        return null;
    }

    public static Map<String, Cuboid> getRegions() {
        return regions;
    }

    public static void setRegion(String name, Cuboid cuboid) {
        regions.put(name,cuboid);
    }

    public static Cuboid getRegionByName(String name) {
        return regions.get(name);
    }
}

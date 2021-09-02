package me.suchtpotenzial.claimsystem.claim;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ClaimSerializer {

    public static String serialize(Claim claim) {
        return claim.getOwner() + "/" + claim.getCuboid().toString() + "/" + claim.getTrustetPlayerString();
    }

    //TODO Das wird nie im Leben funktionieren hahahaha
    public static Claim deserialize(String s) {
        String[] data = s.split("/");
        UUID uuid = UUID.fromString(data[0]);
        String[] cuboidRaw = data[1].substring(7,data[1].length()-1).split(";");

        //System.out.println(""+data[1].substring(7,data[1].length()-1));

        Location min = new Location(Bukkit.getWorld(cuboidRaw[0]),Integer.valueOf(cuboidRaw[1]),Integer.valueOf(cuboidRaw[2]),Integer.valueOf(cuboidRaw[3]));
        Location max = new Location(Bukkit.getWorld(cuboidRaw[0]),Integer.valueOf(cuboidRaw[4]),Integer.valueOf(cuboidRaw[5]),Integer.valueOf(cuboidRaw[6]));
        Cuboid cuboid = new Cuboid(min,max);

        List<UUID> trustedUUIDs = new ArrayList<>();
        if (!data[2].equals("no friends")) {
            String[] trustedRaw = data[2].split(",");
            for (String rawUUID : trustedRaw) {
                trustedUUIDs.add(UUID.fromString(rawUUID));
            }
        }

        return new Claim(cuboid,uuid,trustedUUIDs);
    }
}

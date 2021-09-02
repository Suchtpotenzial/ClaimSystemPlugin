package me.suchtpotenzial.claimsystem.claim;

import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;

public class Cuboid {
    private final World world;
    private int minX;
    private int maxX;
    private int minY;
    private int maxY;
    private int minZ;
    private int maxZ;

    public Cuboid(Location loc1, Location loc2) {
        this(loc1.getWorld(), loc1.getBlockX(), loc1.getBlockY(), loc1.getBlockZ(), loc2.getBlockX(), loc2.getBlockY(), loc2.getBlockZ());
    }

    public Cuboid(World world, int x1, int y1, int z1, int x2, int y2, int z2) {
        this.world = world;

        minX = Math.min(x1, x2);
        minY = 0;
        minZ = Math.min(z1, z2);
        maxX = Math.max(x1, x2);
        maxY = 256;
        maxZ = Math.max(z1, z2);
    }

    public World getWorld() {
        return world;
    }

    public int getMinX() {
        return minX;
    }

    public int getMinY() {
        return minY;
    }

    public int getMinZ() {
        return minZ;
    }

    public int getMaxX() {
        return maxX;
    }

    public int getMaxY() {
        return maxY;
    }

    public int getMaxZ() {
        return maxZ;
    }

    public boolean contains(Cuboid cuboid) {
        return cuboid.getWorld().equals(world) &&
                cuboid.getMinX() >= minX && cuboid.getMaxX() <= maxX &&
                cuboid.getMinZ() >= minZ && cuboid.getMaxZ() <= maxZ;
    }

    public boolean contains(Location location) {
        return contains(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    public boolean contains(int x, int y, int z) {
        return x >= minX && x <= maxX &&
                y >= minY && y <= maxY &&
                z >= minZ && z <= maxZ;
    }

    public boolean overlaps(Cuboid cuboid) {
        return cuboid.getWorld().equals(world) &&
                !(cuboid.getMinX() > maxX || cuboid.getMinZ() > maxZ ||
                        minX > cuboid.getMaxX() || minZ > cuboid.getMaxZ());
    }

    public List<Location> getCorner() {
        List<Location> corners = new ArrayList<>();
        corners.add(new Location(world,minX,minY,minZ));
        corners.add(new Location(world,minX,minY,maxZ));
        corners.add(new Location(world,maxX,minY,minZ));
        corners.add(new Location(world,maxX,minY,maxZ));
        return corners;
    }

    public List<Location> getCornerBlocks() {
        List<Location> corners = new ArrayList<>();
        //min x | min z Corners
        corners.add(new Location(world,minX,world.getHighestBlockYAt(minX,minZ),minZ));
        corners.add(new Location(world,minX+1,world.getHighestBlockYAt(minX+1,minZ),minZ));
        corners.add(new Location(world,minX,world.getHighestBlockYAt(minX,minZ+1),minZ+1));

        //min x | max z Corners
        corners.add(new Location(world,minX,world.getHighestBlockYAt(minX,maxZ),maxZ));
        corners.add(new Location(world,minX+1,world.getHighestBlockYAt(minX+1,maxZ),maxZ));
        corners.add(new Location(world,minX,world.getHighestBlockYAt(minX,maxZ-1),maxZ-1));

        //max x | min z Corners
        corners.add(new Location(world,maxX,world.getHighestBlockYAt(maxX,minZ),minZ));
        corners.add(new Location(world,maxX-1,world.getHighestBlockYAt(maxX-1,minZ),minZ));
        corners.add(new Location(world,maxX,world.getHighestBlockYAt(maxX,minZ+1),minZ+1));

        //max x | max z Corners
        corners.add(new Location(world,maxX,world.getHighestBlockYAt(maxX,maxZ),maxZ));
        corners.add(new Location(world,maxX-1,world.getHighestBlockYAt(maxX-1,maxZ),maxZ));
        corners.add(new Location(world,maxX,world.getHighestBlockYAt(maxX,maxZ-1),maxZ-1));
        return corners;
    }

    public List<Location> getCornerLines() {
       List<Location> locs = new ArrayList<>();
       for (int miny = 0; miny < 256; miny++) {
           locs.add(new Location(world,minX,miny,minZ));
       }
       for (int maxy = 0; maxy < 256; maxy++) {
            locs.add(new Location(world,maxX,maxy,maxZ));
       }
       return locs;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof Cuboid)) {
            return false;
        }
        final Cuboid other = (Cuboid) obj;
        return world.equals(other.world)
                && minX == other.minX
                && minY == other.minY
                && minZ == other.minZ
                && maxX == other.maxX
                && maxY == other.maxY
                && maxZ == other.maxZ;
    }

    @Override
    public String toString() {
        return "Cuboid[" + world.getName() + ";" + minX + ";" + + minY + ";" + minZ + ";" + maxX + ";" + + maxY + ";" + maxZ + "]";
    }
}


package com.mengcraft.wallwar.level;

import com.mengcraft.wallwar.Rank;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * Created on 16-2-25.
 */
public class Land {

    private Map<Rank, Area> areaMap;
    private Set<Area> wallSet;
    private World world;

    private int minSize;
    private int maxSize;

    private int wall;
    private int lava;
    private int lavaHigh;

    public void boomWall() {
        Random rand = new Random();
        wallSet.forEach(area -> area.iterator().forEachRemaining(loc -> {
            if (rand.nextFloat() < 0.05F) {
                loc.getWorld().createExplosion(loc, 4F, false);
            }
        }));
    }

    public void bootLava() {
        areaMap.forEach((rank, area) -> {
            if (rank != Rank.NONE) {
                processLava(area);
            }
        });
        lavaHigh++;
    }

    private void processLava(Area area) {
        Iterator<Location> it = area.getSub(AreaFace.BASE,
                lavaHigh,
                1
        ).iterator();
        for (Block b; it.hasNext(); ) {
            b = it.next().getBlock();
            if (b.getType() == Material.WATER) {
                b.setType(Material.LAVA);
            } else if (b.getType() == Material.AIR) {
                b.setType(Material.LAVA);
            }
        }
    }

    public Map<Rank, Area> getAreaMap() {
        return areaMap;
    }

    public void setAreaMap(Map<Rank, Area> areaMap) {
        this.areaMap = areaMap;
    }

    public Set<Area> getWallSet() {
        return wallSet;
    }

    public void setWallSet(Set<Area> wallSet) {
        this.wallSet = wallSet;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public int getMinSize() {
        return minSize;
    }

    public void setMinSize(int minSize) {
        this.minSize = minSize;
    }

    public int getWall() {
        return wall;
    }

    public void setWall(int wall) {
        this.wall = wall;
    }

    public int getLava() {
        return lava;
    }

    public void setLava(int lava) {
        this.lava = lava;
    }

    public int getLavaHigh() {
        return lavaHigh;
    }

    public void setLavaHigh(int lavaHigh) {
        this.lavaHigh = lavaHigh;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public Area getArea(Rank rank) {
        return areaMap.get(rank);
    }

}

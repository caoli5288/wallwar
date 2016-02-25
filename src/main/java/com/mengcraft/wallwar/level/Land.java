package com.mengcraft.wallwar.level;

import com.mengcraft.wallwar.Rank;
import org.bukkit.World;

import java.util.Map;
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

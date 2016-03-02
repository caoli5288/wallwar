package com.mengcraft.wallwar.level;

import com.mengcraft.wallwar.Rank;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import javax.naming.OperationNotSupportedException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * Created on 16-2-25.
 */
public class Land {

    private Map<Rank, Area> areaMap = new EnumMap<>(Rank.class);
    private List<Area> wallSet = new ArrayList<>();

    private World level;

    private int minSize;
    private int maxSize;

    private int wall;
    private int lava;
    private int lavaHigh;

    public boolean check() {
        throw new RuntimeException();
    }

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

    public List<Area> getWallSet() {
        return wallSet;
    }

    public World getLevel() {
        return level;
    }

    public void setLevel(World level) {
        this.level = level;
    }

    public int getMinSize() {
        return minSize;
    }

    public void setMinSize(int minSize) {
        this.minSize = minSize;
    }

    public void setArea(int i, Area area) {
        areaMap.put(Rank.values()[i], area);
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

    public Area getArea(int index) {
        return areaMap.get(Rank.values()[index]);
    }

    public Area getArea(Rank rank) {
        return areaMap.get(rank);
    }

}

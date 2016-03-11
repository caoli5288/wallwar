package com.mengcraft.wallwar.level;

import com.mengcraft.wallwar.Main;
import com.mengcraft.wallwar.Rank;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static com.mengcraft.wallwar.level.Area.toArea;

/**
 * Created on 16-2-25.
 */
public class Land {

    private final Map<Rank, Area> areaMap = new EnumMap<>(Rank.class);
    private final List<Area> wallSet = new ArrayList<>();

    private World level;
    private Main main;

    private int minSize;
    private int maxSize;

    private int lava;

    public boolean check() {
        return (areaMap.size() == 5 &&
                wallSet.size() != 0 &&
                level != null &&
                minSize > 0 &&
                maxSize > 0);
    }

    public void boomWall() {
        wallSet.forEach(area -> {
            new WallBoomer(area).runTaskTimer(main, 0, 1);
        });
    }

    public void bootLava() {
        areaMap.forEach((rank, area) -> {
            if (rank != Rank.NONE) {
                processLava(area);
            }
        });
        lava++;
    }

    private void processLava(Area area) {
        new LavaDriver(area.getSub(AreaFace.BASE,
                lava,
                1
        ).getIterator()).runTaskTimer(main, 0, 1);
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

    public void save() {
        main.getConfig().set("match.land.name", level.getName());
        main.getConfig().set("match.land.wall", toString(wallSet));
        main.getConfig().set("match.land.area", toString(areaMap.values()));
        main.getConfig().set("match.size.min", minSize);
        main.getConfig().set("match.size.max", maxSize);
    }

    public void load() {
        setLevel(main.getServer().getWorld(main.getConfig().getString("match.land.name")));
        for (String line : main.getConfig().getStringList("match.land.wall")) {
            wallSet.add(toArea(level, line));
        }
        for (String line : main.getConfig().getStringList("match.land.area")) {
            areaMap.put(Rank.getRank(areaMap.size()), toArea(level, line));
        }
        setMinSize(main.getConfig().getInt("match.size.min"));
        setMaxSize(main.getConfig().getInt("match.size.max"));
    }

    public int getLava() {
        return lava;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    private static List<String> toString(Collection<Area> set) {
        List<String> list = new ArrayList<>(set.size());
        set.forEach(area -> {
            list.add(area.toString());
        });
        return list;
    }

    @Override
    public String toString() {
        return ("Land{" +
                "areaMap=" + areaMap +
                ", wallSet=" + wallSet +
                ", level=" + level +
                ", minSize=" + minSize +
                ", maxSize=" + maxSize +
                ", lavaHigh=" + lava +
                '}');
    }

}

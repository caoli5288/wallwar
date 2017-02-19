package com.mengcraft.wallwar.level;

import com.mengcraft.wallwar.Main;
import com.mengcraft.wallwar.Rank;
import com.mengcraft.wallwar.Ranked;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.mengcraft.wallwar.Ranked.of;
import static com.mengcraft.wallwar.level.Area.toArea;

/**
 * Created on 16-2-25.
 */
public class Land {

    private final Ranked<Location> spawn = new Ranked<>();
    private final List<Area> areaSet = new ArrayList<>();
    private final List<Area> wallSet = new ArrayList<>();

    private World level;
    private Main main;

    private int minSize;
    private int maxSize;

    private int lava;

    public boolean check() {
        return (areaSet.size() != 0 &&
                wallSet.size() != 0 &&
                spawn.size() == 5 &&
                level != null &&
                minSize > 0 &&
                maxSize > 0);
    }

    public boolean isArea(Location loc) {
        for (Area area : areaSet) {
            if (area.contains(loc)) {
                return true;
            }
        }
        return false;
    }

    public boolean isWall(Location loc) {
        for (Area area : wallSet) {
            if (area.contains(loc)) {
                return true;
            }
        }
        return false;
    }

    public void boomWall() {
        wallSet.forEach(area -> {
            new WallBoomer(area).runTaskTimer(main, 0, 1);
        });
    }

    public void bootLava() {
        areaSet.forEach(area -> {
            processLava(area);
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

    public void addArea(Area area) {
        areaSet.add(area);
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public Location getSpawn(Rank rank) {
        return spawn.get(rank);
    }

    public void setSpawn(Rank rank, Location loc) {
        spawn.put(rank, loc);
    }

    public void save() {
        main.getConfig().set("match.land.name", level.getName());
        main.getConfig().set("match.land.wall", toString(wallSet));
        main.getConfig().set("match.land.area", toString(areaSet));
        main.getConfig().set("match.size.min", minSize);
        main.getConfig().set("match.size.max", maxSize);
        main.getConfig().set("match.size.spawn", spawn.map());
    }

    public void load() {
        setLevel(main.getServer().getWorld(main.getConfig().getString("match.land.name")));
        for (String line : main.getConfig().getStringList("match.land.wall")) {
            wallSet.add(toArea(level, line));
        }
        for (String line : main.getConfig().getStringList("match.land.area")) {
            areaSet.add(toArea(level, line));
        }
        spawn.putAll(of(main.getConfig().
                getConfigurationSection("match.size.spawn").getValues(false)
        ));
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
        return ("Land (" +
                "areaSet=" + areaSet + ',' +
                "wallSet=" + wallSet + ',' +
                "minSize=" + minSize + ',' +
                "maxSize=" + maxSize + ',' +
                "level=" + level + ',' +
                "lavaLen=" + lava +
                ")");
    }

}

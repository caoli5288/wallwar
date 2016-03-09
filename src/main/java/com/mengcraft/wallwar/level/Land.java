package com.mengcraft.wallwar.level;

import com.mengcraft.wallwar.Main;
import com.mengcraft.wallwar.Rank;
import com.mengcraft.wallwar.util.FileUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.block.Block;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.mengcraft.wallwar.level.Area.toArea;
import static com.mengcraft.wallwar.util.FileUtil.copy;
import static com.mengcraft.wallwar.util.FileUtil.delete;

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
            new WallBoomer(area.getCollection()).runTaskTimer(main, 0, 1);
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
        Iterator<Location> it = area.getSub(AreaFace.BASE,
                lava,
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

    public void saveRegion() {
        try {
            copy(level.getWorldFolder(), new File(main.getDataFolder(), level.getName()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void save() {
        main.getConfig().set("match.land.name", level.getName());
        main.getConfig().set("match.land.wall", toString(wallSet));
        main.getConfig().set("match.land.area", toString(areaMap.values()));
        main.getConfig().set("match.size.min", minSize);
        main.getConfig().set("match.size.max", maxSize);
    }

    public void loadRegion() {
        String name = main.getConfig().getString("match.land.name");
        File file = new File(main.getServer().getWorldContainer(), name);
        main.getServer().unloadWorld(name, false);
        try {
            delete(file);
            copy(new File(main.getDataFolder(), name), file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        World world = main.getServer().createWorld(new WorldCreator(name));
        world.setAutoSave(false);
    }

    public int getLava() {
        return lava;
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

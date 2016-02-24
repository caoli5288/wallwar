package com.mengcraft.wallwar;

import com.mengcraft.wallwar.util.Area;
import org.bukkit.World;

import java.util.Map;
import java.util.Set;

/**
 * Created on 16-2-23.
 */
public class Land {

    private Map<Team, Area> areaSet;
    private Set<Area> wallSet;

    private World world;

    public Area getArea(Team team) {
        return areaSet.get(team);
    }

}

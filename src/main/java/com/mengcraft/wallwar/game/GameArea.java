package com.mengcraft.wallwar.game;

import com.mengcraft.wallwar.Team;
import org.bukkit.Location;

/**
 * Created on 16-2-23.
 */
public class GameArea {

    private Location base;
    private Location offset;

    private Team team;

    public boolean contains(Location loc) {
        return (loc.getX() - base.getX() < offset.getX() &&
                loc.getY() - base.getY() < offset.getY() &&
                loc.getZ() - base.getZ() < offset.getZ());
    }

}

package com.mengcraft.wallwar.util;

import org.bukkit.Location;

import java.util.Iterator;

/**
 * Created on 16-2-23.
 */
public class Area implements Iterable<Location> {

    private Location base;
    private Location offset;
    private Location spawn;

    public boolean contains(Location loc) {
        return ((offset.getX() > 0 ? compareX(loc) > 0 : compareX(loc) < 0) &&
                (offset.getY() > 0 ? compareY(loc) > 0 : compareY(loc) < 0) &&
                (offset.getZ() > 0 ? compareZ(loc) > 0 : compareZ(loc) < 0)
        );
    }

    public double compareX(Location loc) {
        return base.getX() + offset.getX() - loc.getX();
    }

    public double compareY(Location loc) {
        return base.getY() + offset.getY() - loc.getY();
    }

    public double compareZ(Location loc) {
        return base.getZ() + offset.getZ() - loc.getZ();
    }

    public Location getBase() {
        return base;
    }

    public void setBase(Location base) {
        this.base = base;
    }

    public Location getOffset() {
        return offset;
    }

    public void setOffset(Location offset) {
        this.offset = offset;
    }

    @Override
    public Iterator<Location> iterator() {
        return new AreaIterator(base, base.clone().add(offset));
    }

    public Location getSpawn() {
        return spawn;
    }

    public void setSpawn(Location spawn) {
        this.spawn = spawn;
    }

}

package com.mengcraft.wallwar.level;

import org.bukkit.Location;

import java.util.Iterator;

/**
 * Created on 16-2-23.
 */
public class Area implements Iterable<Location> {

    private Location base;
    private Location offset;
    private Location spawn;

    public Area(Location base, Location offset) {
        this.base = base;
        this.offset = offset;
    }

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

    public Location getOffset() {
        return offset;
    }

    public Area getSub(AreaFace face, int begin, int length) {
        Location base = this.base.clone();
        Location offset = this.offset.clone();
        if (face.equals(AreaFace.BASE)) {
            if (offset.getY() > 0) {
                base.setY(base.getY() + begin);
                offset.setY(length);
            } else {
                offset.setY(offset.getY() + begin);
                base.setY(base.getY() + offset.getY() + length);
            }
        } else {
            if (offset.getY() > 0) {
                offset.setY(offset.getY() - begin);
                base.setY(base.getY() + offset.getY() - length);
            } else {
                base.setY(base.getY() - begin);
                offset.setY(-length);
            }
        }
        return new Area(base, offset);
    }

    public void setBase(Location base) {
        this.base = base;
    }

    public void setOffset(Location offset) {
        this.offset = offset;
    }

    public Location getSpawn() {
        return spawn;
    }

    public void setSpawn(Location spawn) {
        this.spawn = spawn;
    }

    @Override
    public Iterator<Location> iterator() {
        return new AreaIterator(base, base.clone().add(offset));
    }

}

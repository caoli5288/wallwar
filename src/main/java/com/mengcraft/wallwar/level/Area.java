package com.mengcraft.wallwar.level;

import com.mengcraft.wallwar.Main;
import org.bukkit.Location;
import org.bukkit.World;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Created on 16-2-23.
 */
public class Area implements Iterable<Location> {

    private Location base;
    private Location offset;

    public Area(Location base, Location offset) {
        this.base = base;
        this.offset = offset;
    }

    public boolean contains(Location loc) {
        return (check(loc.getX() - base.getX(), offset.getX()) &&
                check(loc.getY() - base.getY(), offset.getY()) &&
                check(loc.getZ() - base.getZ(), offset.getZ())
        );
    }

    private boolean check(double x, double y) {
        return x == y || ((y > 0) ? (x > 0 && x < y) : (x < 0 && x > y));
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

    @SuppressWarnings("unchecked")
    public static Location toLocation(World world, String in) {
        return toLocation(world, Main.GSON.fromJson(in, Map.class));
    }

    public static Location toLocation(World world, Map<String, Number> map) {
        return map != null ? new Location(world,
                map.get("x").doubleValue(),
                map.get("y").doubleValue(),
                map.get("z").doubleValue()
        ) : null;
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Number> toMap(Location in) {
        Map<String, Number> object = new HashMap<>();
        object.put("x", in.getX());
        object.put("y", in.getY());
        object.put("z", in.getZ());
        return object;
    }

    @SuppressWarnings("unchecked")
    public static Area toArea(World world, Map<String, Map> map) {
        return new Area(
                toLocation(world, map.get("base")),
                toLocation(world, map.get("offset"))
        );
    }

    @SuppressWarnings("unchecked")
    public static Area toArea(World world, String in) {
        return toArea(world, (JSONObject) JSONValue.parse(in));
    }

    public String toString() {
        Map<String, Object> object = new HashMap<>();
        object.put("base", toMap(base));
        object.put("offset", toMap(offset));
        return Main.GSON.toJson(object);
    }

    public AreaIterator getIterator() {
        return new AreaIterator(base, base.clone().add(offset));
    }

    public List<Location> getList(Predicate<Location> p) {
        ArrayList<Location> list = new ArrayList<>();
        iterator().forEachRemaining(loc -> {
            if (p.test(loc)) {
                list.add(loc);
            }
        });
        return list;
    }

    public List<Location> getList() {
        ArrayList<Location> list = new ArrayList<>();
        iterator().forEachRemaining(loc -> {
            list.add(loc);
        });
        return list;
    }

    @Override
    public Iterator<Location> iterator() {
        return getIterator();
    }

    public static Area of(Location left, Location right) {
        return new Area(left, right.subtract(left));
    }

}

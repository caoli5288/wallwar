package com.mengcraft.wallwar.util;

import com.mengcraft.wallwar.Main;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on 16-2-25.
 */
public final class LocationUtil {

    @SuppressWarnings("unchecked")
    public static Location toLocation(String description, World world) {
        Map<String, Number> map = Main.GSON.fromJson(description, HashMap.class);
        return new Location(
                world,
                map.get("x").doubleValue(),
                map.get("y").doubleValue(),
                map.get("z").doubleValue(),
                map.get("yaw").floatValue(),
                map.get("pitch").floatValue()
        );
    }

    public static Location toLocation(String description) {
        return toLocation(description, null);
    }

    public static String toString(Location location) {
        return Main.GSON.toJson(new Mapping(location));
    }

    static class Mapping extends HashMap<String, Number> {

        public Mapping(Location location) {
            put("x", location.getX());
            put("y", location.getY());
            put("z", location.getZ());
            put("yaw", location.getYaw());
            put("pitch", location.getPitch());
        }
    }

}

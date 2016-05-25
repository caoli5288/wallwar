package com.mengcraft.wallwar.scoreboard;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created on 16-5-17.
 */
public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("Have fun! ;-)");
    }

    public static boolean eq(Object a, Object b) {
        return (a == b) || (a != null && a.equals(b));
    }

}

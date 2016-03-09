package com.mengcraft.wallwar.db;

import com.avaje.ebean.EbeanServer;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;

public class ReflectUtil {

    public static final ReflectUtil UTIL = new ReflectUtil();
    
    private Field server;
    private Field loader;
    
    public void register(Plugin proxy, EbeanServer in) throws Exception {
        if (server == null) {
            server = JavaPlugin.class.getDeclaredField("ebean");
            server.setAccessible(true);
        }
        server.set(proxy, in);
    }
    
	public ClassLoader loader(Plugin in) throws Exception {
		if (loader == null) {
			loader = JavaPlugin.class.getDeclaredField("classLoader");
			loader.setAccessible(true);
		}
		return ClassLoader.class.cast(loader.get(in));
	}
    
}

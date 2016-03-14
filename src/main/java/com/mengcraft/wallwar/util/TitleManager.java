package com.mengcraft.wallwar.util;

import org.bukkit.Server;
import org.bukkit.entity.Player;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.Collection;
import java.util.WeakHashMap;

/**
 * Created on 16-3-13.
 */
public interface TitleManager {

    String SCRIPT = "" +
            "var PacketPlayOutTitle = Java.type(\"net.minecraft.server.\" + version + \".PacketPlayOutTitle\");\n" +
            "var EnumTitleAction    = Java.type(\"net.minecraft.server.\" + version + \".PacketPlayOutTitle$EnumTitleAction\");\n" +
            "var ChatComponentText  = Java.type(\"net.minecraft.server.\" + version + \".ChatComponentText\");\n" +
            "function setTitle(p, title) {\n" +
            "    if (title == null) {\n" +
            "        p.getHandle().playerConnection.sendPacket(new PacketPlayOutTitle(EnumTitleAction.RESET, null));\n" +
            "    } else if (pool.containsKey(title)) {\n" +
            "        pool.get(title).forEach(function(packet) {\n" +
            "            p.getHandle().playerConnection.sendPacket(packet);\n" +
            "        });\n" +
            "    } else {\n" +
            "        var list = new java.util.ArrayList();\n" +
            "        if (title.title != null) {\n" +
            "            list.add(createPacket(EnumTitleAction.TITLE, title.title, title));\n" +
            "        }\n" +
            "        if (title.sub != null) {\n" +
            "            list.add(createPacket(EnumTitleAction.SUBTITLE, title.sub, title));\n" +
            "        }\n" +
            "        list.forEach(function(packet) {\n" +
            "            p.getHandle().playerConnection.sendPacket(packet);\n" +
            "        });\n" +
            "        pool.put(title, list);\n" +
            "    }\n" +
            "}\n" +
            "function createPacket(type, text, title) {\n" +
            "    return new PacketPlayOutTitle(\n" +
            "        type,\n" +
            "        new ChatComponentText(text),\n" +
            "        title.fadeIn,\n" +
            "        title.display,\n" +
            "        title.fadeOut\n" +
            "    )\n" +
            "}";

    void setTitle(Player p, Title title);

    default void setTitle(Player p) {
        setTitle(p, null);
    }

    default void setTitle(Collection<Player> list) {
        list.forEach(p -> setTitle(p));
    }

    default void setTitle(Collection<Player> list, Title title) {
        list.forEach(p -> setTitle(p, title));
    }

    static TitleManager createManager(Server server) {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("js");
        try {
            engine.put("version", server.getClass().getName().split("\\.")[3]);
            engine.put("pool", new WeakHashMap<>());
            engine.eval(SCRIPT);
        } catch (ScriptException e) {
            throw new RuntimeException(e);
        }
        return Invocable.class.cast(engine).getInterface(TitleManager.class);
    }

}

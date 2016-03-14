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
            "PacketPlayOutTitle = Java.type(\"net.minecraft.server.\" + version + \".PacketPlayOutTitle\");\n" +
            "EnumTitleAction    = Java.type(\"net.minecraft.server.\" + version + \".PacketPlayOutTitle$EnumTitleAction\");\n" +
            "ChatComponentText  = Java.type(\"net.minecraft.server.\" + version + \".ChatComponentText\");\n" +
            "function setTitle(p, title) {\n" +
            "    var packet = new PacketPlayOutTitle(EnumTitleAction.RESET, null);\n" +
            "    var packetList;\n" +
            "    if (pool.containsKey(title)) {\n" +
            "        packetList = pool.get(title);\n" +
            "    } else {\n" +
            "        pool.put(title, packetList = new java.util.ArrayList());\n" +
            "        if (title.sub != null) {\n" +
            "            packetList.add(new PacketPlayOutTitle(\n" +
            "                    EnumTitleAction.SUBTITLE,\n" +
            "                    new ChatComponentText(title.sub),\n" +
            "                    title.fadeIn,\n" +
            "                    title.display,\n" +
            "                    title.fadeOut\n" +
            "                    )\n" +
            "            );\n" +
            "        }\n" +
            "        if (title.title != null) {\n" +
            "            packetList.add(new PacketPlayOutTitle(\n" +
            "                    EnumTitleAction.TITLE,\n" +
            "                    new ChatComponentText(title.title),\n" +
            "                    title.fadeIn,\n" +
            "                    title.display,\n" +
            "                    title.fadeOut\n" +
            "                    )\n" +
            "            );\n" +
            "        }\n" +
            "    }\n" +
            "    p.getHandle().playerConnection.sendPacket(packet);\n" +
            "    packetList.forEach(function(packet) {\n" +
            "        p.getHandle().playerConnection.sendPacket(packet);\n" +
            "    });\n" +
            "}";

    void setTitle(Player p, Title title);

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

package com.mengcraft.wallwar.util;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * Created on 16-3-13.
 */
public interface TitleManager {

    String SCRIPT = "" +
            "PacketPlayOutTitle = Java.type(\"net.minecraft.server.\" + version + \".PacketPlayOutTitle\");\n" +
            "EnumTitleAction = Java.type(\"net.minecraft.server.\" + version + \".PacketPlayOutTitle$EnumTitleAction\");\n" +
            "ChatComponentText = Java.type(\"net.minecraft.server.\" + version + \".ChatComponentText\");\n" +
            "function setTitle(p, title) {\n" +
            "    if (title == null) {\n" +
            "        sendReset(p);\n" +
            "    } else {\n" +
            "        if (title.display > 0) {\n" +
            "            sendTime(p, title);\n" +
            "        }\n" +
            "        sendSubtitle(p, title);\n" +
            "        sendTitle(p, title);\n" +
            "    }\n" +
            "}\n" +
            "function sendPacket(p, packet) {\n" +
            "    p.getHandle().playerConnection.sendPacket(packet);\n" +
            "}\n" +
            "function sendTitle(p, title) {\n" +
            "    sendPacket(p, new PacketPlayOutTitle(EnumTitleAction.TITLE, new ChatComponentText(title.title)));\n" +
            "}\n" +
            "function sendSubtitle(p, title) {\n" +
            "    sendPacket(p, new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, new ChatComponentText(title.sub)));\n" +
            "}\n" +
            "function sendTime(p, title) {\n" +
            "    sendPacket(p, new PacketPlayOutTitle(title.fadeIn, title.display, title.fadeOut));\n" +
            "}\n" +
            "function sendReset(p) {\n" +
            "    sendPacket(p, new PacketPlayOutTitle(EnumTitleAction.RESET, null));\n" +
            "}";

    void setTitle(Player p, Title title);

    static TitleManager createManager(Plugin plugin) {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("js");
        try {
            engine.put("version", plugin.getServer().getClass().getName().split("\\.")[3]);
            engine.eval(SCRIPT);
        } catch (ScriptException e) {
            throw new RuntimeException(e);
        }
        return Invocable.class.cast(engine).getInterface(TitleManager.class);
    }

}

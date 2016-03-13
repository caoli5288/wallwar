package com.mengcraft.wallwar.entity;

import org.bukkit.Server;
import org.bukkit.entity.Player;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * Created on 16-3-13.
 */
public interface Action {

    static Action createAction(Server server) {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("js");
        try {
            engine.put("version", server.getClass().getName().split("\\.")[3]);
            engine.eval("" +
                    "var ChatComponentText = Java.type(\"net.minecraft.server.\" + version + \".ChatComponentText\");\n" +
                    "var PacketPlayOutChat = Java.type(\"net.minecraft.server.\" + version + \".PacketPlayOutChat\");\n" +
                    "function toPacket(text) {\n" +
                    "    return new PacketPlayOutChat(new ChatComponentText(text), 2);\n" +
                    "}\n" +
                    "function sendPacket(p, packet) {\n" +
                    "    p.getHandle().playerConnection.sendPacket(packet);\n" +
                    "}"
            );
        } catch (ScriptException e) {
            throw new RuntimeException(e);
        }
        return Invocable.class.cast(engine).getInterface(Action.class);
    }

    default void sendAction(Player p, String text) {
        sendPacket(p, toPacket(text));
    }

    void sendPacket(Player p, Object packet);

    Object toPacket(String text);

}

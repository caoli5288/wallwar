package com.mengcraft.wallwar.util;

import org.bukkit.Server;
import org.bukkit.entity.Player;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.WeakHashMap;

/**
 * Created on 16-3-13.
 */
public interface Action {

    static Action createAction(Server server) {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("js");
        try {
            engine.put("version", server.getClass().getName().split("\\.")[3]);
            engine.put("pool", new WeakHashMap<>());
            engine.eval("" +
                    "ChatComponentText = Java.type(\"net.minecraft.server.\" + version + \".ChatComponentText\");\n" +
                    "PacketPlayOutChat = Java.type(\"net.minecraft.server.\" + version + \".PacketPlayOutChat\");\n" +
                    "function createPacket(text) {\n" +
                    "    return new PacketPlayOutChat(new ChatComponentText(text), 2);\n" +
                    "}\n" +
                    "function sendPacket(p, packet) {\n" +
                    "    p.getHandle().playerConnection.sendPacket(packet);\n" +
                    "}\n" +
                    "function sendAction(p, text) {\n" +
                    "    var packet;\n" +
                    "    if (pool.containsKey(text)) {\n" +
                    "        packet = pool.get(text);\n" +
                    "    } else {\n" +
                    "        pool.put(text, packet = createPacket(text));\n" +
                    "    }\n" +
                    "    sendPacket(p, packet);\n" +
                    "}"
            );
        } catch (ScriptException e) {
            throw new RuntimeException(e);
        }
        return Invocable.class.cast(engine).getInterface(Action.class);
    }

    void sendAction(Player p, String text);

}

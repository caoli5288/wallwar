package com.mengcraft.wallwar;

import jdk.nashorn.api.scripting.JSObject;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * Created on 16-3-13.
 */
public class Action {

    private JSObject functionToPacket;
    private JSObject functionSendPacket;

    public void init(Server server) throws ScriptException {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("js");
        engine.put("version", server.getClass().getName().split("\\.")[3]);
        engine.eval("" +
                "var CraftChatMessage = Java.type(\"org.bukkit.craftbukkit.\" + version + \".util.CraftChatMessage\");\n" +
                "var PacketPlayOutChat = Java.type(\"net.minecraft.server.\" + version + \".PacketPlayOutChat\");\n" +
                "function toPacket(text) {\n" +
                "    return new PacketPlayOutChat(CraftChatMessage.fromString(text)[0], 2);\n" +
                "}\n" +
                "function sendPacket(p, packet) {\n" +
                "    p.getHandle().playerConnection.sendPacket(packet);\n" +
                "}"
        );
        setFunctionToPacket(engine.get("toPacket"));
        setFunctionSendPacket(engine.get("sendPacket"));
    }

    public Object toPacket(String text) {
        return functionToPacket.call(functionToPacket, text);
    }

    public void sendAction(Player p, String text) {
        sendPacket(p, toPacket(text));
    }

    public void sendPacket(Player p, Object packet) {
        functionSendPacket.call(functionSendPacket, p, packet);
    }

    public void setFunctionToPacket(Object functionToPacket) {
        this.functionToPacket = JSObject.class.cast(functionToPacket);
    }

    public void setFunctionSendPacket(Object functionSendPacket) {
        this.functionSendPacket = JSObject.class.cast(functionSendPacket);
    }

}

package com.mengcraft.wallwar.entity;

import jdk.nashorn.api.scripting.JSObject;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * Created on 16-3-13.
 */
public class Action {

    private JSObject funcToPacket;
    private JSObject funcSendPacket;

    public void init(Server server) throws ScriptException {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("js");
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
        setFuncToPacket(engine.get("toPacket"));
        setFuncSendPacket(engine.get("sendPacket"));
    }

    public Object toPacket(String text) {
        return funcToPacket.call(funcToPacket, ChatColor.translateAlternateColorCodes('&', text));
    }

    public void sendAction(Player p, String text) {
        sendPacket(p, toPacket(text));
    }

    public void sendPacket(Player p, Object packet) {
        funcSendPacket.call(funcSendPacket, p, packet);
    }

    public void setFuncToPacket(Object funcToPacket) {
        this.funcToPacket = JSObject.class.cast(funcToPacket);
    }

    public void setFuncSendPacket(Object funcSendPacket) {
        this.funcSendPacket = JSObject.class.cast(funcSendPacket);
    }

}

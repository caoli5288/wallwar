package com.mengcraft.wallwar.util;

import net.minecraft.server.v1_12_R1.ChatComponentText;
import net.minecraft.server.v1_12_R1.ChatMessageType;
import net.minecraft.server.v1_12_R1.PacketPlayOutChat;
import org.bukkit.Server;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

/**
 * Created on 16-3-13.
 */
public interface Action {

    class V1_12_1 implements Action {

        public void sendAction(Player p, String text) {
            PacketPlayOutChat pk = new PacketPlayOutChat(new ChatComponentText(text), ChatMessageType.GAME_INFO);
            ((CraftPlayer) p).getHandle().playerConnection.sendPacket(pk);
        }
    }

    static Action createAction(Server server) {
//        ScriptEngine engine = new ScriptEngineManager().getEngineByName("js");
//        try {
//            engine.put("version", server.getClass().getName().split("\\.")[3]);
//            engine.put("pool", new WeakHashMap<>());
//            engine.eval("" +
//                    "ChatComponentText = Java.type(\"net.minecraft.server.\" + version + \".ChatComponentText\");\n" +
//                    "PacketPlayOutChat = Java.type(\"net.minecraft.server.\" + version + \".PacketPlayOutChat\");\n" +
//                    "function createPacket(text) {\n" +
//                    "    return new PacketPlayOutChat(new ChatComponentText(text), 2);\n" +
//                    "}\n" +
//                    "function sendPacket(p, packet) {\n" +
//                    "    p.getHandle().playerConnection.sendPacket(packet);\n" +
//                    "}\n" +
//                    "function sendAction(p, text) {\n" +
//                    "    var packet;\n" +
//                    "    if (pool.containsKey(text)) {\n" +
//                    "        packet = pool.get(text);\n" +
//                    "    } else {\n" +
//                    "        pool.put(text, packet = createPacket(text));\n" +
//                    "    }\n" +
//                    "    sendPacket(p, packet);\n" +
//                    "}"
//            );
//        } catch (ScriptException e) {
//            throw new RuntimeException(e);
//        }
//        return Invocable.class.cast(engine).getInterface(Action.class);
        return new V1_12_1();
    }

    void sendAction(Player p, String text);
}

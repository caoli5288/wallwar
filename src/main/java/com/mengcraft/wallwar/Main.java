package com.mengcraft.wallwar;

import com.google.gson.Gson;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.logging.Level;

/**
 * Created on 16-2-23.
 */
public class Main extends JavaPlugin {

    private final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    private Location lobby;

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults(true);
        saveConfig();

        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
    }

    private void toLobby(Player p) {
        if (buffer.size() < 1) {
            DataOutput writer = new DataOutputStream(buffer);
            try {
                writer.writeUTF("Connect");
                writer.writeUTF(getConfig().getString("generic.lobby"));
            } catch (IOException e) {
                getLogger().log(Level.WARNING, "", e);
            }
        }
        p.sendPluginMessage(this, "BungeeCord", buffer.toByteArray());
    }

    public Location getLobby() {
        return lobby;
    }

    public void setLobby(Location lobby) {
        this.lobby = lobby;
    }

    public static final Gson GSON = new Gson();
    public static final boolean DEBUG = true;

}

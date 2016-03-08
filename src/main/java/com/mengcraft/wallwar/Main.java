package com.mengcraft.wallwar;

import com.google.gson.Gson;
import com.mengcraft.wallwar.level.Land;
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

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults(true);
        saveConfig();

        if (getConfig().getConfigurationSection("match") != null) {
            Match match = new Match();
            Land land = new Land();
            land.setMain(this);
            land.loadRegion();
            land.load();
            match.setLand(land);
            match.setMain(this);
            match.load();

            Executor executor = new Executor();
            Ticker ticker = new Ticker();
            ticker.setMain(this);
            ticker.setMatch(match);
            executor.setMatch(match);
            executor.setMain(this);
            executor.setTicker(ticker);

            getServer().getScheduler().runTaskTimer(this, ticker, 20, 20);
            getServer().getPluginManager().registerEvents(executor, this);
        } else {
            Commander commander = new Commander();
            Match match = new Match();
            Land land = new Land();
            land.setMain(this);
            match.setLand(land);
            match.setMain(this);
            commander.setMain(this);
            commander.setMatch(match);

            getCommand("wall").setExecutor(commander);
        }
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
    }

    public void tpToLobby(Player p) {
        if (buffer.size() < 1) {
            DataOutput writer = new DataOutputStream(buffer);
            try {
                writer.writeUTF("Connect");
                writer.writeUTF(getConfig().getString("lobby"));
            } catch (IOException e) {
                getLogger().log(Level.WARNING, "", e);
            }
        }
        p.sendPluginMessage(this, "BungeeCord", buffer.toByteArray());
    }

    public static final Gson GSON = new Gson();
    public static final boolean DEBUG = true;

}

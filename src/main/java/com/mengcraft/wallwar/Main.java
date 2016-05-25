package com.mengcraft.wallwar;

import com.google.gson.Gson;
import com.mengcraft.wallwar.db.EbeanHandler;
import com.mengcraft.wallwar.db.EbeanManager;
import com.mengcraft.wallwar.entity.WallUser;
import com.mengcraft.wallwar.level.Land;
import com.mengcraft.wallwar.util.Action;
import com.mengcraft.wallwar.util.TitleManager;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

/**
 * Created on 16-2-23.
 */
public class Main extends JavaPlugin {

    private final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    private final Map<UUID, WallUser> userMap = new ConcurrentHashMap<>();

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults(true);
        saveConfig();

        if (getConfig().getConfigurationSection("match") != null) {
            Action action = Action.createAction(getServer());
            TitleManager title = TitleManager.createManager(this);

            Land land = new Land();
            land.setMain(this);
            land.load();

            Match match = new Match();
            match.setLand(land);
            match.setMain(this);
            match.load();

            Executor executor = new Executor();
            executor.setMatch(match);
            executor.setMain(this);

            Ticker ticker = new Ticker();
            ticker.setMain(this);
            ticker.setMatch(match);
            ticker.setWait(match.getWait());
            ticker.setWaitExit(15);
            ticker.setAction(action);
            ticker.setTitle(title);

            getServer().getScheduler().runTaskTimer(this, ticker, 20, 20);
            getServer().getPluginManager().registerEvents(executor, this);
        } else {
            Action action = Action.createAction(getServer());
            TitleManager title = TitleManager.createManager(this);

            Land land = new Land();
            land.setMain(this);

            Match match = new Match();
            match.setLand(land);
            match.setMain(this);

            Commander commander = new Commander();
            commander.setMain(this);
            commander.setMatch(match);
            commander.setAction(action);
            commander.setTitle(title);

            getCommand("wall").setExecutor(commander);
        }

        EbeanHandler handler = EbeanManager.DEFAULT.getHandler(this);
        if (handler.isNotInitialized()) {
            handler.define(WallUser.class);
            handler.setMaxSize(0xF);
            try {
                handler.initialize();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        handler.install();
        handler.reflect();

        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
    }

    @Override
    public void onDisable() {
        getServer().getOnlinePlayers().forEach(p -> {
            WallUser removed = userMap.remove(p.getUniqueId());
            if (removed != null) {
                getDatabase().save(removed);
            }
        });
    }

    public void saveBean(Player p) {
        WallUser removed = userMap.remove(p.getUniqueId());
        if (removed != null) getServer().getScheduler().runTaskAsynchronously(this, () -> {
            getDatabase().save(removed);
        });
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

    public Map<UUID, WallUser> getUserMap() {
        return userMap;
    }

    public void runTask(Runnable runnable) {
        getServer().getScheduler().runTaskAsynchronously(this, runnable);
    }

    public void createUser(Player p) {
        WallUser user = getDatabase().createEntityBean(WallUser.class);
        user.setId(p.getUniqueId());
        user.setName(p.getName());
        userMap.put(p.getUniqueId(), user);
    }

    public static final Gson GSON = new Gson();

}

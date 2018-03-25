package com.mengcraft.wallwar;

import com.avaje.ebean.EbeanServer;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.google.gson.Gson;
import com.mengcraft.simpleorm.EbeanHandler;
import com.mengcraft.simpleorm.EbeanManager;
import com.mengcraft.wallwar.entity.WallUser;
import com.mengcraft.wallwar.level.Land;
import com.mengcraft.wallwar.util.Action;
import com.mengcraft.wallwar.util.TitleManager;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created on 16-2-23.
 */
public class Main extends JavaPlugin {

    private final Map<UUID, WallUser> ingame = new ConcurrentHashMap<>();
    private EbeanServer dataSource;

    @Override
    public void onEnable() {
        saveDefaultConfig();
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

            Ticker ticker = new Ticker(this, match);
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
//        handler.reflect();
        dataSource = handler.getServer();

        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
    }

    @Override
    public void onDisable() {
        getServer().getOnlinePlayers().forEach(p -> {
            WallUser removed = ingame.remove(p.getUniqueId());
            if (removed != null) {
                dataSource.save(removed);
            }
        });
    }

    public void save(Player p) {
        WallUser removed = ingame.remove(p.getUniqueId());
        if (removed != null) {
            CompletableFuture.runAsync(() -> dataSource.save(removed));
        }
    }

    public void tpToLobby(Player p) {
        ByteArrayDataOutput buf = ByteStreams.newDataOutput();
        buf.writeUTF("Connect");
        buf.writeUTF(getConfig().getString("lobby"));
        p.sendPluginMessage(this, "BungeeCord", buf.toByteArray());
    }

    public Map<UUID, WallUser> getIngame() {
        return ingame;
    }

    public void runAsync(Runnable runnable) {
        CompletableFuture.runAsync(runnable);
    }

    public void createUser(Player p) {
        WallUser user = dataSource.createEntityBean(WallUser.class);
        user.setId(p.getUniqueId());
        user.setName(p.getName());
        ingame.put(p.getUniqueId(), user);
    }

    public static boolean nil(Object any) {
        return any == null;
    }

    public EbeanServer getDataSource() {
        return dataSource;
    }

    public static final Gson GSON = new Gson();
}

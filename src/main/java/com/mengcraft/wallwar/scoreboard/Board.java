package com.mengcraft.wallwar.scoreboard;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.Scoreboard;

import java.util.function.BooleanSupplier;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Created on 16-5-16.
 */
public abstract class Board {

    private final Scoreboard board;
    private final Plugin plugin;
    private int taskId;

    public Board(Plugin plugin) {
        board = plugin.getServer().getScoreboardManager().getNewScoreboard();
        this.plugin = plugin;
    }

    public <T> void update(Supplier<Boolean> condition, int interval) {
        if (taskId == 0) {
            taskId = plugin.getServer().getScheduler().runTaskTimer(plugin, () -> {
                if (condition.get()) {
                    update();
                } else {
                    cancel();
                }
            }, 0, interval).getTaskId();
        }
    }

    public abstract void update();

    public void update(Player p) {
        p.setScoreboard(board);
    }

    public void cancel() {
        if (taskId != 0) {
            plugin.getServer().getScheduler().cancelTask(taskId);
        }
    }

    public Scoreboard getBoard() {
        return board;
    }

    public Plugin getPlugin() {
        return plugin;
    }

}

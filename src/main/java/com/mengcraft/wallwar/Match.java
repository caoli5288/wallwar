package com.mengcraft.wallwar;

import com.mengcraft.wallwar.level.Land;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created on 16-2-25.
 */
public class Match {

    private Map<Rank, List<Player>> map;
    private Map<Player, Rank> mapper;

    private Main main;
    private Land land;

    private Set<Player> waiter;
    private Set<Player> viewer;
    private Set<Player> winner;

    private int wall;
    private int lava;
    private int wait;

    public void checkUp() {
        if (Main.DEBUG) {
            main.getLogger().info("DEBUG #1 Check if game end.");
        }
        HashSet<Rank> set = new HashSet<>(mapper.values());
        if (set.size() < 2) {
            if (Main.DEBUG) {
                main.getLogger().info("DEBUG #2 Game is end!");
                main.getLogger().info("DEBUG #3 List winner!");
            }
            set.forEach(rank -> winner.addAll(map.get(rank)));
        }
    }

    public void clearUp(Player p) {
        if (Main.DEBUG) {
            main.getLogger().info("DEBUG #3 Clear up player " + p.getName() + ".");
        }
        if (mapper.containsKey(p)) {
            addViewer(p);
            if (Main.DEBUG) {
                main.getLogger().info("DEBUG #3 He is player.");
            }
            mapper.remove(p).addNumber(-1);
        } else if (viewer.contains(p)) {
            if (Main.DEBUG) {
                main.getLogger().info("DEBUG #3 He is viewer.");
            }
            viewer.remove(p);
        } else {
            if (Main.DEBUG) {
                main.getLogger().info("DEBUG #3 He is waiter.");
            }
            waiter.remove(p);
        }
    }

    public void addViewer(Player p) {
        if (Main.DEBUG) {
            main.getLogger().info("DEBUG #4 Makeup a viewer " + p.getName() + ".");
        }
        Location spawn = land.getArea(Rank.NONE).getSpawn();
        if (p.getLocation().getWorld() != spawn.getWorld()) {
            p.teleport(spawn);
        }
        if (p.getGameMode() != GameMode.SPECTATOR) {
            p.setGameMode(GameMode.SPECTATOR);
            p.setHealth(p.getMaxHealth());
        }
        viewer.add(p);
    }

    public void addWaiter(Player p) {
        waiter.add(p);
    }

    public void setViewer(Set<Player> viewer) {
        this.viewer = viewer;
    }

    public Set<Player> getWinner() {
        return winner;
    }

    public Set<Player> getViewer() {
        return viewer;
    }

    public Set<Player> getWaiter() {
        return waiter;
    }

    public Land getLand() {
        return land;
    }

    public void setLand(Land land) {
        this.land = land;
    }

    public void setMap(Map<Rank, List<Player>> map) {
        this.map = map;
    }

    public boolean isFighter(Player p) {
        return mapper.get(p) != null;
    }

    public boolean hasFinish() {
        return !winner.isEmpty();
    }

    public boolean isRunning() {
        return !mapper.isEmpty();
    }

    public boolean isRankArea(Player p, Location loc) {
        if (mapper.containsKey(p)) {
            return land.getArea(mapper.get(p)).contains(loc);
        }
        return false;
    }

    public boolean isSameRank(Object o, Object other) {
        if (mapper.containsKey(o)) { // NPE
            return mapper.get(o) == mapper.get(other);
        }
        return false;
    }

    public void setWaiter(Set<Player> waiter) {
        this.waiter = waiter;
    }

    public int getWait() {
        return wait;
    }

    public void setWait(int wait) {
        this.wait = wait;
    }

    public int getWall() {
        return wall;
    }

    public void setWall(int wall) {
        this.wall = wall;
    }

    public int getLava() {
        return lava;
    }

    public void setLava(int lava) {
        this.lava = lava;
    }

    public boolean isTouchMaxSize() {
        return waiter.size() >= land.getMaxSize();
    }

    public boolean isTouchMinSize() {
        return waiter.size() >= land.getMinSize();
    }

}

package com.mengcraft.wallwar;

import com.mengcraft.wallwar.level.Land;
import com.mengcraft.wallwar.util.MultiMap;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created on 16-2-25.
 */
public class Match {

    private MultiMap<Rank, Player> map;
    private Map<Player, Rank> mapper;

    private Location lobby;
    private Main main;
    private Land land;

    private Set<Player> waiter;
    private Set<Player> viewer;
    private Set<Player> winner;

    private int wait;
    private int wall;
    private int lava;

    public Match() {
        this.map = new MultiMap<>(new EnumMap<>(Rank.class));
        this.mapper = new HashMap<>();
    }

    public boolean check() {
        return lobby != null && wait > 0 && wall > 0 && lava > 0 && land.check();
    }

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

    public void addMember(Player p, Rank rank) {
        mapper.put(p, rank);
        map.put(rank, p);
    }

    public void tpToSpawn(Player p) {
        p.teleport(land.getArea(mapper.get(p)).getSpawn());
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

    public Location getLobby() {
        return lobby;
    }

    public void setLobby(Location lobby) {
        this.lobby = lobby;
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

    public MultiMap<Rank, Player> getMap() {
        return map;
    }

    public void setMap(MultiMap<Rank, Player> map) {
        this.map = map;
    }

    public Map<Player, Rank> getMapper() {
        return mapper;
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

package com.mengcraft.wallwar;

import com.mengcraft.wallwar.level.Land;
import com.mengcraft.wallwar.util.MultiMap;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created on 16-2-25.
 */
public class Match {

    private final MultiMap<Rank, Player> map;
    private final Map<Player, Rank> mapper;

    private final Set<Player> waiter;
    private final Set<Player> viewer;

    private Location lobby;
    private Main main;
    private Land land;
    private Rank rank;

    private int wait;
    private int wall;
    private int lava;

    private boolean running;
    private boolean end;

    public Match() {
        this.map = new MultiMap<>(new EnumMap<>(Rank.class));
        this.mapper = new HashMap<>();
        this.waiter = new HashSet<>();
        this.viewer = new HashSet<>();
    }

    public boolean check() {
        return lobby != null && wait > 0 && wall > 0 && lava > 0 && land.check();
    }

    public void checkUp() {
        HashSet<Rank> set = new HashSet<>(mapper.values());
        if (set.size() < 2) {
            set.forEach(rank -> {
                setRank(rank);
            });
            processEnd();
            setEnd(true);
        }
    }

    private void processEnd() {
        map.getMap().forEach((r, list) -> {
            if (getRank().equals(r)) {
                processWin(list);
            } else {
                processFail(list);
            }
        });
    }

    private static void processFail(List<Player> list) {
        list.forEach(p -> {
            p.setGameMode(GameMode.SPECTATOR);
            p.resetTitle();
            p.sendTitle(ChatColor.BLUE + "很可惜你失败了", ChatColor.YELLOW + "请等待传送大厅");
        });
    }

    private static void processWin(List<Player> list) {
        list.forEach(p -> {
            p.setGameMode(GameMode.SPECTATOR);
            p.resetTitle();
            p.sendTitle(ChatColor.BLUE + "赢得了最终胜利", ChatColor.YELLOW + "请等待传送大厅");
        });
    }

    public void clearUp(Player p) {
        if (mapper.containsKey(p)) {
            mapper.remove(p).addNumber(-1);
        } else if (viewer.contains(p)) {
            viewer.remove(p);
        } else {
            waiter.remove(p);
        }
    }

    public void addMember(Player p, Rank rank) {
        p.resetTitle();
        p.sendTitle(ChatColor.BLUE + "游戏已经开始了", ChatColor.YELLOW + "你的队伍是" + rank.getTag() + '队');

        mapper.put(p, rank);
        map.put(rank, p);

        rank.addNumber(1);
    }

    public void tpToSpawn(Player p) {
        p.teleport(land.getArea(mapper.get(p)).getSpawn());
    }

    public void addViewer(Player p) {
        p.teleport(land.getArea(Rank.NONE).getSpawn());
        p.setGameMode(GameMode.SPECTATOR);
        p.setHealth(p.getMaxHealth());

        viewer.add(p);
    }

    public void addWaiter(Player p) {
        p.resetTitle();
        p.sendTitle(ChatColor.BLUE + "你加入到排队中", ChatColor.YELLOW + "游戏正等待开始");

        p.teleport(lobby);
        p.setGameMode(GameMode.SURVIVAL);
        p.setFlying(false);

        waiter.add(p);
    }

    public Location getLobby() {
        return lobby;
    }

    public void setLobby(Location lobby) {
        this.lobby = lobby;
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

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public boolean isRunning() {
        return running;
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

    public void setMain(Main main) {
        this.main = main;
    }

    public void setLava(int lava) {
        this.lava = lava;
    }

    public int getLava() {
        return lava;
    }

    public Map<Player, Rank> getMapper() {
        return mapper;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public boolean isEnd() {
        return end;
    }

    public void setEnd(boolean end) {
        this.end = end;
    }

    public boolean isTouchMaxSize() {
        return waiter.size() >= land.getMaxSize();
    }

    public boolean isTouchMinSize() {
        return waiter.size() >= land.getMinSize();
    }

    public void load() {
        setLobby((Location) main.getConfig().get("match.lobby"));
        setWait(main.getConfig().getInt("match.time.wait"));
        setWall(main.getConfig().getInt("match.time.wall"));
        setLava(main.getConfig().getInt("match.time.lava"));
    }

    public void save() {
        main.getConfig().set("match.lobby", lobby);
        main.getConfig().set("match.time.wait", wait);
        main.getConfig().set("match.time.wall", wall);
        main.getConfig().set("match.time.lava", lava);
    }

    @Override
    public String toString() {
        return ("Match{" +
                "mapper=" + mapper +
                ", lobby=" + lobby +
                ", waiter=" + waiter +
                ", viewer=" + viewer +
                ", land=" + land +
                ", wait=" + wait +
                ", wall=" + wall +
                ", lava=" + lava +
                '}');
    }

}

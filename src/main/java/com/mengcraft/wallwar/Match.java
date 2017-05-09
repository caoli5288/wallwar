package com.mengcraft.wallwar;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.mengcraft.wallwar.entity.WallUser;
import com.mengcraft.wallwar.level.Land;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static com.mengcraft.wallwar.util.ListHelper.forEach;

/**
 * Created on 16-2-25.
 */
public class Match {

    private final Multimap<Rank, Player> map = HashMultimap.create();
    private final Map<Player, Rank> mapper = new ConcurrentHashMap<>();
    private final Set<Player> waiter = new HashSet<>();
    private final Set<Player> viewer = new HashSet<>();

    private Location lobby;
    private Main main;
    private Land land;
    private Rank rank;

    private int wait;
    private int wall;
    private int lava;

    private boolean running;
    private boolean end;

    public boolean check() {
        return lobby != null && wait > 0 && wall > 0 && lava > 0 && land.check();
    }

    public void checkEnd() {
        if (running && !end) {
            checkEnd(new HashSet<>(mapper.values()));
        }
    }

    private void checkEnd(HashSet<Rank> set) {
        if (set.size() < 2) {
            set.forEach(rank -> {
                setRank(rank);
            });
            processEnd();
            setEnd(true);
        }
    }

    private void processEnd() {
        map.asMap().forEach((r, list) -> {
            if (r.equals(rank)) {
                processWin(list);
            } else {
                processFail(list);
            }
        });
    }

    private void processFail(Collection<Player> list) {
        forEach(list, p -> p.isOnline(), p -> {
            p.setGameMode(GameMode.SPECTATOR);
            p.resetTitle();
            p.sendTitle(ChatColor.BLUE + "你在比赛中失败", ChatColor.YELLOW + "请等待传送大厅");

            p.sendMessage(ChatColor.BLUE + "你在比赛中失败");
            p.sendMessage(ChatColor.YELLOW + "请等待传送大厅");
        });
    }

    private void processWin(Collection<Player> list) {
        forEach(list, p -> p.isOnline(), p -> {
            p.setGameMode(GameMode.SPECTATOR);
            p.resetTitle();
            p.sendTitle(ChatColor.BLUE + "你在比赛中胜利", ChatColor.YELLOW + "请等待传送大厅");

            p.sendMessage(ChatColor.BLUE + "你在比赛中胜利");
            p.sendMessage(ChatColor.YELLOW + "请等待传送大厅");

            getUser(p).addWinning();
        });
    }

    public void cleanUp(Player p) {
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

        p.sendMessage(ChatColor.BLUE + "游戏已经开始了");
        p.sendMessage(ChatColor.YELLOW + "你的队伍是" + rank.getTag() + '队');

        p.getInventory().clear();

        mapper.put(p, rank);
        map.put(rank, p);

        getUser(p).addJoining();

        rank.addNumber(1);
    }

    public void tpToSpawn(Player p) {
        p.teleport(land.getSpawn(mapper.get(p)));
    }

    public void addViewer(Player p) {
        viewer.add(p);

        p.teleport(land.getSpawn(Rank.NONE));
        p.setGameMode(GameMode.SPECTATOR);
        p.setHealth(p.getMaxHealth());
    }

    public void addWaiter(Player p) {
        p.resetTitle();
        p.sendTitle(ChatColor.BLUE + "你加入到排队中", ChatColor.YELLOW + "游戏正等待开始");

        p.sendMessage(ChatColor.BLUE + "你加入到排队中");
        p.sendMessage(ChatColor.YELLOW + "游戏正等待开始");

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

    public Set<Player> getViewer() {
        return viewer;
    }

    public Land getLand() {
        return land;
    }

    public void setLand(Land land) {
        this.land = land;
    }

    public Rank getRank(Player p) {
        return mapper.containsKey(p) ? mapper.get(p) : Rank.NONE;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public boolean isRunning() {
        return running;
    }

    public boolean isArea(Block b) {
        return land.isArea(b.getLocation());
    }

    public boolean isWall(Block b) {
        return wall > 0 && land.isWall(b.getLocation());
    }

    public boolean isTeammate(Entity p, Entity other) {
        return mapper.get(p) == mapper.get(other);
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

    public synchronized boolean setRunning(boolean running) {
        if (this.running == running) return false;
        this.running = running;
        return true;
    }

    public boolean isEnd() {
        return end;
    }

    public void setEnd(boolean end) {
        this.end = end;
    }

    public Rank getRank() {
        return rank;
    }

    public boolean isTouchMaxSize() {
        return waiter.size() >= land.getMaxSize();
    }

    public boolean isTouchMinSize() {
        return waiter.size() >= land.getMinSize();
    }

    public String getMessage(String s) {
        return main.getConfig().getString("message." + s);
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

    public WallUser getUser(Player p) {
        return main.getUserMap().get(p.getUniqueId());
    }

    @Override
    public String toString() {
        return ("Match (" +
                "mapper=" + mapper +
                ", lobby=" + lobby +
                ", waiter=" + waiter +
                ", viewer=" + viewer +
                ", land=" + land +
                ", wait=" + wait +
                ", wall=" + wall +
                ", lava=" + lava +
                ')');
    }

    public boolean isNotRunning() {
        return !running;
    }

    public Collection<Player> getTeam(Rank rank) {
        return map.get(rank);
    }

}

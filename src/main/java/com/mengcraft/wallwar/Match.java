package com.mengcraft.wallwar;

import com.mengcraft.wallwar.entity.RankRoller;
import com.mengcraft.wallwar.entity.WallUser;
import com.mengcraft.wallwar.level.Land;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.mengcraft.wallwar.Main.nil;
import static com.mengcraft.wallwar.util.ListHelper.forEach;

/**
 * Created on 16-2-25.
 */
public class Match {

    private final Map<Player, Rank> living = new HashMap<>();
    private final Set<Player> waiter = new HashSet<>();

    private Location lobby;
    private Main main;
    private Land land;
    private Rank win;

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
            checkEnd(new HashSet<>(living.values()));
        }
    }

    private void checkEnd(HashSet<Rank> set) {
        if (set.size() < 2) {
            set.forEach(this::setWin);
            processEnd();
            setEnd(true);
        }
    }

    private void processEnd() {
        if (nil(win)) {
            for (Rank t : RankRoller.ALL) {
                processFail(t.getList());
            }
        } else {
            for (Rank t : RankRoller.ALL) {
                if (!(t == win)) processFail(t.getList());
            }
            processWin(win.getList());
        }
    }

    private void processFail(Collection<Player> list) {
        forEach(list, Player::isOnline, p -> {
            p.setGameMode(GameMode.SPECTATOR);
            p.resetTitle();
            p.sendTitle(ChatColor.BLUE + "你在比赛中失败", ChatColor.YELLOW + "请等待传送大厅");

            p.sendMessage(ChatColor.BLUE + "你在比赛中失败");
            p.sendMessage(ChatColor.YELLOW + "请等待传送大厅");
        });
    }

    private void processWin(Collection<Player> list) {
        forEach(list, Player::isOnline, p -> {
            p.setGameMode(GameMode.SPECTATOR);
            p.resetTitle();
            p.sendTitle(ChatColor.BLUE + "你在比赛中胜利", ChatColor.YELLOW + "请等待传送大厅");

            p.sendMessage(ChatColor.BLUE + "你在比赛中胜利");
            p.sendMessage(ChatColor.YELLOW + "请等待传送大厅");

            getUser(p).addWinning();
        });
    }

    public void cleanUp(Player p) {
        if (living.containsKey(p)) {
            living.remove(p).getLiving().remove(p);
        } else if (getViewer().contains(p)) {
            getViewer().remove(p);
        } else {
            waiter.remove(p);
        }
    }

    public void addPlayer(Player p, Rank rank) {
        p.resetTitle();
        p.sendTitle(ChatColor.BLUE + "游戏已经开始了", ChatColor.YELLOW + "你的队伍是" + rank.getTag() + '队');

        p.sendMessage(ChatColor.BLUE + "游戏已经开始了");
        p.sendMessage(ChatColor.YELLOW + "你的队伍是" + rank.getTag() + '队');

        p.getInventory().clear();

        living.put(p, rank);
        rank.add(p);

        getUser(p).addJoining();
    }

    public void tpToSpawn(Player p) {
        p.teleport(land.getSpawn(living.get(p)));
    }

    public void addViewer(Player p) {
        getViewer().add(p);

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
        return Rank.NONE.getList();
    }

    public Land getLand() {
        return land;
    }

    public void setLand(Land land) {
        this.land = land;
    }

    public Rank getRank(Player p) {
        return living.containsKey(p) ? living.get(p) : Rank.NONE;
    }

    public void setWin(Rank win) {
        this.win = win;
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
        return living.get(p) == living.get(other);
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

    public Map<Player, Rank> getLiving() {
        return living;
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

    public Rank getWin() {
        return win;
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
        return main.getIngame().get(p.getUniqueId());
    }

    @Override
    public String toString() {
        return ("Match (" +
                "mapper=" + living +
                ", lobby=" + lobby +
                ", waiter=" + waiter +
                ", viewer=" + getViewer() +
                ", land=" + land +
                ", wait=" + wait +
                ", wall=" + wall +
                ", lava=" + lava +
                ')');
    }

    public boolean isNotRunning() {
        return !running;
    }

}

package com.mengcraft.wallwar;

import com.mengcraft.wallwar.level.Land;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

/**
 * Created on 16-2-25.
 */
public class Match {

    private MemberSet waiter;
    private MemberSet viewer;
    private RankTable mapper;

    private Main main;
    private Land land;

    public void checkUp(Location loc) {
        Set<Rank> set = new HashSet<>(mapper.values());
        if (set.size() < 2) {
            for (Player p : viewer) {
                // 观战
            }
            for (Player p : mapper.keySet()) {
                // 胜者
            }
            main.getServer().getScheduler().runTaskLater(main, () -> {
                main.toLobby(viewer, mapper.keySet());
            }, 100);
            viewer.clear();
            mapper.clear();
        }
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

    public void addViewer(Player p) {
        if (p.getGameMode() != GameMode.SPECTATOR) {
            p.setGameMode(GameMode.SPECTATOR);
            p.setHealth(p.getMaxHealth());
        }
        p.teleport(getLand()
                .getArea(Rank.NONE)
                .getSpawn()
        );
        viewer.add(p);
    }

    public void addWaiter(Player p) {
        waiter.add(p);
    }

    public void setViewer(MemberSet viewer) {
        this.viewer = viewer;
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

    public boolean isFighter(Player p) {
        return mapper.get(p) != null;
    }

    public boolean isRunning() {
        return !mapper.isEmpty();
    }

}

package com.mengcraft.wallwar;

import com.mengcraft.wallwar.level.Land;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created on 16-2-25.
 */
public class Match {

    private Map<Player, Rank> mapping;

    private Set<Player> waiter;
    private Set<Player> viewer;
    private Set<Player> member;

    private Land land;
    private boolean b;

    public void dead(Player p) {

    }

    public void setViewer(Set<Player> viewer) {
        this.viewer = viewer;
    }

    public void setMember(Set<Player> member) {
        this.member = member;
    }

    public Set<Player> getViewer() {
        return viewer;
    }

    public Set<Player> getMember() {
        return member;
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

    public void setRunning(boolean running) {
        this.b = running;
    }

    public void addViewer(Player p) {
        viewer.add(p);
    }

    public void addWaiter(Player p) {
        waiter.add(p);
    }

    public boolean isRunning() {
        return b;
    }

}

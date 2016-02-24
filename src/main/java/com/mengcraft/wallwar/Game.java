package com.mengcraft.wallwar;

import org.bukkit.entity.Player;

import java.util.List;

/**
 * Created on 16-2-23.
 */
public class Game {

    private List<Player> list;
    private Land land;

    public List<Player> getList() {
        return list;
    }

    public Land getLand() {
        return land;
    }

    public void shutdown() {

    }
}

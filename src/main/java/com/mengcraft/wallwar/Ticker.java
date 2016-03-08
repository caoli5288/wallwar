package com.mengcraft.wallwar;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;

/**
 * Created on 16-2-25.
 */
public class Ticker implements Runnable {

    private Match match;
    private Main main;

    private int wait;
    private int wall;
    private int lava;

    @Override
    public void run() {
        if (match.isRunning()) {
            if (match.isEnd()) {
                processEndOf();
            } else {
                process();
            }
        } else {
            if (match.isTouchMaxSize()) {
                startMatch();
            } else {
                if (match.isTouchMinSize()) {
                    processStart();
                } else {
                    setWait(0);
                }
            }
        }
    }

    public void setWait(int wait) {
        this.wait = wait;
    }

    private void startMatch() {
        RankRoller roller = new RankRoller();
        match.getWaiter().forEach(p -> {
            match.addMember(p, roller.next());
            match.tpToSpawn(p);
            p.setGameMode(GameMode.SURVIVAL);
            p.setFlying(false);
        });
        match.setRunning(true);
        match.getWaiter().clear();
    }

    private void endOfMatch() {
        main.getServer().getOnlinePlayers().forEach(p -> {
            main.tpToLobby(p);
        });
        main.getServer().getScheduler().runTaskLater(main, () -> {
            main.getServer().shutdown();
        }, 10);
    }

    private void process() {
        if (wall > 0) {
            wall--;
            if (wall == 0) {
                match.getLand().boomWall();
                match.getMapper().keySet().forEach(p -> {
                    p.resetTitle();
                    p.sendTitle(ChatColor.BLUE + "战墙正开始倒塌", ChatColor.YELLOW + "岩浆将开始蔓延");

                });
            }
        } else if (lava > 0) {
            lava--;
            if (lava == 0) {
                match.getLand().bootLava();
            }
        } else {
            lava = match.getLava();
        }

    }

    public void setMatch(Match match) {
        this.match = match;
        this.wait = match.getWait();
        this.wall = match.getWall();
        this.lava = match.getLava();
    }

    public void setMain(Main main) {
        this.main = main;
    }

    private void processStart() {
        if (wait == 0) {
            wait = match.getWait();
        }
        wait--;
        if (wait == 0) {
            startMatch();
        }
    }

    private void processEndOf() {
        if (wait == 0) {
            wait = match.getWait();
        }
        wait--;
        if (wait == 0) {
            endOfMatch();
        }
    }

    @Override
    public String toString() {
        return ("Ticker{" +
                "match=" + match +
                ", wait=" + wait +
                ", wall=" + wall +
                ", lava=" + lava +
                '}');
    }

}

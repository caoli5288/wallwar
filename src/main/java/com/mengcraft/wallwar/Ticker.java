package com.mengcraft.wallwar;

import com.mengcraft.wallwar.entity.RankRoller;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;

/**
 * Created on 16-2-25.
 */
public class Ticker implements Runnable {

    private Match match;
    private Main main;

    private int wait;
    private int waitExit;
    private int lava;

    @Override
    public void run() {
        if (match.isRunning()) {
            if (match.isEnd()) {
                processEndOf();
            } else {
                process();
            }
        } else if (match.isTouchMaxSize()) {
            startMatch();
        } else if (match.isTouchMinSize()) {
            processStart();
        } else {
            setWait(match.getWait());
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
        if (match.getWall() > 0) {
            match.setWall(match.getWall() - 1);
            if (match.getWall() == 0) {
                match.getLand().boomWall();
                match.getMapper().keySet().forEach(p -> {
                    p.resetTitle();
                    p.sendTitle(ChatColor.BLUE + "战墙正开始倒塌", ChatColor.YELLOW + "岩浆将开始蔓延");
                });
            } else {
                // TODO
            }
        } else if (lava > 0) {
            lava--;
            if (lava == 0) {
                match.getLand().bootLava();
            } else {
                // TODO
            }
        } else {
            setLava(match.getLava());
        }
    }

    public void setLava(int lava) {
        this.lava = lava;
    }

    public void setMatch(Match match) {
        this.match = match;
        this.wait = match.getWait();
        this.lava = match.getLava();
    }

    public void setMain(Main main) {
        this.main = main;
    }

    private void processStart() {
        wait--;
        match.getWaiter().forEach(p -> {
            p.resetTitle();
            p.sendTitle(ChatColor.BLUE + "游戏马上就开始", ChatColor.YELLOW + "预计需等待" + wait + "秒");
        });
        if (wait == 0) {
            startMatch();
        }
    }

    public void setWaitExit(int waitExit) {
        this.waitExit = waitExit;
    }

    private void processEndOf() {
        waitExit--;
        if (waitExit == 0) {
            endOfMatch();
        }
    }

    @Override
    public String toString() {
        return ("Ticker{" +
                "match=" + match +
                ", wait=" + wait +
                ", lava=" + lava +
                '}');
    }

}

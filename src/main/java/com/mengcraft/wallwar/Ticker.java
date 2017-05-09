package com.mengcraft.wallwar;

import com.mengcraft.wallwar.entity.RankRoller;
import com.mengcraft.wallwar.util.Action;
import com.mengcraft.wallwar.util.Title;
import com.mengcraft.wallwar.util.TitleManager;
import lombok.val;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.potion.PotionEffect;

/**
 * Created on 16-2-25.
 */
public class Ticker implements Runnable {

    private final Match match;
    private final Main main;

    private int wait;
    private int waitExit;
    private int lava;
    private Action action;
    private TitleManager title;

    public Ticker(Main main, Match match) {
        this.match = match;
        this.main = main;
        wait = match.getWait();
        lava = match.getLava();
    }

    @Override
    public void run() {
        if (match.isRunning()) {
            if (match.isEnd()) {
                processEnd();
            } else {
                process();
            }
            tickViewer();
        } else {
            if (match.isTouchMaxSize()) {
                startMatch();
            } else if (match.isTouchMinSize()) {
                processStart();
            } else {
                setWait(match.getWait());
            }
            tickWaiter();
        }
    }

    private void tickWaiter() {
        // TODO
    }

    private void tickViewer() {
        match.getViewer().forEach(p -> {
            action.sendAction(p, ChatColor.translateAlternateColorCodes('&', "&3&l输入&e&l/lobby&3&l返回大厅"));
        });
    }

    public void setWait(int wait) {
        this.wait = wait;
    }

    private void startMatch() {
        if (match.setRunning(true)) {// 暂且的修复方案
            val roller = new RankRoller();
            for (val p : match.getWaiter()) {
                match.addPlayer(p, roller.next());
                p.setGameMode(GameMode.SURVIVAL);
                p.setFlying(false);
                for (PotionEffect e : p.getActivePotionEffects()) {
                    p.removePotionEffect(e.getType());
                }
                match.tpToSpawn(p);
            }
            Rank.up();
            match.getWaiter().clear();
        }
    }

    private void end() {
        main.getServer().getOnlinePlayers().forEach(main::tpToLobby);
        main.getServer().getScheduler().runTaskLater(main, () -> {
            main.getServer().shutdown();
        }, 25);
    }

    private void process() {
        if (match.getWall() > 0) {
            match.setWall(match.getWall() - 1);
            if (match.getWall() < 1) {
                match.getLand().boomWall();
                match.getLiving().keySet().forEach(p -> {
                    p.resetTitle();
                    p.sendTitle(ChatColor.BLUE + "战墙正开始倒塌", ChatColor.YELLOW + "岩浆将开始蔓延");
                });
            }
        } else if (lava > 0) {
            lava--;
            if (lava < 1) {
                match.getLand().bootLava();
            }
        } else {
            setLava(match.getLava());
        }
    }

    public void setLava(int lava) {
        this.lava = lava;
    }

    private void processStart() {
        wait--;
        Title t = new Title(
                ChatColor.BLUE + "游戏马上就开始",
                ChatColor.YELLOW + "预计需等待" + wait + "秒",
                0, 50, 10
        );
        match.getWaiter().forEach(p -> title.setTitle(p, t));
        if (wait < 1) {// 虽然不知道为什么但是有时候迷一样会小于0
            startMatch();
        }
    }

    public void setWaitExit(int waitExit) {
        this.waitExit = waitExit;
    }

    private void processEnd() {
        waitExit--;
        if (waitExit < 1) {
            end();
        }
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public void setTitle(TitleManager title) {
        this.title = title;
    }
}

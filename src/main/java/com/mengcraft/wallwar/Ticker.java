package com.mengcraft.wallwar;

import com.mengcraft.wallwar.entity.RankRoller;
import com.mengcraft.wallwar.util.Action;
import com.mengcraft.wallwar.util.Title;
import com.mengcraft.wallwar.util.TitleManager;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;

import static com.mengcraft.wallwar.util.CollectionUtil.forEach;

/**
 * Created on 16-2-25.
 */
public class Ticker implements Runnable {

    private Match match;
    private Main main;

    private int wait;
    private int waitExit;
    private int lava;
    private Action action;
    private TitleManager title;

    @Override
    public void run() {
        if (match.isRunning()) {
            if (match.isEnd()) {
                processEndOf();
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
            }
            tickMember();
        } else if (lava > 0) {
            lava--;
            if (lava == 0) {
                match.getLand().bootLava();
            }
        } else {
            setLava(match.getLava());
        }
    }

    private void tickMember() {
        forEach(match.getMapper().keySet(), p -> !match.isRanked(p, p.getLocation()), p -> {
            match.tpToSpawn(p);
        });
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
        Title t = new Title(
                ChatColor.BLUE + "游戏马上就开始",
                ChatColor.YELLOW + "预计需等待" + wait + "秒",
                0, 50, 10
        );
        match.getWaiter().forEach(p -> title.setTitle(p, t));
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

    public void setAction(Action action) {
        this.action = action;
    }

    public void setTitle(TitleManager title) {
        this.title = title;
    }
}

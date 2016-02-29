package com.mengcraft.wallwar;

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
            if (match.hasFinish()) {
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
        });
    }

    private void endOfMatch() {
        main.getServer().getOnlinePlayers().forEach(p -> {
            main.tpToLobby(p);
        });
        main.getServer().shutdown();
    }

    private void process() {
        if (wall > 0) {
            wall--;
            if (wall == 0) {
                match.getLand().boomWall();
            }
        } else {
            if (lava > 0) {
                lava--;
                if (lava == 0) {
                    match.getLand().bootLava();
                }
            } else {
                lava = match.getLand().getLava();
            }
        }
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public Main getMain() {
        return main;
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

}

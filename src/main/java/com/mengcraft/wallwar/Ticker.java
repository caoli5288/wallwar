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

    public int getWait() {
        return wait;
    }

    public void setWait(int wait) {
        this.wait = wait;
    }

    private void startMatch() {

    }

    private void endOfMatch() {

    }

    private void lavaBootUp() {
        match.getLand().lavaBootUp();
    }

    private void process() {

    }

    private void processLavaBootUp() {
        if (lava == 0) {
            lava = match.getLava();
        }
        lava--;
        if (lava == 0) {
            lavaBootUp();
        }
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

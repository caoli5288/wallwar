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
            if (match.getWinner().size() > 0) {
                processEndOfMatch();
            } else {
                processWallOrLava();
            }
        } else {
            if (match.isTouchMaxSize()) {
                startMatch();
            } else {
                if (match.isTouchMinSize()) {
                    processStartMatch();
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

    }

    private void processWallOrLava() {

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

    private void processStartMatch() {
        if (wait == 0) {
            wait = match.getWait();
        }
        wait--;
        if (wait == 0) {
            startMatch();
        }
    }

    private void processEndOfMatch() {
        if (wait == 0) {
            wait = match.getWait();
        }
        wait--;
        if (wait == 0) {
            endOfMatch();
        }
    }

}

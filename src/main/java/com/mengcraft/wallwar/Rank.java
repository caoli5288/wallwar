package com.mengcraft.wallwar;

/**
 * Created on 16-2-25.
 */
public enum Rank {

    BLUE, AQUA, GRAY, GOLD, NONE;

    private int number;

    public int getNumber() {
        return number;
    }

    public int addNumber(int i) {
        return number += i;
    }

}

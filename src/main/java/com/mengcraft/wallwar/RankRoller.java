package com.mengcraft.wallwar;

/**
 * Created on 16-2-29.
 */
public class RankRoller {

    public static final Rank[] RANK_ARRAY = {
            Rank.BLUE,
            Rank.AQUA,
            Rank.GRAY,
            Rank.GOLD,
    };

    private int cursor;

    public Rank next() {
        if (cursor > 3) {
            cursor = 0;
        }
        return RANK_ARRAY[cursor++];
    }

}

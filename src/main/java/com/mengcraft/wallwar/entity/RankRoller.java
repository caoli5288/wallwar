package com.mengcraft.wallwar.entity;

import com.mengcraft.wallwar.Rank;

/**
 * Created on 16-2-29.
 */
public class RankRoller {

    public static final Rank[] RANK_ARRAY = {
            Rank.BLUE,
            Rank.GREEN,
            Rank.RED,
            Rank.YELLOW,
    };

    private int cursor;

    public Rank next() {
        if (cursor > 3) {
            cursor = 0;
        }
        return RANK_ARRAY[cursor++];
    }

}

package com.mengcraft.wallwar.scoreboard;

import java.util.List;

/**
 * Created on 16-5-17.
 */
public class ListedLine implements Line {

    private final List<String> list;
    private int cursor;

    public ListedLine(List<String> list) {
        this.list = list;
    }

    @Override
    public String getText() {
        if (cursor < list.size()) {
            return list.get(cursor++);
        }
        cursor = 0;
        return list.get(cursor++);
    }

    public List<String> getList() {
        return list;
    }

}

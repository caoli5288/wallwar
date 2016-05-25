package com.mengcraft.wallwar.scoreboard;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 16-5-18.
 */
public abstract class FixedBodyHandler implements LineBuilder {

    private List<Line> list;

    @Override
    public List<Line> getList() {
        list = new ArrayList<>();
        update();
        return list;
    }

    protected void append(Line line) {
        list.add(line);
    }

    protected void append(String line) {
        list.add(TextLine.of(line));
    }

    protected abstract void update();

}

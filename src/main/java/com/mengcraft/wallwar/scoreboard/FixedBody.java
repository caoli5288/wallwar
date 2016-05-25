package com.mengcraft.wallwar.scoreboard;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

/**
 * Created on 16-5-17.
 */
public class FixedBody implements Body {

    private final LineBuilder builder;

    private FixedBody(LineBuilder builder) {
        this.builder = builder;
    }

    @Override
    public List<LinePair> getList() {
        List<Line> list = builder.getList();
        int size = list.size();
        List<LinePair> output = new ArrayList<>(size);
        for (Line line : list) {
            output.add(LinePair.of(line, size--));
        }
        return output;
    }

    public static FixedBody of(LineBuilder body) {
        return new FixedBody(body);
    }

    public static FixedBody of(List<Line> list) {
        return new FixedBody(() -> list);
    }

    public static FixedBody of(Line... list) {
        return new FixedBody(() -> asList(list));
    }

}

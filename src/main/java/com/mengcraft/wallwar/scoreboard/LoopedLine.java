package com.mengcraft.wallwar.scoreboard;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 16-5-19.
 */
public class LoopedLine extends ListedLine {

    protected LoopedLine(String in, int blank) {
        super(format(in, blank));
    }

    private static List<String> format(String in, int blank) {
        List<String> out = new ArrayList<>();
        for (int i = 0; i != blank; i++) {
            out.add(blank(i) + in + blank(blank - i));
        }
        for (int i = blank; i != 0; i--) {
            out.add(blank(i) + in + blank(blank - i));
        }
        return out;
    }

    private static String blank(int i) {
        String out = "";
        for (int j = 0; j < i; j++) {
            out += ' ';
        }
        return out;
    }

    public static LoopedLine of(String in, int blank) {
        return new LoopedLine(in, blank);
    }

}

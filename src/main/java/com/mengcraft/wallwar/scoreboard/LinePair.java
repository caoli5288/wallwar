package com.mengcraft.wallwar.scoreboard;

/**
 * Created on 16-5-16.
 */
public class LinePair {

    private final Line line;
    private final int score;

    private LinePair(Line line, int score) {
        this.line = line;
        this.score = score;
    }

    public Line getLine() {
        return line;
    }

    public int getScore() {
        return score;
    }

    public String getText() {
        return line.getText();
    }

    public static LinePair of(Line line, int i) {
        return new LinePair(line, i);
    }

}

package com.mengcraft.wallwar.scoreboard;

/**
 * Created on 16-5-16.
 */
public class TextLine implements Line {

    private final String text;

    private TextLine(String text) {
        this.text = text;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return text;
    }

    public static Line of(String text) {
        return new TextLine(text);
    }

}

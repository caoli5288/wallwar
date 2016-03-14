package com.mengcraft.wallwar.util;

/**
 * Created on 16-3-13.
 */
public class Title {

    public final String title;
    public final String sub;

    public int fadeIn;
    public int fadeOut;
    public int display;

    public Title(String title, String sub, int fadeIn, int display, int fadeOut) {
        this.title = title;
        this.sub = sub;
        this.fadeIn = fadeIn;
        this.display = display;
        this.fadeOut = fadeOut;
    }

    public Title(String title, String sub) {
        this.title = title;
        this.sub = sub;
    }

    public Title setFadeIn(int fadeIn) {
        this.fadeIn = fadeIn;
        return this;
    }

    public Title setFadeOut(int fadeOut) {
        this.fadeOut = fadeOut;
        return this;
    }

    public Title setDisplay(int display) {
        this.display = display;
        return this;
    }

}

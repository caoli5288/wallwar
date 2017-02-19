package com.mengcraft.wallwar.scoreboard;

import org.bukkit.ChatColor;

import java.util.Objects;

import static org.bukkit.ChatColor.RESET;

/**
 * Created on 16-5-18.
 */
public class Format {

    private String format;
    private String color;

    private String getFormat() {
        if (format == null) {
            return "";
        }
        return format;
    }

    private String getColor() {
        if (color == null) {
            return "";
        }
        return color;
    }

    public String get() {
        if (format == null && color == null) {
            return "§r";
        }
        return getFormat() + getColor();
    }

    public void set(ChatColor in) {
        if (Objects.equals(in, RESET)) {
            format = null;
            color = null;
        } else if (in.isFormat()) {
            format = in.toString();
        } else if (in.isColor()) {
            color = in.toString();
        }
    }

}

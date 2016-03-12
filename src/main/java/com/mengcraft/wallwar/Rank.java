package com.mengcraft.wallwar;

import org.bukkit.ChatColor;

/**
 * Created on 16-2-25.
 */
public enum Rank {

    NONE(ChatColor.WHITE, "观"),
    BLUE(ChatColor.AQUA, "蓝"),
    GREEN(ChatColor.GREEN, "绿"),
    RED(ChatColor.RED, "红"),
    YELLOW(ChatColor.YELLOW, "黄");

    private final ChatColor colour;
    private final String tag;

    private int number;

    Rank(ChatColor colour, String tag) {
        this.colour = colour;
        this.tag = tag;
    }

    public int getNumber() {
        return number;
    }

    public int addNumber(int i) {
        return number += i;
    }

    public ChatColor getColour() {
        return colour;
    }

    public static Rank getRank(int i) {
        return values()[i];
    }

    public String getTag() {
        return tag;
    }

}

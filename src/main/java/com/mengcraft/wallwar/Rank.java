package com.mengcraft.wallwar;

import org.bukkit.ChatColor;

/**
 * Created on 16-2-25.
 */
public enum Rank {

    NONE(ChatColor.BOLD, "观"),
    BLUE(ChatColor.BLUE, "蓝"),
    AQUA(ChatColor.AQUA, "绿"),
    GRAY(ChatColor.GRAY, "灰"),
    GOLD(ChatColor.GOLD, "黄");

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

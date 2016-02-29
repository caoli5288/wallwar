package com.mengcraft.wallwar;

import org.bukkit.ChatColor;

/**
 * Created on 16-2-25.
 */
public enum Rank {

    BLUE(ChatColor.BLUE),
    AQUA(ChatColor.AQUA),
    GRAY(ChatColor.GRAY),
    GOLD(ChatColor.GOLD),
    NONE(ChatColor.BOLD);

    Rank(ChatColor colour) {
        this.colour = colour;
    }

    private ChatColor colour;
    private int number;

    public int getNumber() {
        return number;
    }

    public int addNumber(int i) {
        return number += i;
    }

    public ChatColor getColour() {
        return colour;
    }

}

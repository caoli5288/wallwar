package com.mengcraft.wallwar;

import com.mengcraft.wallwar.entity.RankRoller;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

/**
 * Created on 16-2-25.
 */
@Getter
public enum Rank {

    NONE(ChatColor.WHITE, "观"),
    BLUE(ChatColor.AQUA, "蓝"),
    GREEN(ChatColor.GREEN, "绿"),
    RED(ChatColor.RED, "红"),
    YELLOW(ChatColor.YELLOW, "黄");

    private final Set<Player> list;
    private final ChatColor colour;
    private final String tag;
    private Set<Player> living;

    Rank(ChatColor colour, String tag) {
        this.colour = colour;
        this.tag = tag;
        list = new HashSet<>();
        living = list;
    }

    public void add(Player p) {
        list.add(p);
    }

    public static void up() {
        for (Rank i : RankRoller.ALL) {
            i.living = new HashSet<>(i.list);
        }
    }

    public static Rank getById(int i) {
        return values()[i];
    }

}

package com.mengcraft.wallwar.scoreboard;

import org.bukkit.ChatColor;
import org.junit.Test;

/**
 * Created on 16-5-18.
 */
public class LightedLineTest {

    @Test
    public void of() {
        for (String j : LightedLine.of("纯文字测试", ChatColor.GOLD).getList()) {
            System.out.println(j);
        }
        for (String j : LightedLine.of("§a颜色测试", ChatColor.GOLD).getList()) {
            System.out.println(j);
        }
    }

}
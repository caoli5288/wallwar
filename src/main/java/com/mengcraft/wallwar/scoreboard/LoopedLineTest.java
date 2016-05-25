package com.mengcraft.wallwar.scoreboard;

import org.junit.Test;

/**
 * Created on 16-5-19.
 */
public class LoopedLineTest {

    @Test
    public void of() throws Exception {
        for (String j : LoopedLine.of("测试文字", 6).getList()) {
            System.out.println(j);
        }
    }

}
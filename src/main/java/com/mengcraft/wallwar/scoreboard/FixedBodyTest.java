package com.mengcraft.wallwar.scoreboard;

import org.junit.Test;

import java.util.Arrays;

/**
 * Created on 16-5-18.
 */
public class FixedBodyTest {

    @Test
    public void of() throws Exception {
        FixedBody.of(Arrays.asList(
                TextLine.of("" + System.nanoTime()),
                TextLine.of("" + System.nanoTime())
        )); // With pre-build line list, all line was static
    }

    @Test
    public void of1() throws Exception {
        FixedBody.of(Arrays.asList(
                TextLine.of("" + System.nanoTime()),
                () -> "" + System.nanoTime()
        )); // With pre-build line list, include a dynamic line
    }

    @Test
    public void of2() throws Exception {
        FixedBody.of(() -> Arrays.asList(
                TextLine.of("" + System.nanoTime()),
                TextLine.of("" + System.nanoTime())
        ));// With lister, build line list dynamic
    }

}
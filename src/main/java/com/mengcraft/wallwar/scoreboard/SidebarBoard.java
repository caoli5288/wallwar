package com.mengcraft.wallwar.scoreboard;

import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 16-5-17.
 */
public class SidebarBoard extends Board {

    private final Objective objective;
    private Body body;
    private Line head;
    private List<String> list;

    private SidebarBoard(Plugin plugin) {
        super(plugin);
        objective = getBoard().registerNewObjective("board", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    @Override
    public void update() {
        // Any other way update board will led screen flicker
        List<String> list1 = list;
        if (head != null) {
            objective.setDisplayName(head.getText());
        }

        list = new ArrayList<>();
        String line;
        for (LinePair pair : body.getList()) {
            line = pair.getText();
            list.add(line);
            if (list1 != null) {
                list1.remove(line);
            }
            objective.getScore(line).setScore(pair.getScore());
        }

        if (list1 != null) {
            list1.forEach(j -> getBoard().resetScores(j));
        }
    }

    public void setHead(Line head) {
        this.head = head;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public static SidebarBoard of(Plugin plugin) {
        return new SidebarBoard(plugin);
    }

}

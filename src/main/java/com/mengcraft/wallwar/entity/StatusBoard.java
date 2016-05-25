package com.mengcraft.wallwar.entity;

import com.mengcraft.wallwar.Match;
import com.mengcraft.wallwar.Rank;
import com.mengcraft.wallwar.scoreboard.FixedBodyHandler;
import com.mengcraft.wallwar.scoreboard.LightedLine;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Created on 16-3-9.
 */
public class StatusBoard extends FixedBodyHandler {

    private final Match match;
    private final LightedLine tail;
    private final Player p;

    public StatusBoard(Match match, Player p) {
        this.p = p;
        this.match = match;
        this.tail = LightedLine.of(match.getMessage("scoreboard.tail"), ChatColor.GOLD);
    }

    @Override
    public void update() {
        if (match.isRunning()) {
            if (match.isEnd()) {
                createEndEntry();
            } else {
                createRunningEntry(match.getRank(p));
            }
        } else {
            createWaitEntry(match.getUser(p));
        }
    }

    private void createRunningEntry(Rank rank) {
        append("§r");
        append("§6你的队伍: " + (rank != Rank.NONE ? rank.getColour() + rank.getTag() + '队' : "观众"));
        append("§r§r");
        append("§6地图: " + match.getMessage("scoreboard.match"));
        append("§6存活: " + match.getMapper().size());
        append("§6观战: " + match.getViewer().size());
        append("§c红队: " + Rank.RED.getNumber());
        append("§e黄队: " + Rank.YELLOW.getNumber());
        append("§b蓝队: " + Rank.BLUE.getNumber());
        append("§a绿队: " + Rank.GREEN.getNumber());
        append("§r§r");
        append("§6战墙倒塌: " + match.getWall());
        append("§6岩浆高度: " + match.getLand().getLava());
        append("§r§r§r");
        append(tail.getText());
    }

    private void createEndEntry() {
        append("§r");
        append("§7========");
        append("§r§r");
        append("§3获胜队伍: " + match.getRank().getColour() + match.getRank().getTag() + '队');
        append("§r§r§r");
        append("§7========");
        append("§r§r§r§r");
        append(tail.getText());
    }

    private void createWaitEntry(WallUser user) {
        append("§r");
        append("§e场次: " + (user != null ? user.getJoining() : 0));
        append("§e胜场: " + (user != null ? user.getWinning() : 0));
        append("§e击杀: " + (user != null ? user.getKilled() : 0));
        append("§e死亡: " + (user != null ? user.getDead() : 0));
        append("§r§r");
        append("§6地图: " + match.getMessage("scoreboard.match"));
        append("§e玩家: " + match.getWaiter().size());
        append("§6状态: " + (match.getWaiter().size() < match.getLand().getMinSize() ? "§6等待排队" : "§c即将开始"));
        append("§r§r§r");
        append(tail.getText());
    }

}

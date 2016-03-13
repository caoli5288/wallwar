package com.mengcraft.wallwar.entity;

import com.mengcraft.wallwar.Match;
import com.mengcraft.wallwar.Rank;
import me.tigerhix.lib.scoreboard.common.EntryBuilder;
import me.tigerhix.lib.scoreboard.common.animate.HighlightedString;
import me.tigerhix.lib.scoreboard.type.Entry;
import me.tigerhix.lib.scoreboard.type.ScoreboardHandler;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Created on 16-3-9.
 */
public class StatusBoard implements ScoreboardHandler {

    private final Match match;
    private final HighlightedString tail;

    public StatusBoard(Match match) {
        this.match = match;
        this.tail = new HighlightedString(match.getMessage("scoreboard.tail"), "&6", "&e");
    }

    @Override
    public String getTitle(Player p) {
        return String.format(match.getMessage("scoreboard.title"), p.getName());
    }

    @Override
    public List<Entry> getEntries(Player p) {
        if (match.isRunning()) {
            if (match.isEnd()) {
                return createEndEntry();
            } else {
                return createRunningEntry(match.getRank(p));
            }
        } else {
            return createWaitEntry(match.getUser(p));
        }
    }

    private List<Entry> createRunningEntry(Rank rank) {
        EntryBuilder builder = new EntryBuilder();
        builder.next("");
        builder.next("&6你的队伍: " + (rank != Rank.NONE ? rank.getColour() + rank.getTag() + '队' : "观众"));
        builder.next("");
        builder.next("&6地图: " + match.getMessage("scoreboard.match"));
        builder.next("&6存活: " + match.getMapper().size());
        builder.next("&6观战: " + match.getViewer().size());
        builder.next("&c红队: " + Rank.RED.getNumber());
        builder.next("&e黄队: " + Rank.YELLOW.getNumber());
        builder.next("&b蓝队: " + Rank.BLUE.getNumber());
        builder.next("&a绿队: " + Rank.GREEN.getNumber());
        builder.next("");
        builder.next("&6战墙倒塌: " + match.getWall());
        builder.next("&6岩浆高度: " + match.getLand().getLava());
        builder.next("");
        builder.next(tail.next());

        return builder.build();
    }

    private List<Entry> createEndEntry() {
        EntryBuilder builder = new EntryBuilder();
        builder.next("");
        builder.next("&7========");
        builder.blank();
        builder.next("&3获胜队伍: " + match.getRank().getColour() + match.getRank().getTag() + '队');
        builder.next("");
        builder.next("&7========");
        builder.next("");
        builder.next(tail.next());

        return builder.build();
    }

    private List<Entry> createWaitEntry(WallUser user) {
        EntryBuilder builder = new EntryBuilder();
        builder.next("");
        builder.next("&e场次: " + (user != null ? user.getJoining() : 0));
        builder.next("&e胜场: " + (user != null ? user.getWinning() : 0));
        builder.next("&e击杀: " + (user != null ? user.getKilled() : 0));
        builder.next("&e死亡: " + (user != null ? user.getDead() : 0));
        builder.next("");
        builder.next("&6地图: " + match.getMessage("scoreboard.match"));
        builder.next("&e玩家: " + match.getWaiter().size());
        builder.next("&6状态: " + (match.getWaiter().size() < match.getLand().getMinSize() ? "&6等待排队" : "&c即将开始"));
        builder.next("");
        builder.next(tail.next());

        return builder.build();
    }

}

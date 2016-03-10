package com.mengcraft.wallwar;

import me.tigerhix.lib.scoreboard.ScoreboardLib;
import me.tigerhix.lib.scoreboard.type.Scoreboard;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Created on 16-2-25.
 */
public class Executor implements Listener {

    private StatusBoard board;
    private Match match;
    private Main main;

    @EventHandler
    public void handle(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        if (match.isRunning()) {
            match.addViewer(p);
        } else {
            match.addWaiter(p);
        }
        main.runTask(() -> {
            WallUser user = main.getDatabase().find(WallUser.class, p.getUniqueId());
            if (user != null) {
                main.getUserMap().put(user.getId(), user);
            } else {
                main.createUser(p);
            }
        });
        Scoreboard scoreboard = ScoreboardLib.createScoreboard(p);
        scoreboard.setHandler(board)
                .setUpdateInterval(10);
        scoreboard.activate();
    }

    @EventHandler
    public void handle(PlayerDeathEvent event) {
        Player p = event.getEntity();
        if (match.isRunning()) {
            match.addViewer(p);
        }
        match.clearUp(p);
        match.checkUp();

        match.getUser(p).addDead();
        if (p.getKiller() != null) {
            match.getUser(p.getKiller()).addKilled();
        }

        p.resetTitle();
        p.sendTitle(ChatColor.BLUE + "你在游戏中死亡", ChatColor.YELLOW + "你进入观战模式");

        p.sendMessage(ChatColor.BLUE + "你在游戏中死亡");
        p.sendMessage(ChatColor.YELLOW + "你进入观战模式");
    }

    @EventHandler
    public void handle(PlayerQuitEvent event) {
        Player p = event.getPlayer();
        if (p.getLocation().getWorld() != match.getLobby().getWorld()) {
            p.teleport(match.getLobby());
        }
        match.clearUp(p);
        match.checkUp();
    }

    @EventHandler
    public void handle(EntityDamageEvent event) {
        if (match.isNotRunning()) {
            event.setDamage(0);
        }
    }

    @EventHandler
    public void handle(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            event.setCancelled(match.isSameRank(event.getEntity(), event.getDamager()));
        }
    }

    @EventHandler
    public void handle(BlockBreakEvent event) {
        if (!match.isRankArea(event.getPlayer(), event.getBlock().getLocation())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void handle(BlockPlaceEvent event) {
        if (!match.isRankArea(event.getPlayer(), event.getBlock().getLocation())) {
            event.setCancelled(true);
        }
    }

    public void setBoard(StatusBoard board) {
        this.board = board;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public void setMain(Main main) {
        this.main = main;
    }

}

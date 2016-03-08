package com.mengcraft.wallwar;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Created on 16-2-25.
 */
public class Executor implements Listener {

    private Ticker ticker;
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
        // 更新积分板信息
    }

    @EventHandler
    public void handle(PlayerDeathEvent event) {
        Player p = event.getEntity();
        if (match.isRunning()) {
            match.addViewer(p);
        }
        match.clearUp(p);
        match.checkUp();

        p.resetTitle();
        p.sendTitle(ChatColor.BLUE + "你在游戏中死亡", ChatColor.YELLOW + "你进入观战模式");

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

    public Ticker getTicker() {
        return ticker;
    }

    public void setTicker(Ticker ticker) {
        this.ticker = ticker;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

}

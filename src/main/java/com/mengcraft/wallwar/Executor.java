package com.mengcraft.wallwar;

import org.bukkit.GameMode;
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
        if (p.getGameMode()!= GameMode.SPECTATOR) {
            p.setGameMode(GameMode.SPECTATOR);
            p.setHealth(p.getMaxHealth());
        }
        if (match.isRunning()) {
            match.addViewer(p);
        } else {
            match.addWaiter(p);
        }
        // 更新积分板信息
    }

    @EventHandler
    public void handle(PlayerDeathEvent event) {
        match.clearUp(event.getEntity());
        match.checkUp();
    }

    @EventHandler
    public void handle(PlayerQuitEvent event) {
        Player p = event.getPlayer();
        if (p.getLocation().getWorld() != main.getLobby().getWorld()) {
            p.teleport(main.getLobby());
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

}

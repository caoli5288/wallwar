package com.mengcraft.wallwar;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Created on 16-2-25.
 */
public class Executor implements Listener {

    private Location lobby;
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
        match.clearUp(event.getEntity());
        match.addViewer(event.getEntity());
        match.checkUp(lobby);
    }

    @EventHandler
    public void handle(PlayerQuitEvent event) {
        Player p = event.getPlayer();
        if (p.getLocation().getWorld() != lobby.getWorld()) {
            p.teleport(lobby);
        }
        match.clearUp(p);
        match.checkUp(lobby);
    }

}

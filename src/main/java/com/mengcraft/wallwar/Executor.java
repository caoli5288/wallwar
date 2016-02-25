package com.mengcraft.wallwar;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import static com.mengcraft.wallwar.Rank.NONE;
import static org.bukkit.GameMode.SPECTATOR;

/**
 * Created on 16-2-25.
 */
public class Executor implements Listener {

    private Location lobby;
    private Match match;

    @EventHandler
    public void handle(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        p.setGameMode(SPECTATOR);
        p.setHealth(p.getMaxHealth());
        p.teleport(lobby);

        if (match.isRunning()) {
            p.teleport(match.getLand().
                    getArea(NONE).
                    getSpawn()
            );
            match.addViewer(p);
        } else {
            match.addWaiter(p);
        }
        // 更新积分板信息
    }

    @EventHandler
    public void handle(PlayerQuitEvent event) {
        Player p = event.getPlayer();
        if (match.isRunning()) {
            match.dead(p);
        }
    }

}

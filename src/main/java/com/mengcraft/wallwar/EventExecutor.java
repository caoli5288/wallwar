package com.mengcraft.wallwar;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Created on 16-2-24.
 */
public class EventExecutor implements Listener {

    private Manager manager = new Manager();

    @EventHandler
    public void handle(PlayerQuitEvent event) {
        if (manager.isFighting(event.getPlayer())) {
            manager.setFailure(event.getPlayer());
        }
    }

}

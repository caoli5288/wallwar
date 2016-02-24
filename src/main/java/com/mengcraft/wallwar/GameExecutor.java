package com.mengcraft.wallwar;

import com.mengcraft.wallwar.manager.TeamManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import static com.mengcraft.wallwar.Team.VIEWER;
import static org.bukkit.GameMode.SPECTATOR;

/**
 * Created on 16-2-24.
 */
public class GameExecutor implements Listener {

    private TeamManager manager = new TeamManager();

    @EventHandler
    public void handle(PlayerDeathEvent event) {
        Player p = event.getEntity();
        if (manager.getGameSize(p) != 2) {
            manager.getGame(p).
                    getList().
                    remove(p);
            manager.setTeam(p, null);
            manager.setGame(p, null);
            p.teleport(manager.getGame(p).
                    getLand().
                    getArea(VIEWER).
                    getSpawn()
            );
            p.setHealth(p.getMaxHealth());
            p.setGameMode(SPECTATOR);
        } else {
            manager.endGame(p);
        }
    }

}

package com.mengcraft.wallwar.manager;

import com.mengcraft.wallwar.Game;
import com.mengcraft.wallwar.Team;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created on 16-2-24.
 */
public class TeamManager {

    private Map<UUID, Team> teamMap = new HashMap<>();
    private Map<UUID, Game> gameMap = new HashMap<>();

    public Team getTeam(Player p) {
        return teamMap.get(p.getUniqueId());
    }

    public Game getGame(Player p) {
        return gameMap.get(p.getUniqueId());
    }

    public void setTeam(Player p, Team team) {
        if (team == null) {
            teamMap.remove(p.getUniqueId());
        } else {
            teamMap.put(p.getUniqueId(), team);
        }
    }

    public void setGame(Player p, Game game) {
        if (game == null) {
            gameMap.remove(p.getUniqueId());
        } else {
            gameMap.put(p.getUniqueId(), game);
        }
    }

    public boolean hasTeam(Player p) {
        return teamMap.containsKey(p.getUniqueId());
    }

    public int getGameSize(Player p) {
        return getGame(p).getList().size();
    }

    public void endGame(Player p) {
        // TODO
    }
}

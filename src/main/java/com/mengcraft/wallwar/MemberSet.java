package com.mengcraft.wallwar;

import org.bukkit.entity.Player;

import java.net.ServerSocket;
import java.util.HashSet;
import java.util.Set;

/**
 * Created on 16-2-25.
 */
public class MemberSet {

    private final Set<Player> liveSet = new HashSet<>();
    private final Set<Player> deadSet = new HashSet<>();

    public Set<Player> getLiveSet() {
        return liveSet;
    }

    public Set<Player> getDeadSet() {
        return deadSet;
    }

}

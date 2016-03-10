package com.mengcraft.wallwar.level;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created on 16-3-10.
 */
public class LavaDriver extends BukkitRunnable {

    private final AreaIterator it;

    public LavaDriver(AreaIterator it) {
        this.it = it;
    }

    @Override
    public void run() {
        if (it.hasNext()) {
            for (int i = 0; it.hasNext() && i != 32; i++) {
                process(it.next().getBlock());
            }
        } else {
            cancel();
        }
    }

    private void process(Block next) {
        if (next.getType() != Material.BEDROCK) {
            next.setType(Material.LAVA);
        }
    }

}

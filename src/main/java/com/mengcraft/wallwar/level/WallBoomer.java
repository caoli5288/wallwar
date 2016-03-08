package com.mengcraft.wallwar.level;

import com.mengcraft.wallwar.util.SetPicker;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created on 16-3-8.
 */
public class WallBoomer extends BukkitRunnable {

    private final Random random = ThreadLocalRandom.current();
    private final SetPicker<Location> it;

    public WallBoomer(Collection<Location> collection) {
        this.it = new SetPicker<>(collection, random);
    }

    @Override
    public void run() {
        if (it.hasNext()) {
            process(it.next());
        } else {
            cancel();
        }
    }

    private void process(Location next) {
        next.getWorld().playEffect(next, Effect.EXPLOSION_LARGE, 1);
        next.getWorld().playEffect(next, Effect.FIREWORKS_SPARK, 1);
        if (random.nextFloat() < 0.25) {
            next.getWorld().playSound(next, Sound.EXPLODE, 1, 1);
        }
        next.getBlock().setType(Material.AIR);
    }

}

package com.mengcraft.wallwar.level;

import com.mengcraft.wallwar.util.SetPicker;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created on 16-3-8.
 */
public class WallBoomer extends BukkitRunnable {

    private final Random random = ThreadLocalRandom.current();
    private final Area area;

    private SetPicker<Location> picker;

    public WallBoomer(Area area) {
        this.area = area;
    }

    @Override
    public void run() {
        if (picker != null && picker.hasNext()) {
            process(picker.next());
        } else {
            startUp(area.getList(loc -> loc.getBlock().getType() != Material.AIR));
        }
    }

    private void startUp(List<Location> list) {
        if (list.size() != 0) {
            setPicker(new SetPicker<>(list, random));
        } else {
            cancel();
        }
    }

    private void process(Location next) {
        if (next.getBlock().getType() != Material.AIR) {
            next.getWorld().createExplosion(next , 3F);
        }
    }

    private void setPicker(SetPicker<Location> picker) {
        this.picker = picker;
    }

}

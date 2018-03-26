package com.mengcraft.wallwar;

import com.mengcraft.wallwar.util.BiFunctionRegistry;
import me.clip.placeholderapi.external.EZPlaceholderHook;
import org.bukkit.entity.Player;

import static com.mengcraft.wallwar.Main.nil;

public class PlaceholderSupport extends EZPlaceholderHook {

    private static PlaceholderSupport instance;
    private final BiFunctionRegistry<Player, String, String> registry = new BiFunctionRegistry<>();

    private PlaceholderSupport(Main plugin) {
        super(plugin, "wallwar");
        registry.register("team_name", (p, _i) -> {
            Rank rank = plugin.getMatch().getRank(p);
            return rank.getTag();
        });
        registry.register("team_color", (p, _i) -> {
            Rank rank = plugin.getMatch().getRank(p);
            return "" + rank.getColour();
        });
    }

    @Override
    public String onPlaceholderRequest(Player p, String label) {
        return String.valueOf(registry.handle(label, p, ""));
    }

    public static void b(Main plugin) {
        if (nil(instance)) {
            instance = new PlaceholderSupport(plugin);
            instance.hook();
        }
    }

}

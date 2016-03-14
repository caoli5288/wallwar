package com.mengcraft.wallwar;

import com.mengcraft.maprestore.MapRestore;
import com.mengcraft.wallwar.util.Action;
import com.mengcraft.wallwar.level.Area;
import com.mengcraft.wallwar.util.Title;
import com.mengcraft.wallwar.util.TitleManager;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Iterator;

import static java.lang.Integer.parseInt;
import static java.util.Arrays.asList;
import static org.bukkit.ChatColor.translateAlternateColorCodes;

/**
 * Created on 16-3-1.
 */
public class Commander implements CommandExecutor {

    private final WorldEditPlugin we = JavaPlugin.getPlugin(WorldEditPlugin.class);
    private final MapRestore restore = JavaPlugin.getPlugin(MapRestore.class);

    private Match match;
    private Main main;
    private Action action;
    private TitleManager title;

    @Override
    public boolean onCommand(CommandSender p, Command cmd, String s, String[] args) {
        if (p instanceof Player && args.length > 0) {
            return process(Player.class.cast(p), asList(args).iterator());
        } else {
            p.sendMessage("§4Command error! Empty request");
        }
        return false;
    }

    private boolean process(Player p, Iterator<String> it) {
        String sub = it.next();
        if (sub.equals("get-debug")) {
            return getDebug(p);
        }
        if (sub.equals("get-action")) {
            return getAction(p, it);
        }
        if (sub.equals("set-lobby")) {
            return setLobby(p);
        }
        if (sub.equals("set-level")) {
            return setLevel(p);
        }
        if (sub.equals("set-size")) {
            return setSize(p, it);
        }
        if (sub.equals("set-time")) {
            return setTime(p, it);
        }
        if (sub.equals("set-area")) {
            return setArea(p, it);
        }
        if (sub.equals("set-wall")) {
            return setWall(p);
        }
        if (sub.equals("set-spawn")) {
            return setSpawn(p, it);
        }
        if (sub.equals("set-save")) {
            return setSave(p);
        }
        if (sub.equals("get-title")) {
            return getTitle(p, it);
        }
        return false;
    }

    private boolean getTitle(Player p, Iterator<String> it) {
        if (it.hasNext()) {
            title.setTitle(p, new Title(translateAlternateColorCodes('&', it.next()), "§2This is a subtitle!"));
        } else {
            title.setTitle(p, new Title("§1This is a title!", "§2This is a subtitle!"));
        }
        return false;
    }

    private boolean getAction(Player p, Iterator<String> it) {
        if (it.hasNext()) {
            action.sendAction(p, it.next().replace('&', ChatColor.COLOR_CHAR));
        } else {
            action.sendAction(p, "§1This is a sample!");
        }
        return false;
    }

    private boolean getDebug(Player p) {
        p.sendMessage(match.toString());
        return true;
    }

    private boolean setSave(Player p) {
        boolean b = match.check();
        if (b) {
            restore.addMap(match.getLand().getLevel());
            match.save();
            match.getLand().save();
            main.saveConfig();
        } else {
            p.sendMessage("§4Command error! Not configured yet.");
        }
        return b;
    }

    private boolean setSpawn(Player p, Iterator<String> it) {
        boolean b = it.hasNext() && p.getWorld() == match.getLand().getLevel();
        if (b) {
            Area area = match.getLand().getArea(parseInt(it.next()));
            if (area != null) {
                Location loc = p.getLocation();
                if (area.contains(loc)) {
                    area.setSpawn(p.getLocation());
                } else {
                    p.sendMessage("§4Command error! Not contains in area.");
                }
            } else {
                p.sendMessage("§4Command error! Area not found.");
            }
        } else {
            p.sendMessage("§4Command error! Area not found.");
        }
        return b;
    }

    private boolean setLobby(Player p) {
        boolean b = match.getLobby() == null;
        if (b) {
            match.setLobby(p.getLocation());
        } else {
            p.sendMessage("§4Command error! Same with match level.");
        }
        return b;
    }


    private boolean setWall(Player p) {
        boolean b = p.getWorld() == match.getLand().getLevel();
        if (b) {
            Selection selection = we.getSelection(p);
            if (selection != null) {
                match.getLand().getWallSet().add(Area.of(
                        selection.getMinimumPoint(),
                        selection.getMaximumPoint()
                ));
            } else {
                p.sendMessage("§4Command error! Not selected.");
            }
        } else {
            p.sendMessage("§4Command error!");
        }
        return b;
    }

    private boolean setArea(Player p, Iterator<String> it) {
        boolean b = it.hasNext() && p.getWorld() == match.getLand().getLevel();
        if (b) {
            Selection selection = we.getSelection(p);
            if (selection != null) {
                match.getLand().setArea(parseInt(it.next()), Area.of(
                        selection.getMinimumPoint(),
                        selection.getMaximumPoint()
                ));
            } else {
                p.sendMessage("§4Command error! Not selected.");
            }
        } else {
            p.sendMessage("§4Command error!");
        }
        return b;
    }

    private boolean setSize(Player p, Iterator<String> it) {
        boolean b = it.hasNext();
        if (b) {
            int i = parseInt(it.next());
            int j = it.hasNext() ? parseInt(it.next()) : i;

            match.getLand().setMinSize(i);
            match.getLand().setMaxSize(j);
        } else {
            p.sendMessage("§4Command error!");
        }
        return b;
    }

    private boolean setTime(Player p, Iterator<String> it) {
        boolean b = it.hasNext();
        if (b) {
            int wait = parseInt(it.next());
            int wall = parseInt(it.next());
            int lava = parseInt(it.next());

            match.setWait(wait);
            match.setWall(wall);
            match.setLava(lava);
        } else {
            p.sendMessage("§4Command error!");
        }
        return b;
    }

    private boolean setLevel(Player p) {
        boolean b = match.getLand().getLevel() == null;
        if (b) {
            match.getLand().setLevel(p.getWorld());
        } else {
            p.sendMessage("§4Command error!");
        }
        return b;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public void setTitle(TitleManager title) {
        this.title = title;
    }
}

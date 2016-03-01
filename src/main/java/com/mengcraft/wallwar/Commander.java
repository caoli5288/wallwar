package com.mengcraft.wallwar;

import com.sk89q.worldedit.WorldEdit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Iterator;

import static java.util.Arrays.asList;

/**
 * Created on 16-3-1.
 */
public class Commander implements CommandExecutor {

    private Match match;
    private Main main;
    private WorldEdit we;

    @Override
    public boolean onCommand(CommandSender p, Command cmd, String s, String[] args) {
        if (p instanceof Player && args.length > 0) {
            return process(Player.class.cast(p), asList(args).iterator());
        } else {
            p.sendMessage("§4Command error!");
        }
        return false;
    }

    private boolean process(Player p, Iterator<String> it) {
        return false;
    }

}

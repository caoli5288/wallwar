package com.mengcraft.wallwar;

import com.mengcraft.wallwar.entity.StatusBoard;
import com.mengcraft.wallwar.entity.WallUser;
import me.tigerhix.lib.scoreboard.ScoreboardLib;
import me.tigerhix.lib.scoreboard.type.Scoreboard;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerListPingEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.mengcraft.wallwar.util.CollectionUtil.forEach;

/**
 * Created on 16-2-25.
 */
public class Executor implements Listener {

    private StatusBoard board;
    private Match match;
    private Main main;

    @EventHandler
    public void handle(AsyncPlayerChatEvent event) {
        if (match.isRunning() && !match.isEnd()) {
            Rank rank = match.getRank(event.getPlayer());
            if (Rank.NONE.equals(rank)) {
                Set<Player> set = event.getRecipients();
                set.clear();
                set.addAll(match.getViewer());
            } else if (event.getMessage().startsWith("!") && !event.getPlayer().hasPermission("wall.chat.shout")) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(ChatColor.DARK_RED + "你没有权限这样做！");
            } else {
                Set<Player> set = event.getRecipients();
                set.clear();
                set.addAll(match.getTeam(rank));
                set.addAll(match.getViewer());
            }
            event.setFormat("[" + rank.getColour() + rank.getTag() + "§r]§7 %1$s §8>§7>§f> %2$s");
        }
    }

    @EventHandler
    public void handle(InventoryOpenEvent event) {
        if (match.isNotRunning()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void handle(PlayerInteractEvent event) {
        if (match.isNotRunning()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void handle(ServerListPingEvent event) {
        if (match.isRunning()) {
            event.setMotd(ChatColor.RED + "游戏中");
        } else {
            event.setMotd(ChatColor.GREEN + "等待中");
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void handle(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        if (match.isRunning()) {
            match.addViewer(p);
            event.setJoinMessage("§7[§b§l梦世界§7] §6玩家§a"
                    + p.getName()
                    + "§6加入了观战。");

        } else {
            match.addWaiter(p);
            event.setJoinMessage("§7[§b§l梦世界§7] §6玩家§a"
                    + p.getName()
                    + "§6加入了游戏。§7(§3"
                    + p.getServer().getOnlinePlayers().size()
                    + "§7/§6" + match.getLand().getMaxSize() + "§7)");
        }
        main.runTask(() -> {
            WallUser user = main.getDatabase().find(WallUser.class, p.getUniqueId());
            if (user != null) {
                main.getUserMap().put(user.getId(), user);
            } else {
                main.createUser(p);
            }
        });
        Scoreboard scoreboard = ScoreboardLib.createScoreboard(p);
        scoreboard.setHandler(board)
                .setUpdateInterval(10);
        scoreboard.activate();
    }

    @EventHandler
    public void handle(PlayerDeathEvent event) {
        Player p = event.getEntity();
        if (match.isRunning()) {
            match.addViewer(p);
        }
        match.clearUp(p);
        match.checkUp();

        match.getUser(p).addDead();
        if (p.getKiller() != null) {
            match.getUser(p.getKiller()).addKilled();
        }

        p.resetTitle();
        p.sendTitle(ChatColor.BLUE + "你在游戏中死亡", ChatColor.YELLOW + "你进入观战模式");

        p.sendMessage(ChatColor.BLUE + "你在游戏中死亡");
        p.sendMessage(ChatColor.YELLOW + "你进入观战模式");
    }

    @EventHandler
    public void handle(PlayerQuitEvent event) {
        Player p = event.getPlayer();
        if (p.getLocation().getWorld() != match.getLobby().getWorld()) {
            p.teleport(match.getLobby());
        }
        main.saveBean(p);
        match.clearUp(p);
        match.checkUp();
    }

    @EventHandler
    public void handle(EntityDamageEvent event) {
        if (match.isNotRunning()) {
            event.setDamage(0);
        }
    }

    @EventHandler
    public void handle(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            event.setCancelled(match.isTeammate(event.getEntity(), event.getDamager()));
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void handle(BlockExplodeEvent event) {
        List<Block> list = new ArrayList<>();
        forEach(event.blockList(), b -> match.isRanked(b), b -> {
            list.add(b);
        });
        event.blockList().removeAll(list);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void handle(EntityExplodeEvent event) {
        List<Block> list = new ArrayList<>();
        forEach(event.blockList(), b -> match.isRanked(b), b -> {
            list.add(b);
        });
        event.blockList().removeAll(list);
    }

    @EventHandler
    public void handle(BlockBreakEvent event) {
        if (match.isNotRunning()) {
            event.setCancelled(true);
        } else if (isNotRanked(event.getPlayer(), event.getBlock())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void handle(BlockPlaceEvent event) {
        if (match.isNotRunning()) {
            event.setCancelled(true);
        } else if (isNotRanked(event.getPlayer(), event.getBlock())) {
            event.setCancelled(true);
        }
    }

    private boolean isNotRanked(Player p, Block b) {
        if (match.getWall() != 0) {
            return !match.isRanked(p, b);
        }
        return !match.isRanked(b);
    }

    public void setBoard(StatusBoard board) {
        this.board = board;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public void setMain(Main main) {
        this.main = main;
    }

}

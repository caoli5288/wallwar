package com.mengcraft.wallwar;

import com.mengcraft.wallwar.entity.StatusBoard;
import com.mengcraft.wallwar.entity.WallUser;
import com.mengcraft.wallwar.scoreboard.FixedBody;
import com.mengcraft.wallwar.scoreboard.SidebarBoard;
import com.mengcraft.wallwar.scoreboard.TextLine;
import com.mengcraft.wallwar.util.ListHelper;
import lombok.val;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.kitteh.tag.AsyncPlayerReceiveNameTagEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.mengcraft.wallwar.util.ListHelper.forEach;

/**
 * Created on 16-2-25.
 */
public class Executor implements Listener {

    private Match match;
    private Main main;

    @EventHandler(priority = EventPriority.HIGHEST)
    public void handle(AsyncPlayerReceiveNameTagEvent event) {
        if (match.isRunning()) {
            Rank rank = match.getRank(event.getNamedPlayer());
            if (rank != Rank.NONE) {
                event.setTag(rank.getColour() + ChatColor.stripColor(event.getTag()));
            }
        }
    }

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
        SidebarBoard board = SidebarBoard.of(main);
        board.setHead(TextLine.of(String.format(match.getMessage("scoreboard.title"), p.getName())));
        board.setBody(FixedBody.of(new StatusBoard(match, p)));
        board.update(p);
        board.update(p::isOnline, 10);
    }

    @EventHandler
    public void handle(PlayerDeathEvent event) {
        Player p = event.getEntity();
        if (match.isRunning()) {
            match.addViewer(p);
        }
        match.cleanUp(p);
        match.checkEnd();

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
    public void handle(FoodLevelChangeEvent event) {
        if (match.isNotRunning()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void handle(PlayerQuitEvent event) {
        Player p = event.getPlayer();
        if (p.getLocation().getWorld() != match.getLobby().getWorld()) {
            p.teleport(match.getLobby());
        }
        main.save(p);
        match.cleanUp(p);
        match.checkEnd();
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void handle(EntityDamageEvent event) {
        if (match.isNotRunning()) {
            event.setCancelled(true);
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
        List<Block> l = new ArrayList<>();
        forEach(event.blockList(), b -> match.isWall(b) || !match.isArea(b), l::add);
        event.blockList().removeAll(l);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void handle(EntityExplodeEvent event) {
        List<Block> l = new ArrayList<>();
        forEach(event.blockList(), b -> match.isWall(b) || !match.isArea(b), l::add);
        event.blockList().removeAll(l);
    }

    @EventHandler
    public void handle(BlockBreakEvent event) {
        if (match.isNotRunning()) {
            event.setCancelled(true);
        } else if (match.isWall(event.getBlock())) {
            event.setCancelled(true);
        } else if (!match.isArea(event.getBlock())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void handle(BlockPlaceEvent event) {
        if (match.isNotRunning()) {
            event.setCancelled(true);
        } else if (match.isWall(event.getBlock())) {
            event.setCancelled(true);
        } else if (!match.isArea(event.getBlock())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void handle(BlockPistonExtendEvent event) {
        if (match.isNotRunning()) {
            val list = event.getBlocks();
            if (ListHelper.any(list, b -> match.isWall(b) || !match.isArea(b))) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void handle(BlockPistonRetractEvent event) {
        if (match.isNotRunning()) {
            val list = event.getBlocks();
            if (ListHelper.any(list, b -> match.isWall(b) || !match.isArea(b))) {
                event.setCancelled(true);
            }
        }
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public void setMain(Main main) {
        this.main = main;
    }

}

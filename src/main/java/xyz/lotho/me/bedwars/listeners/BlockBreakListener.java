package xyz.lotho.me.bedwars.listeners;

import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import xyz.lotho.me.bedwars.Bedwars;
import xyz.lotho.me.bedwars.managers.game.Game;
import xyz.lotho.me.bedwars.managers.team.Team;
import xyz.lotho.me.bedwars.util.Chat;

import java.util.concurrent.atomic.AtomicReference;

public class BlockBreakListener implements Listener {

    private final Bedwars instance;

    public BlockBreakListener(Bedwars instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        Game game = this.instance.getGameManager().findPlayerGame(player.getUniqueId());
        if (game == null) return;

        if (block.getType() == Material.BED_BLOCK) {
            event.setCancelled(true);
            block.setType(Material.AIR, false);

            Team nearestTeam = game.getNearestBed(block.getLocation());
            Team playerTeam = game.getGamePlayerManager().getPlayerTeam(player.getUniqueId());

            if (nearestTeam.getTeamName().equals(playerTeam.getTeamName())) {
                player.sendMessage(Chat.color("&cYou cannot break your own bed!"));
                event.setCancelled(true);
                return;
            }

            nearestTeam.breakBed(game.getGamePlayerManager().getPlayer(player.getUniqueId()));
        } else {
            if (!game.getBlockManager().isBlockPlayerPlaced(block)) {
                event.setCancelled(true);
                player.sendMessage(Chat.color("&cYou can only break blocks placed by a player."));
                return;
            }

            game.getBlockManager().removePlayerPlacedBlock(block);
        }
    }
}

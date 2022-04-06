package xyz.lotho.me.bedwars.listeners;

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

public class BlockBreakListener implements Listener {

    private final Bedwars instance;

    public BlockBreakListener(Bedwars instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        Game game = this.instance.getGameManager().findGameByPlayer(player.getUniqueId());
        if (game == null) return;

        if (!game.getBlockManager().isBlockWithinBounds(block)) {
            player.sendMessage(Chat.color("&cYou are too far outside the map to do this!"));
            event.setCancelled(true);
            return;
        }

        if (block.getType() == Material.BED_BLOCK) {
            Team nearestTeam = game.getWorldManager().getNearestBed(block.getLocation());
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

package xyz.lotho.me.bedwars.listeners;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import xyz.lotho.me.bedwars.Bedwars;
import xyz.lotho.me.bedwars.managers.game.Game;
import xyz.lotho.me.bedwars.util.Chat;

public class BlockPlaceListener implements Listener {

    private final Bedwars instance;

    public BlockPlaceListener(Bedwars instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onBreak(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        Game game = this.instance.getGameManager().findPlayerGame(player.getUniqueId());
        if (game == null) return;

        // todo: check for protected zones

        if (!game.getBlockManager().isBlockWithinBounds(block)) {
            player.sendMessage(Chat.color("&cYou are too far outside the map to do this!"));
            event.setCancelled(true);
            return;
        }

        game.getBlockManager().addPlayerPlacedBlock(block);
    }
}

package xyz.lotho.me.bedwars.listeners;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import xyz.lotho.me.bedwars.Bedwars;
import xyz.lotho.me.bedwars.managers.game.Game;

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
        // todo: check if in bounds of game area

        game.getBlockManager().addPlayerPlacedBlock(block);
    }
}

package xyz.lotho.me.bedwars.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import xyz.lotho.me.bedwars.Bedwars;
import xyz.lotho.me.bedwars.managers.game.Game;
import xyz.lotho.me.bedwars.managers.player.GamePlayer;
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

        Game game = this.instance.getGameManager().findGameByPlayer(player.getUniqueId());
        if (game == null) return;

        if (block.getType() == Material.TNT) {
            GamePlayer gamePlayer = game.getGamePlayerManager().getPlayer(player.getUniqueId());
            gamePlayer.removeItem(Material.TNT, 1);

            game.getWorld().spawnEntity(block.getLocation(), EntityType.PRIMED_TNT);
            event.setCancelled(true);
        }

        // todo: check for protected zones

        if (!game.getBlockManager().isBlockWithinBounds(block)) {
            player.sendMessage(Chat.color("&cYou are too far outside the map to do this!"));
            event.setCancelled(true);
            return;
        }

        game.getBlockManager().addPlayerPlacedBlock(block);
    }
}

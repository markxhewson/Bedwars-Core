package xyz.lotho.me.bedwars.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import xyz.lotho.me.bedwars.Bedwars;
import xyz.lotho.me.bedwars.managers.game.Game;

import java.util.Iterator;
import java.util.List;

public class ExplodeEventListener implements Listener {

    private final Bedwars instance;

    public ExplodeEventListener(Bedwars instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onExplode(EntityExplodeEvent event) {
        if (event.getEntity().getType() != EntityType.PRIMED_TNT) return;

        Game game = this.instance.getGameManager().findGameByLocation(event.getLocation());
        if (game == null) return;

        List<Block> destroyed = event.blockList();
        destroyed.removeIf(block -> !game.getBlockManager().isBlockPlayerPlaced(block) || block.getType() == Material.STAINED_GLASS);
    }
}

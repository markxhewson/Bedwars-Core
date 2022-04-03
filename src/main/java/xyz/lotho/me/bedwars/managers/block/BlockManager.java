package xyz.lotho.me.bedwars.managers.block;

import org.bukkit.Location;
import org.bukkit.block.Block;
import xyz.lotho.me.bedwars.Bedwars;
import xyz.lotho.me.bedwars.managers.game.Game;

import java.util.ArrayList;

public class BlockManager {

    private final Bedwars instance;
    private final Game game;

    private final ArrayList<Block> playerPlacedBlocks = new ArrayList<>();

    public BlockManager(Bedwars instance, Game game) {
        this.instance = instance;
        this.game = game;
    }

    public ArrayList<Block> getPlayerPlacedBlocks() {
        return this.playerPlacedBlocks;
    }

    public boolean isBlockWithinBounds(Block block) {
        Location location = block.getLocation();

        int topBlockX = (Math.max(this.game.getCornerOne().getBlockX(), this.game.getCornerTwo().getBlockX()));
        int bottomBlockX = (Math.min(this.game.getCornerOne().getBlockX(), this.game.getCornerTwo().getBlockX()));

        int topBlockY = (Math.max(this.game.getCornerOne().getBlockY(), this.game.getCornerTwo().getBlockY()));
        int bottomBlockY = (Math.min(this.game.getCornerOne().getBlockY(), this.game.getCornerTwo().getBlockY()));

        int topBlockZ = (Math.max(this.game.getCornerOne().getBlockZ(), this.game.getCornerTwo().getBlockZ()));
        int bottomBlockZ = (Math.min(this.game.getCornerOne().getBlockZ(), this.game.getCornerTwo().getBlockZ()));

        for(int x = bottomBlockX; x <= topBlockX; x++) {
            for (int z = bottomBlockZ; z <= topBlockZ; z++) {
                for (int y = bottomBlockY; y <= topBlockY; y++) {
                    Location cacheLocation = new Location(this.game.getWorld(), x, y, z);
                    if (cacheLocation.equals(location)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public boolean isBlockPlayerPlaced(Block block) {
        return this.getPlayerPlacedBlocks().contains(block);
    }

    public void addPlayerPlacedBlock(Block block) {
        this.getPlayerPlacedBlocks().add(block);
    }

    public void removePlayerPlacedBlock(Block block) {
        this.getPlayerPlacedBlocks().remove(block);
    }

}

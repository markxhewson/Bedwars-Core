package xyz.lotho.me.bedwars.managers.block;

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

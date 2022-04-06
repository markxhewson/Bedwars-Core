package xyz.lotho.me.bedwars.managers.game;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import xyz.lotho.me.bedwars.Bedwars;
import xyz.lotho.me.bedwars.managers.player.GamePlayer;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class GameManager {

    private final Bedwars instance;
    private final ArrayList<Game> games = new ArrayList<>();

    public GameManager(Bedwars instance) {
        this.instance = instance;
    }

    public ArrayList<Game> getGames() {
        return this.games;
    }

    public void addGame(Game game) {
        this.getGames().add(game);
    }

    public void removeGame(Game game) {
        this.getGames().remove(game);
    }

    public Game findGameByLocation(Location location) {
        for (Game game : games) {
            int topBlockX = (Math.max(game.getCornerOne().getBlockX(), game.getCornerTwo().getBlockX()));
            int bottomBlockX = (Math.min(game.getCornerOne().getBlockX(), game.getCornerTwo().getBlockX()));

            int topBlockY = (Math.max(game.getCornerOne().getBlockY(), game.getCornerTwo().getBlockY()));
            int bottomBlockY = (Math.min(game.getCornerOne().getBlockY(), game.getCornerTwo().getBlockY()));

            int topBlockZ = (Math.max(game.getCornerOne().getBlockZ(), game.getCornerTwo().getBlockZ()));
            int bottomBlockZ = (Math.min(game.getCornerOne().getBlockZ(), game.getCornerTwo().getBlockZ()));

            for(int x = bottomBlockX; x <= topBlockX; x++) {
                for (int z = bottomBlockZ; z <= topBlockZ; z++) {
                    for (int y = bottomBlockY; y <= topBlockY; y++) {
                        if (location.getBlockX() == x && location.getBlockY() == y && location.getBlockZ() == z) {
                            return game;
                        }
                    }
                }
            }
        }

        return null;
    }

    public Game findGameByPlayer(UUID uuid) {
        for (Game game : games) {
            for (GamePlayer gamePlayer : game.getGamePlayers()) {
                if (gamePlayer.getUuid() == uuid) {
                    return game;
                }
            }
        };

        return null;
    }

}

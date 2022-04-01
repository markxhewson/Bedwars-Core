package xyz.lotho.me.bedwars.managers.game;

import xyz.lotho.me.bedwars.Bedwars;
import xyz.lotho.me.bedwars.managers.player.GamePlayer;

import java.util.ArrayList;
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

    public Game findPlayerGame(UUID uuid) {
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

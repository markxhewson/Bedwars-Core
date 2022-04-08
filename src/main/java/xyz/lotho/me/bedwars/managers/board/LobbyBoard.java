package xyz.lotho.me.bedwars.managers.board;

import dev.jcsoftware.jscoreboards.JPerPlayerScoreboard;
import org.bukkit.entity.Player;
import xyz.lotho.me.bedwars.Bedwars;
import xyz.lotho.me.bedwars.managers.game.Game;
import xyz.lotho.me.bedwars.managers.player.GamePlayer;

import java.util.Arrays;

public class LobbyBoard {

    private final Bedwars instance;
    private final Game game;
    private GamePlayer gamePlayer;

    public LobbyBoard(Bedwars instance, Game game, GamePlayer gamePlayer) {
        this.instance = instance;
        this.game = game;
        this.gamePlayer = gamePlayer;

        createBoard();
    }

    public void createBoard() {
        JPerPlayerScoreboard scoreboard = new JPerPlayerScoreboard((player) -> "&e&lBedwars",
            (player) ->
                Arrays.asList(
                        "&7&m----------------",
                        "&fState: &a" + game.getGameState().name(),
                        "",
                        "&fPlayers: &e" + game.getGamePlayers().size() + "/" + "16",
                        "&fMap: &a" + game.getMapName(),
                        "",
                        "&eplay.lotho.xyz",
                        "&7&m----------------"
        ));

        Player player = this.gamePlayer.getPlayer();
        if (player == null) return;

        scoreboard.addPlayer(player);
        this.gamePlayer.setScoreboard(scoreboard);
    }
}

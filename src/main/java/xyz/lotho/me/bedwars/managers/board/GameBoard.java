package xyz.lotho.me.bedwars.managers.board;

import dev.jcsoftware.jscoreboards.JPerPlayerScoreboard;
import org.bukkit.entity.Player;
import xyz.lotho.me.bedwars.Bedwars;
import xyz.lotho.me.bedwars.managers.game.Game;
import xyz.lotho.me.bedwars.managers.player.GamePlayer;
import xyz.lotho.me.bedwars.managers.team.Team;
import xyz.lotho.me.bedwars.managers.team.TeamManager;

import java.util.Arrays;

public class GameBoard {

    private final Bedwars instance;
    private final Game game;
    private GamePlayer gamePlayer;

    public GameBoard(Bedwars instance, Game game, GamePlayer gamePlayer) {
        this.instance = instance;
        this.game = game;
        this.gamePlayer = gamePlayer;

        createBoard();
    }

    public void createBoard() {
        TeamManager teamManager = game.getTeamManager();

        String bedAlive = "&a✓";
        String teamDead = "&c✗";

        Team redTeam = teamManager.getTeam("RED");
        Team blueTeam = teamManager.getTeam("BLUE");
        Team greenTeam = teamManager.getTeam("GREEN");
        Team yellowTeam = teamManager.getTeam("YELLOW");
        Team aquaTeam = teamManager.getTeam("AQUA");
        Team whiteTeam = teamManager.getTeam("WHITE");
        Team pinkTeam = teamManager.getTeam("PINK");
        Team grayTeam = teamManager.getTeam("GRAY");

        JPerPlayerScoreboard scoreboard = new JPerPlayerScoreboard((player) -> "&e&lBedwars",
                (player) ->
                        Arrays.asList(
                                "&7&m----------------",
                                "&fState: &a" + game.getGameState().name(),
                                "",
                                "&c" + redTeam.getTeamName().charAt(0) + " &fRed: " + (redTeam.isBedBroken() ? (redTeam.getAliveMembers().size() > 0 ? "&c" + redTeam.getAliveMembers().size() : teamDead) : bedAlive),
                                "&9" + blueTeam.getTeamName().charAt(0) + " &fBlue: " + (blueTeam.isBedBroken() ? (blueTeam.getAliveMembers().size() > 0 ? "&c" + blueTeam.getAliveMembers().size() : teamDead) : bedAlive),
                                "&a" + greenTeam.getTeamName().charAt(0) + " &fGreen: " + (greenTeam.isBedBroken() ? (greenTeam.getAliveMembers().size() > 0 ? "&c" + greenTeam.getAliveMembers().size() : teamDead) : bedAlive),
                                "&e" + yellowTeam.getTeamName().charAt(0) + " &fYellow: " + (yellowTeam.isBedBroken() ? (yellowTeam.getAliveMembers().size() > 0 ? "&c" + yellowTeam.getAliveMembers().size() : teamDead) : bedAlive),
                                "&b" + aquaTeam.getTeamName().charAt(0) + " &fAqua: " + (aquaTeam.isBedBroken() ? (aquaTeam.getAliveMembers().size() > 0 ? "&c" + aquaTeam.getAliveMembers().size() : teamDead) : bedAlive),
                                "&f" + whiteTeam.getTeamName().charAt(0) + " &fWhite: " + (whiteTeam.isBedBroken() ? (whiteTeam.getAliveMembers().size() > 0 ? "&c" + whiteTeam.getAliveMembers().size() : teamDead) : bedAlive),
                                "&d" + pinkTeam.getTeamName().charAt(0) + " &fPink: " + (pinkTeam.isBedBroken() ? (pinkTeam.getAliveMembers().size() > 0 ? "&c" + pinkTeam.getAliveMembers().size() : teamDead) : bedAlive),
                                "&8" + grayTeam.getTeamName().charAt(0) + " &fGray: " + (grayTeam.isBedBroken() ? (grayTeam.getAliveMembers().size() > 0 ? "&c" + grayTeam.getAliveMembers().size() : teamDead) : bedAlive),
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

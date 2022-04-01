package xyz.lotho.me.bedwars.managers.teams;

import org.bukkit.ChatColor;
import xyz.lotho.me.bedwars.Bedwars;
import xyz.lotho.me.bedwars.managers.game.Game;

import java.util.HashMap;
import java.util.Map;

public class TeamManager {

    private final Bedwars instance;
    private final Game game;
    private final Map<String, Team> teamsMap = new HashMap<>();

    public TeamManager(Bedwars instance, Game game) {
        this.instance = instance;
        this.game = game;
    }

    public Map<String, Team> getTeamsMap() {
        return this.teamsMap;
    }

    public void addTeam(String teamName, ChatColor teamColor) {
        this.getTeamsMap().put(teamName, new Team(this.instance, this.game, teamName, teamColor));
    }

    public Team getTeam(String name) {
        return this.getTeamsMap().getOrDefault(name, null);
    }
}

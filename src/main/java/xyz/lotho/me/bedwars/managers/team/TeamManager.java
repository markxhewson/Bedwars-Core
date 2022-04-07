package xyz.lotho.me.bedwars.managers.team;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import xyz.lotho.me.bedwars.Bedwars;
import xyz.lotho.me.bedwars.managers.game.Game;

import java.util.ArrayList;
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

    public ArrayList<Team> getAliveTeams() {
        ArrayList<Team> teams = new ArrayList<>();

        this.getTeamsMap().forEach((teamName, team) -> {
            if (team.getAliveMembers().size() > 0) teams.add(team);
        });

        return teams;
    }

    public void addTeam(String teamName, ChatColor teamColor, Color armorColor, int metaID) {
        this.getTeamsMap().put(teamName, new Team(this.instance, this.game, teamName, teamColor, armorColor, metaID));
    }

    public Team getTeam(String name) {
        return this.getTeamsMap().getOrDefault(name, null);
    }
}

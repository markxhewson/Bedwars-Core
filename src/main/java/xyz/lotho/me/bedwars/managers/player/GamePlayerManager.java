package xyz.lotho.me.bedwars.managers.player;

import xyz.lotho.me.bedwars.Bedwars;
import xyz.lotho.me.bedwars.managers.game.Game;
import xyz.lotho.me.bedwars.managers.teams.Team;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class GamePlayerManager {

    private final Bedwars instance;
    private final Game game;

    private final Map<UUID, GamePlayer> playerMap = new HashMap<>();

    public GamePlayerManager(Bedwars instance, Game game) {
        this.instance = instance;
        this.game = game;
    }

    public Map<UUID, GamePlayer> getPlayerMap() {
        return this.playerMap;
    }

    public void addPlayer(UUID uuid) {
        this.getPlayerMap().put(uuid, new GamePlayer(this.instance, uuid));
    }

    public GamePlayer getPlayer(UUID uuid) {
        return this.getPlayerMap().getOrDefault(uuid, null);
    }

    public Team getPlayerTeam(UUID uuid) {
        AtomicReference<Team> playerTeam = new AtomicReference<>();

        this.game.getTeamManager().getTeamsMap().forEach((teamName, team) -> {
            team.getTeamMembers().forEach(gamePlayer -> {
                if (gamePlayer.getUuid() == uuid) {
                    playerTeam.set(team);
                }
            });
        });

        return playerTeam.get();
    }
}

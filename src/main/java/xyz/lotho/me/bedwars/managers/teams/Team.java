package xyz.lotho.me.bedwars.managers.teams;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.lotho.me.bedwars.Bedwars;
import xyz.lotho.me.bedwars.managers.game.Game;
import xyz.lotho.me.bedwars.managers.player.GamePlayer;
import xyz.lotho.me.bedwars.util.ItemBuilder;

import java.util.ArrayList;

public class Team {

    private final Bedwars instance;
    private final Game game;

    private final String teamName;
    private final ChatColor teamColor;
    private final Color armorColor;

    private Location spawnLocation;
    private ArrayList<GamePlayer> teamMembers = new ArrayList<>();

    private final int maxTeamSize = 1;

    public Team(Bedwars instance, Game game, String teamName, ChatColor teamColor, Color armorColor) {
        this.instance = instance;
        this.game = game;
        this.teamName = teamName;
        this.teamColor = teamColor;
        this.armorColor = armorColor;
    }

    public void loadTeam() {
        this.getTeamMembers().forEach(GamePlayer::spawn);
    }

    public Game getGame() {
        return game;
    }

    public String getTeamName() {
        return teamName;
    }

    public ChatColor getTeamColor() {
        return teamColor;
    }

    public Color getArmorColor() {
        return this.armorColor;
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public void setSpawnLocation(Location spawnLocation) {
        // this.game.getGameWorld().getBlockAt(spawnLocation).setType(Material.AIR);
        this.spawnLocation = spawnLocation;
    }

    public void addMember(GamePlayer gamePlayer) {
        this.getTeamMembers().add(gamePlayer);
    }

    public void removeMember(GamePlayer gamePlayer) {
        this.getTeamMembers().remove(gamePlayer);
    }

    public ArrayList<GamePlayer> getTeamMembers() {
        return teamMembers;
    }

    public int getMaxTeamSize() {
        return maxTeamSize;
    }
}

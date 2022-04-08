package xyz.lotho.me.bedwars.managers.game;

import com.sk89q.worldedit.*;
import com.sk89q.worldedit.data.DataException;
import org.bukkit.*;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import xyz.lotho.me.bedwars.Bedwars;
import xyz.lotho.me.bedwars.generators.Generator;
import xyz.lotho.me.bedwars.managers.block.BlockManager;
import xyz.lotho.me.bedwars.managers.player.GamePlayer;
import xyz.lotho.me.bedwars.managers.player.GamePlayerManager;
import xyz.lotho.me.bedwars.managers.team.PlayerTeam;
import xyz.lotho.me.bedwars.managers.team.Team;
import xyz.lotho.me.bedwars.managers.team.TeamManager;
import xyz.lotho.me.bedwars.managers.world.WorldManager;
import xyz.lotho.me.bedwars.ui.main.PickTeamMenu;
import xyz.lotho.me.bedwars.util.Chat;
import xyz.lotho.me.bedwars.util.ItemBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class Game {

    private final Bedwars instance;
    private final UUID gameUUID;
    private final World world;
    private final String mapName;
    private final Location lobbyLocation;

    private GameState gameState;
    private Location cornerOne;
    private Location cornerTwo;

    private final GamePlayerManager gamePlayerManager;
    private final TeamManager teamManager;
    private final BlockManager blockManager;
    private final WorldManager worldManager;

    private final PickTeamMenu pickTeamMenu;

    private int gameTickID;

    private boolean started = false;
    private int lobbyTime = 10;
    private int elapsedTime = 0;

    private final ArrayList<Generator> generators = new ArrayList<>();
    private final ArrayList<GamePlayer> players = new ArrayList<>();

    public Game(Bedwars instance, UUID gameUUID, World world, String mapName, Location lobbyLocation) {
        this.instance = instance;
        this.gameUUID = gameUUID;
        this.world = world;
        this.mapName = mapName;
        this.lobbyLocation = lobbyLocation;
        this.gamePlayerManager = new GamePlayerManager(this.instance, this);
        this.teamManager = new TeamManager(this.instance, this);
        this.blockManager = new BlockManager(this.instance, this);
        this.worldManager = new WorldManager(this.instance, this);
        this.pickTeamMenu = new PickTeamMenu(this.instance, this);

        this.setGameState(GameState.PRELOBBY);
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;

        switch (gameState) {
            case PRELOBBY:
                try {
                    this.loadGame();
                } catch (IOException | DataException | MaxChangedBlocksException e) {
                    e.printStackTrace();
                }

                this.instance.getServer().getScheduler().runTaskLater(this.instance, () -> {
                    this.setGameState(GameState.LOBBY);
                }, 50);

                break;

            case LOBBY:
                BukkitTask task = this.instance.getServer().getScheduler().runTaskTimer(this.instance, this::gameTick, 20, 20);
                this.gameTickID = task.getTaskId();

                this.instance.getQueueManager().getQueuedPlayersLimited(16).forEach(uuid -> {
                    Player player = this.instance.getServer().getPlayer(uuid);
                    if (player == null) return;

                    this.getGamePlayerManager().addPlayer(player.getUniqueId());
                    this.players.add(this.getGamePlayerManager().getPlayer(player.getUniqueId()));
                });

                this.loadLobbyPlayers();
                break;

            case PLAYING:
                this.setStarted(true);

                this.getTeamManager().getTeamsMap().forEach((teamName, team) -> {
                    if (!team.isBedBroken() && team.getAliveMembers().size() == 0) {
                        team.setBedBroken(true);
                    }
                });

                this.getGenerators().forEach(generator -> generator.setActive(true));
                this.getTeamManager().getTeamsMap().forEach((teamName, team) -> team.loadTeam());

                break;

            case ENDED:
                Team winningTeam = this.getWinningTeam();

                this.getGamePlayers().forEach(gamePlayer -> {
                    Player player = gamePlayer.getPlayer();
                    if (player == null) return;

                    if (gamePlayer.getTeam() == winningTeam) gamePlayer.sendTitle(player, "&aYOU WIN!", "", 100);
                    else gamePlayer.sendTitle(player, "&cGAME OVER!", "", 100);

                    player.playSound(player.getLocation(), Sound.ENDERDRAGON_DEATH, 1, 1);

                    player.sendMessage(Chat.color("\n&c&lGAME OVER!\n" + winningTeam.getTeamColor() + winningTeam.getTeamName() + " &fhas won the game!"));
                    player.sendMessage(Chat.color("\n\n&aCongratulations:"));

                    winningTeam.getTeamMembers().forEach(winningPlayer -> {
                        player.sendMessage(Chat.color("&8 - &f" + winningPlayer.getPlayer().getName() + " &7(Kills: &f" + winningPlayer.getKills() + " &7| Deaths: &f" + winningPlayer.getDeaths() + "&7)"));
                    });

                    player.sendMessage("");
                });

                this.instance.getServer().getScheduler().runTaskLater(this.instance, this::endGame, 60);
                break;
        }
    }

    public void gameTick() {
        System.out.println(this.getGameState() + "(" + this.lobbyTime + ")");
        System.out.println("Elapsed time: " + this.getElapsedTime() + " seconds");

        switch (this.getGameState()) {
            case LOBBY:
                if (lobbyTime <= 0) {
                    this.setGameState(GameState.PLAYING);
                    return;
                }

                this.getGamePlayerManager().getPlayerMap().forEach((uuid, gamePlayer) -> {
                    Player player = this.instance.getServer().getPlayer(gamePlayer.getUuid());

                    if (lobbyTime == 15 || lobbyTime == 10 || lobbyTime <= 5) {
                        player.sendMessage(Chat.color("&eThe game starts in &c" + lobbyTime + " &esecond" + (lobbyTime == 1 ? "" : "s") + ".."));
                    }
                });

                lobbyTime--;
                break;

            case PLAYING:
                this.getGenerators().forEach(Generator::spawnMaterials);
                this.setElapsedTime(this.getElapsedTime() + 1);

                for (GamePlayer gamePlayer : this.getGamePlayers()) {
                    gamePlayer.getScoreboard().updateScoreboard();
                }

                if (this.getTeamManager().getAliveTeams().size() == 1) {
                    this.setGameState(GameState.ENDED);
                }

                break;
        }
    }

    public Team getWinningTeam() {
        AtomicReference<Team> winningTeam = new AtomicReference<>();

        this.getGamePlayers().forEach(gamePlayer -> {
            Player player = gamePlayer.getPlayer();
            if (player == null) return;

            if (gamePlayer.getTeam().getAliveMembers().size() > 0) {
                winningTeam.set(gamePlayer.getTeam());
            }
        });

        return winningTeam.get();
    }

    public void endGame() {
        this.instance.getServer().getScheduler().runTaskLater(this.instance, () -> {
            this.getGamePlayers().forEach(gamePlayer -> {
                Player player = gamePlayer.getPlayer();
                if (player == null) return;

                gamePlayer.getScoreboard().destroy();

                player.teleport(new Location(this.instance.getMainWorld(), -113, 106, 181));
                player.setGameMode(GameMode.SURVIVAL);
                player.getInventory().clear();

                player.getInventory().setHelmet(null);
                player.getInventory().setChestplate(null);
                player.getInventory().setLeggings(null);
                player.getInventory().setBoots(null);

                player.setPlayerListName(gamePlayer.getPlayer().getName());
            });

            this.instance.getServer().getScheduler().runTaskLater(this.instance, () -> {
                this.getWorldManager().resetMap();
                this.instance.getServer().getScheduler().cancelTask(this.gameTickID);
                this.instance.getGameManager().removeGame(this);
            }, 20);
        }, 20);
    }

    public void loadGame() throws IOException, DataException, MaxChangedBlocksException {
        this.getWorldManager().loadMap(this.getMapName());

        this.setCornerOne(new Location(this.getLobbyLocation().getWorld(), this.getLobbyLocation().getX() - 100, this.getLobbyLocation().getY() + 25, this.getLobbyLocation().getZ() + 100));
        this.setCornerTwo(new Location(this.getLobbyLocation().getWorld(), this.getLobbyLocation().getX() + 100, this.getLobbyLocation().getY() - 90, this.getLobbyLocation().getZ() - 100));

        this.getWorld().getBlockAt(this.getCornerOne()).setType(Material.STONE);
        this.getWorld().getBlockAt(this.getCornerTwo()).setType(Material.STONE);

        this.loadTeams();
        this.getWorldManager().handleMapSetup();
    }

    public void loadLobbyPlayers() {
        this.getGamePlayers().forEach(gamePlayer -> {
            Player player = gamePlayer.getPlayer();
            if (player == null) return;

            gamePlayer.setLobbyBoard();

            player.getInventory().clear();
            player.getInventory().setHelmet(null);
            player.getInventory().setChestplate(null);
            player.getInventory().setLeggings(null);
            player.getInventory().setBoots(null);

            player.teleport(this.getLobbyLocation());
            player.getInventory().setItem(4, new ItemBuilder(Material.NOTE_BLOCK).setDisplayName("&aChoose Team").setLore("&7Choose your team color!").build());
            player.setGameMode(GameMode.ADVENTURE);
        });
    }

    public void loadTeams() {
        Arrays.stream(PlayerTeam.values()).forEach(team -> {
            this.getTeamManager().addTeam(team.toString(), team.getTeamColor(), team.getArmorColor(), team.getMetaID());
        });
    }

    public void broadcast(String message) {
        this.getGamePlayers().forEach((gamePlayer) -> {
            Player player = gamePlayer.getPlayer();
            if (player == null) return;

            player.sendMessage(Chat.color(message));
        });
    }

    public BlockManager getBlockManager() {
        return this.blockManager;
    }

    public ArrayList<Generator> getGenerators() {
        return this.generators;
    }

    public GameState getGameState() {
        return this.gameState;
    }

    public World getWorld() {
        return world;
    }

    public Location getLobbyLocation() {
        return lobbyLocation;
    }

    public Location getCornerOne() {
        return cornerOne;
    }

    public void setCornerOne(Location cornerOne) {
        this.cornerOne = cornerOne;
    }

    public Location getCornerTwo() {
        return cornerTwo;
    }

    public void setCornerTwo(Location cornerTwo) {
        this.cornerTwo = cornerTwo;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public GamePlayerManager getGamePlayerManager() {
        return this.gamePlayerManager;
    }

    public TeamManager getTeamManager() {
        return teamManager;
    }

    public ArrayList<GamePlayer> getGamePlayers() {
        return this.players;
    }

    public int getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(int elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public PickTeamMenu getPickTeamMenu() {
        return this.pickTeamMenu;
    }

    public WorldManager getWorldManager() {
        return this.worldManager;
    }

    public UUID getGameUUID() {
        return gameUUID;
    }

    public String getMapName() {
        return mapName;
    }
}

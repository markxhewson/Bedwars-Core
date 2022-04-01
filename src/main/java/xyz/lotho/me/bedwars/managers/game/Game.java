package xyz.lotho.me.bedwars.managers.game;

import com.sk89q.worldedit.*;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.data.DataException;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.session.ClipboardHolder;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.lotho.me.bedwars.Bedwars;
import xyz.lotho.me.bedwars.generators.Generator;
import xyz.lotho.me.bedwars.generators.GeneratorType;
import xyz.lotho.me.bedwars.managers.player.GamePlayer;
import xyz.lotho.me.bedwars.managers.player.GamePlayerManager;
import xyz.lotho.me.bedwars.managers.teams.PlayerTeam;
import xyz.lotho.me.bedwars.managers.teams.Team;
import xyz.lotho.me.bedwars.managers.teams.TeamManager;
import xyz.lotho.me.bedwars.util.Chat;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class Game {

    private final Bedwars instance;
    private final World world;
    private final Location center;

    private GameState gameState = GameState.LOBBY;
    private Location cornerOne;
    private Location cornerTwo;

    private final GamePlayerManager gamePlayerManager;
    private final TeamManager teamManager;

    private boolean started = false;
    private int lobbyTime = 20;
    private int elapsedTime = 0;

    private final ArrayList<Generator> generators = new ArrayList<>();
    private final ArrayList<GamePlayer> gamePlayers = new ArrayList<>();

    public Game(Bedwars instance, World world, Location center) {
        this.instance = instance;
        this.world = world;
        this.center = center;
        this.gamePlayerManager = new GamePlayerManager(this.instance, this);
        this.teamManager = new TeamManager(this.instance, this);
    }

    public World getGameWorld() {
        return this.getWorld();
    }

    public ArrayList<Generator> getGenerators() {
        return this.generators;
    }

    public void gameTick() {
        switch (this.getGameState()) {
            case LOBBY:
                if (lobbyTime <= 1) {
                    this.setGameState(GameState.PLAYING);
                    this.setStarted(true);

                    this.teamManager.getTeamsMap().forEach((teamName, team) -> team.loadTeam());
                    return;
                }
                lobbyTime--;
                this.instance.getServer().broadcastMessage(Chat.color("&aGame starting in &f" + lobbyTime + " &aseconds.."));
                break;

            case PLAYING:
                this.getGenerators().forEach(Generator::spawnMaterials);

                this.elapsedTime++;
                break;

            case ENDED:
                break;
        }
    }

    public void loadGame() throws IOException, DataException, MaxChangedBlocksException {
        this.loadMap();

        this.setCornerOne(new Location(this.getCenter().getWorld(), this.getCenter().getX() - 100, this.getCenter().getY() + 25, this.getCenter().getZ() + 100));
        this.setCornerTwo(new Location(this.getCenter().getWorld(), this.getCenter().getX() + 100, this.getCenter().getY() - 90, this.getCenter().getZ() - 100));

        this.getWorld().getBlockAt(this.getCornerOne()).setType(Material.STONE);
        this.getWorld().getBlockAt(this.getCornerTwo()).setType(Material.STONE);

        this.handleTeamSetup();
        this.handleMapSetup();

        this.instance.getServer().getScheduler().runTaskLater(this.instance, () -> {
            this.instance.getServer().getOnlinePlayers().forEach(player -> {
                player.teleport(this.getCenter());
                this.getGamePlayerManager().addPlayer(player.getUniqueId());

                GamePlayer gamePlayer = this.getGamePlayerManager().getPlayer(player.getUniqueId());
                this.gamePlayers.add(gamePlayer);
            });

            this.setupLobbyPlayers();
        }, 40);
        this.instance.getServer().getScheduler().runTaskTimer(this.instance, this::gameTick, 20, 20);
    }

    public void setupLobbyPlayers() {
        this.getGamePlayers().forEach(gamePlayer -> {
            Player player = this.instance.getServer().getPlayer(gamePlayer.getUuid());
            if (player == null) return;

            player.getInventory().clear();
            player.getInventory().setItem(4, new ItemStack(Material.NOTE_BLOCK, 1));
            player.setGameMode(GameMode.ADVENTURE);
        });
    }

    public void handleTeamSetup() {
        Arrays.stream(PlayerTeam.values()).forEach(team -> {
            this.getTeamManager().addTeam(team.toString(), team.getTeamColor());
        });
    }

    public void handleMapSetup() {
        int topBlockX = (Math.max(this.getCornerOne().getBlockX(), this.getCornerTwo().getBlockX()));
        int bottomBlockX = (Math.min(this.getCornerOne().getBlockX(), this.getCornerTwo().getBlockX()));

        int topBlockY = (Math.max(this.getCornerOne().getBlockY(), this.getCornerTwo().getBlockY()));
        int bottomBlockY = (Math.min(this.getCornerOne().getBlockY(), this.getCornerTwo().getBlockY()));

        int topBlockZ = (Math.max(this.getCornerOne().getBlockZ(), this.getCornerTwo().getBlockZ()));
        int bottomBlockZ = (Math.min(this.getCornerOne().getBlockZ(), this.getCornerTwo().getBlockZ()));

        for(int x = bottomBlockX; x <= topBlockX; x++) {
            for(int z = bottomBlockZ; z <= topBlockZ; z++) {
                for(int y = bottomBlockY; y <= topBlockY; y++) {
                    Block block = this.getWorld().getBlockAt(x, y, z);

                    switch (block.getType()) {
                        case WOOL:
                            if (block.getData() == 3) { // diamond generator, light blue wool
                                Generator generator = new Generator(this.instance, this, block.getLocation(), GeneratorType.DIAMOND, false);
                                this.getGenerators().add(generator);
                                // block.setType(Material.AIR);
                            }
                            else if (block.getData() == 13) { // emerald generator, dark green wool
                                Generator generator = new Generator(this.instance, this, block.getLocation(), GeneratorType.EMERALD, false);
                                this.getGenerators().add(generator);
                                // block.setType(Material.AIR);
                            }
                            else if (block.getData() == 12) { // iron generator, brown wool
                                Generator generator = new Generator(this.instance, this, block.getLocation(), GeneratorType.IRON, true);
                                this.getGenerators().add(generator);
                                // block.setType(Material.AIR);
                            }
                            else if (block.getData() == 1) { // gold generator, orange wool
                                Generator generator = new Generator(this.instance, this, block.getLocation(), GeneratorType.GOLD, true);
                                this.getGenerators().add(generator);
                                // block.setType(Material.AIR);
                            }
                            break;

                        case STAINED_CLAY:
                            if (block.getData() == 1) { // red team (orange stained clay)
                                Team team = this.teamManager.getTeam("RED");
                                team.setSpawnLocation(block.getLocation());
                            }
                            else if (block.getData() == 7) { // gray team
                                Team team = this.teamManager.getTeam("GRAY");
                                team.setSpawnLocation(block.getLocation());
                            }
                            else if (block.getData() == 2) { // pink team
                                Team team = this.teamManager.getTeam("PINK");
                                team.setSpawnLocation(block.getLocation());
                            }
                            else if (block.getData() == 0) { // white team
                                Team team = this.teamManager.getTeam("WHITE");
                                team.setSpawnLocation(block.getLocation());
                            }
                            else if (block.getData() == 3) { // aqua team
                                Team team = this.teamManager.getTeam("AQUA");
                                team.setSpawnLocation(block.getLocation());
                            }
                            else if (block.getData() == 8) { // yellow team (light gray stained clay)
                                Team team = this.teamManager.getTeam("YELLOW");
                                team.setSpawnLocation(block.getLocation());
                            }
                            else if (block.getData() == 5) { // green team
                                Team team = this.teamManager.getTeam("GREEN");
                                team.setSpawnLocation(block.getLocation());
                            }
                            else if (block.getData() == 11) { // blue team
                                Team team = this.teamManager.getTeam("BLUE");
                                team.setSpawnLocation(block.getLocation());
                            }
                            break;
                    }
                }
            }
        }
    }

    public void loadMap() throws IOException {
        File defaultSchematic = new File(this.instance.getDataFolder().getAbsolutePath() + "/schematics/" + "Speedway" + ".schematic");

        ClipboardFormat clipboardFormat = ClipboardFormat.findByFile(defaultSchematic);

        com.sk89q.worldedit.world.World bukkitWorld = new BukkitWorld(this.getWorld());

        assert clipboardFormat != null;
        ClipboardReader reader = clipboardFormat.getReader(new FileInputStream(defaultSchematic));
        Clipboard clipboard = reader.read(bukkitWorld.getWorldData());

        EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(bukkitWorld, -1);
        Operation operation = new ClipboardHolder(clipboard, bukkitWorld.getWorldData()).createPaste(editSession, bukkitWorld.getWorldData()).to(new Vector(this.center.getX(), this.center.getY(), this.center.getZ())).ignoreAirBlocks(true).build();

        try {
            Operations.complete(operation);
            editSession.flushQueue();
        } catch (WorldEditException e) {
            e.printStackTrace();
        }
    }

    public GameState getGameState() {
        return this.gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public World getWorld() {
        return world;
    }

    public Location getCenter() {
        return center;
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
        return this.gamePlayers;
    }
}

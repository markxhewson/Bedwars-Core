package xyz.lotho.me.bedwars.managers.world;

import com.google.common.util.concurrent.AtomicDouble;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.session.ClipboardHolder;
import org.bukkit.Location;
import org.bukkit.block.Block;
import xyz.lotho.me.bedwars.Bedwars;
import xyz.lotho.me.bedwars.generators.Generator;
import xyz.lotho.me.bedwars.generators.GeneratorType;
import xyz.lotho.me.bedwars.managers.game.Game;
import xyz.lotho.me.bedwars.managers.team.Team;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

public class WorldManager {

    private final Bedwars instance;
    private final Game game;

    public WorldManager(Bedwars instance, Game game) {
        this.instance = instance;
        this.game = game;
    }

    public Team getNearestBed(Location location) {
        AtomicDouble check = new AtomicDouble(0);
        AtomicReference<Team> nearestTeam = new AtomicReference<>();

        this.game.getTeamManager().getTeamsMap().forEach((teamName, team) -> {
            double locationDiff = location.distance(team.getSpawnLocation());

            if (check.get() == 0) check.set(locationDiff);

            if (locationDiff <= check.get()) {
                check.set(locationDiff);
                nearestTeam.set(team);
            }
        });

        return nearestTeam.get();
    }

    public void loadMap() throws IOException {
        File defaultSchematic = new File(this.instance.getDataFolder().getAbsolutePath() + "/schematics/" + "Speedway" + ".schematic");

        ClipboardFormat clipboardFormat = ClipboardFormat.findByFile(defaultSchematic);

        com.sk89q.worldedit.world.World bukkitWorld = new BukkitWorld(this.game.getWorld());

        assert clipboardFormat != null;
        ClipboardReader reader = clipboardFormat.getReader(new FileInputStream(defaultSchematic));
        Clipboard clipboard = reader.read(bukkitWorld.getWorldData());

        EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(bukkitWorld, -1);
        Operation operation = new ClipboardHolder(clipboard, bukkitWorld.getWorldData()).createPaste(editSession, bukkitWorld.getWorldData()).to(new Vector(this.game.getLobbyLocation().getX(), this.game.getLobbyLocation().getY(), this.game.getLobbyLocation().getZ())).ignoreAirBlocks(true).build();

        try {
            Operations.complete(operation);
            editSession.flushQueue();
        } catch (WorldEditException e) {
            e.printStackTrace();
        }
    }

    public void handleMapSetup() {
        int topBlockX = (Math.max(this.game.getCornerOne().getBlockX(), this.game.getCornerTwo().getBlockX()));
        int bottomBlockX = (Math.min(this.game.getCornerOne().getBlockX(), this.game.getCornerTwo().getBlockX()));

        int topBlockY = (Math.max(this.game.getCornerOne().getBlockY(), this.game.getCornerTwo().getBlockY()));
        int bottomBlockY = (Math.min(this.game.getCornerOne().getBlockY(), this.game.getCornerTwo().getBlockY()));

        int topBlockZ = (Math.max(this.game.getCornerOne().getBlockZ(), this.game.getCornerTwo().getBlockZ()));
        int bottomBlockZ = (Math.min(this.game.getCornerOne().getBlockZ(), this.game.getCornerTwo().getBlockZ()));

        for(int x = bottomBlockX; x <= topBlockX; x++) {
            for(int z = bottomBlockZ; z <= topBlockZ; z++) {
                for(int y = bottomBlockY; y <= topBlockY; y++) {
                    Block block = this.game.getWorld().getBlockAt(x, y, z);

                    switch (block.getType()) {
                        case WOOL:
                            if (block.getData() == 3) { // diamond generator, light blue wool
                                Generator generator = new Generator(this.instance, this.game, block.getLocation(), GeneratorType.DIAMOND, false);
                                this.game.getGenerators().add(generator);
                                // block.setType(Material.AIR);
                            }
                            else if (block.getData() == 13) { // emerald generator, dark green wool
                                Generator generator = new Generator(this.instance, this.game, block.getLocation(), GeneratorType.EMERALD, false);
                                this.game.getGenerators().add(generator);
                                // block.setType(Material.AIR);
                            }
                            else if (block.getData() == 12) { // iron generator, brown wool
                                Generator generator = new Generator(this.instance, this.game, block.getLocation(), GeneratorType.IRON, true);
                                this.game.getGenerators().add(generator);
                                // block.setType(Material.AIR);
                            }
                            else if (block.getData() == 1) { // gold generator, orange wool
                                Generator generator = new Generator(this.instance, this.game, block.getLocation(), GeneratorType.GOLD, true);
                                this.game.getGenerators().add(generator);
                                // block.setType(Material.AIR);
                            }
                            break;

                        case STAINED_CLAY:
                            if (block.getData() == 1) { // red team (orange stained clay)
                                Team team = this.game.getTeamManager().getTeam("RED");
                                team.setSpawnLocation(block.getLocation());
                            }
                            else if (block.getData() == 7) { // gray team
                                Team team = this.game.getTeamManager().getTeam("GRAY");
                                team.setSpawnLocation(block.getLocation());
                            }
                            else if (block.getData() == 2) { // pink team
                                Team team = this.game.getTeamManager().getTeam("PINK");
                                team.setSpawnLocation(block.getLocation());
                            }
                            else if (block.getData() == 0) { // white team
                                Team team = this.game.getTeamManager().getTeam("WHITE");
                                team.setSpawnLocation(block.getLocation());
                            }
                            else if (block.getData() == 3) { // aqua team
                                Team team = this.game.getTeamManager().getTeam("AQUA");
                                team.setSpawnLocation(block.getLocation());
                            }
                            else if (block.getData() == 8) { // yellow team (light gray stained clay)
                                Team team = this.game.getTeamManager().getTeam("YELLOW");
                                team.setSpawnLocation(block.getLocation());
                            }
                            else if (block.getData() == 5) { // green team
                                Team team = this.game.getTeamManager().getTeam("GREEN");
                                team.setSpawnLocation(block.getLocation());
                            }
                            else if (block.getData() == 11) { // blue team
                                Team team = this.game.getTeamManager().getTeam("BLUE");
                                team.setSpawnLocation(block.getLocation());
                            }
                            break;
                    }
                }
            }
        }
    }
}
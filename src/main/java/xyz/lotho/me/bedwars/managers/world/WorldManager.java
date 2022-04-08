package xyz.lotho.me.bedwars.managers.world;

import com.boydti.fawe.FaweAPI;
import com.boydti.fawe.util.TaskManager;
import com.google.common.util.concurrent.AtomicDouble;
import com.sk89q.worldedit.*;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.blocks.BlockID;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.extent.clipboard.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.regions.CuboidRegion;
import net.minecraft.server.v1_8_R3.EntityLiving;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.entity.Villager;
import xyz.lotho.me.bedwars.Bedwars;
import xyz.lotho.me.bedwars.generators.Generator;
import xyz.lotho.me.bedwars.generators.GeneratorType;
import xyz.lotho.me.bedwars.managers.game.Game;
import xyz.lotho.me.bedwars.managers.team.Team;
import xyz.lotho.me.bedwars.util.Chat;

import javax.annotation.Nullable;
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

            if (check.get() == 0) {
                check.set(locationDiff);
            }

            if (locationDiff <= check.get()) {
                check.set(locationDiff);
                nearestTeam.set(team);
            }
        });

        return nearestTeam.get();
    }

    public void loadMap(String mapName) {
        File mapSchematic = new File(this.instance.getDataFolder().getAbsolutePath() + "/schematics/" + mapName + ".schematic");
        pasteSchematic(this.game.getLobbyLocation(), mapSchematic);
    }

    public void pasteSchematic(Location location, File file) {
        long startTime = System.currentTimeMillis();

        ClipboardFormat clipboardFormat = ClipboardFormats.findByFile(file);

        if (clipboardFormat == null) {
            System.out.println("[ERROR] Schematic file could not be found for map " + file.getName());
            return;
        }

        try {
            EditSession editSession = clipboardFormat.load(file).paste(FaweAPI.getWorld(location.getWorld().getName()), BukkitUtil.toVector(location), false, true, null);
            editSession.commit();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(System.currentTimeMillis() - startTime + "ms to load map");
    }

    public void resetMap() {
        long startTime = System.currentTimeMillis();

        Vector cornerOne = new Vector(this.game.getCornerOne().getBlockX(), this.game.getCornerOne().getBlockY(), this.game.getCornerOne().getBlockZ());
        Vector cornerTwo = new Vector(this.game.getCornerTwo().getBlockX(), this.game.getCornerTwo().getBlockY(), this.game.getCornerTwo().getBlockZ());

        CuboidRegion cuboidRegion = new CuboidRegion(cornerOne, cornerTwo);

        EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(FaweAPI.getWorld(this.game.getWorld().getName()), -1);
        editSession.setBlocks(cuboidRegion, new BaseBlock(BlockID.AIR));

        System.out.println(System.currentTimeMillis() - startTime + "ms to reset map");
    }

    public void handleMapSetup() {
        long startTime = System.currentTimeMillis();

        Vector cornerOne = new Vector(this.game.getCornerOne().getBlockX(), this.game.getCornerOne().getBlockY(), this.game.getCornerOne().getBlockZ());
        Vector cornerTwo = new Vector(this.game.getCornerTwo().getBlockX(), this.game.getCornerTwo().getBlockY(), this.game.getCornerTwo().getBlockZ());

        CuboidRegion cuboidRegion = new CuboidRegion(cornerOne, cornerTwo);

        for (BlockVector blockVector : cuboidRegion) {
            Block block = this.game.getWorld().getBlockAt(blockVector.getBlockX(), blockVector.getBlockY(), blockVector.getBlockZ());

            switch (block.getType()) {
                case WOOL:
                    if (block.getData() == 3) { // diamond generator, light blue wool
                        Generator generator = new Generator(this.instance, this.game, block.getLocation(), GeneratorType.DIAMOND, false);
                        this.game.getGenerators().add(generator);
                        block.setType(Material.AIR);
                    }
                    else if (block.getData() == 13) { // emerald generator, dark green wool
                        Generator generator = new Generator(this.instance, this.game, block.getLocation(), GeneratorType.EMERALD, false);
                        this.game.getGenerators().add(generator);
                        block.setType(Material.AIR);
                    }
                    else if (block.getData() == 12) { // iron generator, brown wool
                        Generator generator = new Generator(this.instance, this.game, block.getLocation(), GeneratorType.IRON, true);
                        this.game.getGenerators().add(generator);
                        block.setType(Material.AIR);
                    }
                    else if (block.getData() == 1) { // gold generator, orange wool
                        Generator generator = new Generator(this.instance, this.game, block.getLocation(), GeneratorType.GOLD, true);
                        this.game.getGenerators().add(generator);
                        block.setType(Material.AIR);
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

                case SPONGE:
                    if (block.getData() == 0) { // upgrades shop
                        block.setType(Material.AIR);
                        Villager villager = this.game.getWorld().spawn(block.getLocation().add(0.5, 0, 0.5), Villager.class);

                        villager.setCustomNameVisible(true);
                        villager.setCustomName(Chat.color("&bUpgrades Shop"));
                        villager.setCanPickupItems(false);
                        villager.setProfession(Villager.Profession.LIBRARIAN);

                        EntityLiving handle = ((CraftLivingEntity) villager).getHandle();
                        handle.getDataWatcher().watch(15, (byte) 0);
                    }
                    else if (block.getData() == 1) { // item shop
                        block.setType(Material.AIR);
                        Villager villager = this.game.getWorld().spawn(block.getLocation().add(0.5, 0, 0.5), Villager.class);

                        villager.setCustomNameVisible(true);
                        villager.setCustomName(Chat.color("&aItem Shop"));
                        villager.setCanPickupItems(false);
                        villager.setProfession(Villager.Profession.BLACKSMITH);

                        EntityLiving handle = ((CraftLivingEntity) villager).getHandle();
                        handle.getDataWatcher().watch(15, (byte) 0);
                    }
            }
        }

        System.out.println(System.currentTimeMillis() - startTime + "ms to setup map");
    }
}

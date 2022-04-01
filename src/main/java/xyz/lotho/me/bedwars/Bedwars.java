package xyz.lotho.me.bedwars;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.lotho.me.bedwars.command.KaboomCommand;
import xyz.lotho.me.bedwars.command.NickCommand;
import xyz.lotho.me.bedwars.command.StartGameCommand;
import xyz.lotho.me.bedwars.listeners.BlockBreakListener;
import xyz.lotho.me.bedwars.listeners.InventoryClickListener;
import xyz.lotho.me.bedwars.listeners.PlayerInteractListener;
import xyz.lotho.me.bedwars.managers.disguise.DisguiseManager;
import xyz.lotho.me.bedwars.managers.game.GameManager;
import xyz.lotho.me.bedwars.util.disguise.HTTPUtility;
import xyz.lotho.me.bedwars.util.disguise.NMSHelper;
import xyz.lotho.me.bedwars.util.world.VoidWorldGenerator;

import java.util.Arrays;

public final class Bedwars extends JavaPlugin {

    private final DisguiseManager disguiseManager = new DisguiseManager(this);
    private final NMSHelper nmsHelper = new NMSHelper();
    private final HTTPUtility httpUtility = new HTTPUtility(this);

    private final GameManager gameManager = new GameManager(this);

    private World gameWorld;
    private Location lastGame;

    @Override
    public void onEnable() {
        this.initCommands();
        this.initListeners();

        if (this.getServer().getWorld("games") == null) {
            WorldCreator worldCreator = new WorldCreator("games");
            worldCreator.generator(new VoidWorldGenerator());
            worldCreator.createWorld();
        }

        this.gameWorld = this.getServer().getWorld("games");
        this.lastGame = new Location(this.gameWorld, 0, 150, 0);
    }

    @Override
    public void onDisable() {

    }

    public void initCommands() {
        this.getCommand("kaboom").setExecutor(new KaboomCommand(this));
        this.getCommand("nick").setExecutor(new NickCommand(this));
        this.getCommand("startgame").setExecutor(new StartGameCommand(this));
    }

    public void initListeners() {
        Arrays.asList(
                new PlayerInteractListener(this),
                new InventoryClickListener(this),
                new BlockBreakListener(this)
        ).forEach(listener -> this.getServer().getPluginManager().registerEvents(listener, this));
    }

    public World getGameWorld() {
        return this.gameWorld;
    }

    public Location getLastGame() {
        return this.lastGame;
    }

    public DisguiseManager getDisguiseManager() {
        return this.disguiseManager;
    }

    public HTTPUtility getHttpUtility() {
        return this.httpUtility;
    }

    public NMSHelper getNmsHelper() {
        return this.nmsHelper;
    }

    public GameManager getGameManager() {
        return gameManager;
    }
}

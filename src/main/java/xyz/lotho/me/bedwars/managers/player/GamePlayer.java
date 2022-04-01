package xyz.lotho.me.bedwars.managers.player;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.lotho.me.bedwars.Bedwars;
import xyz.lotho.me.bedwars.managers.game.Game;
import xyz.lotho.me.bedwars.managers.teams.Team;
import xyz.lotho.me.bedwars.util.ItemBuilder;

import java.util.UUID;

public class GamePlayer {

    private final Bedwars instance;
    private final Game game;
    private final UUID uuid;

    public GamePlayer(Bedwars instance, Game game, UUID uuid) {
        this.instance = instance;
        this.game = game;
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public void spawn() {
        Player player = this.instance.getServer().getPlayer(this.getUuid());
        if (player == null) return;

        Team team = this.game.getGamePlayerManager().getPlayerTeam(this.getUuid());
        if (team == null) return;

        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.teleport(team.getSpawnLocation());
        player.setGameMode(GameMode.SURVIVAL);

        this.giveGameItems(player);
    }

    public void giveGameItems(Player player) {
        Team team = this.game.getGamePlayerManager().getPlayerTeam(this.getUuid());
        if (team == null) return;

        player.getInventory().setItem(0, new ItemBuilder(Material.WOOD_SWORD).build());

        player.getInventory().setHelmet(new ItemBuilder(Material.LEATHER_HELMET).setArmorColor(team.getArmorColor()).build());
        player.getInventory().setChestplate(new ItemBuilder(Material.LEATHER_CHESTPLATE).setArmorColor(team.getArmorColor()).build());

        // todo: check if armor upgrades
        player.getInventory().setLeggings(new ItemBuilder(Material.LEATHER_LEGGINGS).setArmorColor(team.getArmorColor()).build());
        player.getInventory().setBoots(new ItemBuilder(Material.LEATHER_BOOTS).setArmorColor(team.getArmorColor()).build());
    }
}

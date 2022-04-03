package xyz.lotho.me.bedwars.managers.player;

import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import xyz.lotho.me.bedwars.Bedwars;
import xyz.lotho.me.bedwars.managers.game.Game;
import xyz.lotho.me.bedwars.managers.team.Team;
import xyz.lotho.me.bedwars.util.Chat;
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

    public void sendTitle(Player player, String title, String subtitle) {
        IChatBaseComponent titleComponent = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + Chat.color(title) + "\"}");
        IChatBaseComponent subtitleComponent = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + Chat.color(subtitle) + "\"}");

        PacketPlayOutTitle titlePacket = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, titleComponent);
        PacketPlayOutTitle subTitlePacket = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, subtitleComponent);
        PacketPlayOutTitle length = new PacketPlayOutTitle(5, 50, 5);

        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(titlePacket);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(subTitlePacket);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(length);
    }

    public void kill() {
        Player killed = this.instance.getServer().getPlayer(this.getUuid());
        if (killed == null) return;

        Team team = this.game.getGamePlayerManager().getPlayerTeam(this.getUuid());
        if (team == null) return;

        if (team.isBedBroken()) {
            this.sendTitle(killed, "&cYOU DIED!", "&eYou will no longer respawn!");
            killed.teleport(game.getLobbyLocation());
            killed.setGameMode(GameMode.SPECTATOR);

            game.getGamePlayerManager().getPlayerMap().forEach((uuid, gamePlayer) -> {
                Player player = this.instance.getServer().getPlayer(gamePlayer.getUuid());
                if (player == null) return;

                player.sendMessage(Chat.color(team.getTeamColor() + killed.getName() + " &7died" + " &b&lFINAL KILL!"));
            });
            return;
        }

        killed.setHealth(20);
        killed.teleport(team.getSpawnLocation());
        killed.sendTitle(Chat.color("&cYOU DIED!"), Chat.color("&eYou have respawned!"));

        game.getGamePlayerManager().getPlayerMap().forEach((uuid, gamePlayer) -> {
            Player player = this.instance.getServer().getPlayer(gamePlayer.getUuid());
            if (player == null) return;

            player.sendMessage(Chat.color(team.getTeamColor() + killed.getName() + " &7died"));
        });
    }

    public void killByPlayer(Player killer, Team killerTeam) {
        Player killed = this.instance.getServer().getPlayer(this.getUuid());
        if (killed == null) return;

        Team team = this.game.getGamePlayerManager().getPlayerTeam(this.getUuid());
        if (team == null) return;

        if (team.isBedBroken()) {
            this.sendTitle(killed, "&cYOU DIED!", "&eYou will no longer respawn!");
            killed.teleport(game.getLobbyLocation());
            killed.setGameMode(GameMode.SPECTATOR);

            game.getGamePlayerManager().getPlayerMap().forEach((uuid, gamePlayer) -> {
                Player player = this.instance.getServer().getPlayer(gamePlayer.getUuid());
                if (player == null) return;

                player.sendMessage(Chat.color(team.getTeamColor() + killed.getName() + " &7was &6bested &7by " + killerTeam.getTeamColor() + killer.getName() + " &b&lFINAL KILL!"));
            });
            return;
        }

        killed.setHealth(20);
        killed.teleport(team.getSpawnLocation());
        this.sendTitle(killed, "&cYOU DIED!", "&eYou have respawned!");

        game.getGamePlayerManager().getPlayerMap().forEach((uuid, gamePlayer) -> {
            Player player = this.instance.getServer().getPlayer(gamePlayer.getUuid());
            if (player == null) return;

            player.sendMessage(Chat.color(team.getTeamColor() + killed.getName() + " &7was &6bested &7by " + killerTeam.getTeamColor() + killer.getName()));
        });
    }

    public void spawn() {
        Player player = this.instance.getServer().getPlayer(this.getUuid());
        if (player == null) return;

        Team team = this.game.getGamePlayerManager().getPlayerTeam(this.getUuid());
        if (team == null) return;

        player.getInventory().clear();
        player.teleport(team.getSpawnLocation());
        player.setGameMode(GameMode.SURVIVAL);
        player.setPlayerListName(Chat.color(team.getTeamColor() + "&l" + team.getTeamName().charAt(0) + team.getTeamColor() + " " + player.getName() + ChatColor.RESET));

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

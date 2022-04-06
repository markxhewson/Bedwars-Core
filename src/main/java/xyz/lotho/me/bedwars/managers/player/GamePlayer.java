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
import org.bukkit.scheduler.BukkitRunnable;
import xyz.lotho.me.bedwars.Bedwars;
import xyz.lotho.me.bedwars.managers.game.Game;
import xyz.lotho.me.bedwars.managers.team.Team;
import xyz.lotho.me.bedwars.util.Chat;
import xyz.lotho.me.bedwars.util.ItemBuilder;

import java.util.UUID;

public class GamePlayer {

    private final Bedwars instance;
    private final Game game;
    private Team team;
    private final UUID uuid;

    private boolean ironArmorUpgrade = true;
    private boolean diamondArmorUpgrade = true;

    public GamePlayer(Bedwars instance, Game game, Team team, UUID uuid) {
        this.instance = instance;
        this.game = game;
        this.team = team;
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public Player getPlayer() {
        return this.instance.getServer().getPlayer(this.getUuid());
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

    public void kill(boolean finalKill, GamePlayer killer) {
        if (this.getPlayer() == null) return;
        Player killed = this.getPlayer();

        killed.getInventory().clear();
        killed.setHealth(20);
        killed.setGameMode(GameMode.SPECTATOR);
        killed.teleport(game.getLobbyLocation());

        if (finalKill) {
            this.sendTitle(killed, "&cYOU DIED!", "&eYou will no longer respawn!");

            game.getGamePlayerManager().getPlayerMap().forEach((uuid, gamePlayer) -> {
                Player player = this.instance.getServer().getPlayer(gamePlayer.getUuid());
                if (player == null) return;

                if (killer == null) player.sendMessage(Chat.color(team.getTeamColor() + killed.getName() + " &7died." + " &b&lFINAL KILL!"));
                else player.sendMessage(Chat.color(team.getTeamColor() + killed.getName() + " &7was &6bested &7by " + killer.getTeam().getTeamColor() + killer.getPlayer().getName() + "&7. &b&lFINAL KILL!"));
            });
        }
        else {
            new BukkitRunnable() {
                int countdown = 5;

                @Override
                public void run() {
                    if (countdown <= 0) {
                        killed.setGameMode(GameMode.SURVIVAL);
                        killed.teleport(team.getSpawnLocation());
                        GamePlayer.this.giveGameItems(killed);
                        this.cancel();
                    } else {
                        GamePlayer.this.sendTitle(killed, "&cYOU DIED!", "&eYou will respawn in &c" + countdown + " &eseconds!");
                        countdown--;
                    }
                }
            }.runTaskTimer(this.instance, 20, 20);

            game.getGamePlayerManager().getPlayerMap().forEach((uuid, gamePlayer) -> {
                Player player = this.instance.getServer().getPlayer(gamePlayer.getUuid());
                if (player == null) return;

                if (killer == null) player.sendMessage(Chat.color(team.getTeamColor() + killed.getName() + " &7died."));
                else player.sendMessage(Chat.color(team.getTeamColor() + killed.getName() + " &7was &6bested &7by " + killer.getTeam().getTeamColor() + killer.getPlayer().getName() + "&7."));
            });
        }
    }

    public void spawn() {
        if (this.getPlayer() == null) return;
        if (this.getTeam() == null) return;

        Player player = this.getPlayer();

        player.getInventory().clear();
        player.teleport(team.getSpawnLocation());
        player.setGameMode(GameMode.SURVIVAL);
        player.setPlayerListName(Chat.color(team.getTeamColor() + "&l" + team.getTeamName().charAt(0) + team.getTeamColor() + " " + player.getName() + ChatColor.RESET));

        this.giveGameItems(player);
    }

    public void giveGameItems(Player player) {
        if (this.getTeam() == null) return;

        player.getInventory().setItem(0, new ItemBuilder(Material.WOOD_SWORD).setUnbreakable(true).build());

        player.getInventory().setHelmet(new ItemBuilder(Material.LEATHER_HELMET).setUnbreakable(true).setArmorColor(team.getArmorColor()).build());
        player.getInventory().setChestplate(new ItemBuilder(Material.LEATHER_CHESTPLATE).setUnbreakable(true).setArmorColor(team.getArmorColor()).build());

        if (this.hasIronArmorUpgrade()) {
            if (this.hasDiamondArmorUpgrade()) {
                player.getInventory().setLeggings(new ItemBuilder(Material.DIAMOND_LEGGINGS).setUnbreakable(true).build());
                player.getInventory().setBoots(new ItemBuilder(Material.DIAMOND_BOOTS).setUnbreakable(true).build());
            } else {
                player.getInventory().setLeggings(new ItemBuilder(Material.IRON_LEGGINGS).setUnbreakable(true).build());
                player.getInventory().setBoots(new ItemBuilder(Material.IRON_BOOTS).setUnbreakable(true).build());
            }
        }
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Team getTeam() {
        return team;
    }

    public boolean hasIronArmorUpgrade() {
        return ironArmorUpgrade;
    }

    public void setIronArmorUpgrade(boolean ironArmorUpgrade) {
        this.ironArmorUpgrade = ironArmorUpgrade;
    }

    public boolean hasDiamondArmorUpgrade() {
        return diamondArmorUpgrade;
    }

    public void setDiamondArmorUpgrade(boolean diamondArmorUpgrade) {
        this.diamondArmorUpgrade = diamondArmorUpgrade;
    }
}

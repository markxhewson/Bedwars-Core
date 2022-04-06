package xyz.lotho.me.bedwars.managers.player;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.lotho.me.bedwars.Bedwars;
import xyz.lotho.me.bedwars.managers.game.Game;
import xyz.lotho.me.bedwars.managers.team.Team;
import xyz.lotho.me.bedwars.managers.teamupgrades.ReinforcedArmorTier;
import xyz.lotho.me.bedwars.util.Chat;
import xyz.lotho.me.bedwars.util.ItemBuilder;

import java.util.UUID;

public class GamePlayer {

    private final Bedwars instance;
    private final Game game;
    private Team team;
    private final UUID uuid;

    private boolean chainArmorUpgrade = false;
    private boolean ironArmorUpgrade = false;
    private boolean diamondArmorUpgrade = false;

    private int kills = 0;
    private int deaths = 0;

    private boolean finalKilled = false;

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

    public int getItemCount(Material material) {
        int count = 0;

        for (ItemStack itemStack : this.getPlayer().getInventory().getContents()) {
            if (itemStack != null && itemStack.getType() != null) {
                if (itemStack.getType() == material) count += itemStack.getAmount();
            }
        }

        return count;
    }

    public void removeItem(Material material, int count) {
        this.getPlayer().getInventory().removeItem(new ItemStack(material, count));
    }

    public void sendTitle(Player player, String title, String subtitle, int time) {
        IChatBaseComponent titleComponent = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + Chat.color(title) + "\"}");
        IChatBaseComponent subtitleComponent = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + Chat.color(subtitle) + "\"}");

        PacketPlayOutTitle titlePacket = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, titleComponent);
        PacketPlayOutTitle subTitlePacket = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, subtitleComponent);
        PacketPlayOutTitle length = new PacketPlayOutTitle(5, time, 5);

        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(titlePacket);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(subTitlePacket);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(length);
    }

    public void kill(boolean finalKill, GamePlayer killer) {
        if (this.getPlayer() == null) return;
        Player killed = this.getPlayer();

        this.setDeaths(this.getDeaths() + 1);
        if (killer != null) killer.setKills(killer.getKills() + 1);

        killed.getInventory().clear();
        killed.setHealth(20);
        killed.setGameMode(GameMode.SPECTATOR);
        killed.teleport(game.getLobbyLocation());

        if (finalKill) {
            this.sendTitle(killed, "&cYOU DIED!", "&eYou will no longer respawn!", 50);

            if (killer == null) this.game.broadcast(team.getTeamColor() + killed.getName() + " &7died." + " &b&lFINAL KILL!");
            else this.game.broadcast(team.getTeamColor() + killed.getName() + " &7was &6bested &7by " + killer.getTeam().getTeamColor() + killer.getPlayer().getName() + "&7. &b&lFINAL KILL!");

            this.setFinalKilled(true);
        }
        else {
            new BukkitRunnable() {
                int countdown = 5;

                @Override
                public void run() {
                    if (countdown <= 0) {
                        GamePlayer.this.spawn();
                        this.cancel();
                    } else {
                        GamePlayer.this.sendTitle(killed, "&cYOU DIED!", "&eYou will respawn in &c" + countdown + " &eseconds!", 50);
                        countdown--;
                    }
                }
            }.runTaskTimer(this.instance, 0, 20);

            if (killer == null) this.game.broadcast(team.getTeamColor() + killed.getName() + " &7died.");
            else this.game.broadcast(team.getTeamColor() + killed.getName() + " &7was &6bested &7by " + killer.getTeam().getTeamColor() + killer.getPlayer().getName() + "&7.");
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

        this.giveGameItems();

        if (team.hasSharpenedSwords()) team.applySharpness();
    }

    public void giveItem(ItemBuilder itemBuilder) {
        this.getPlayer().getInventory().addItem(itemBuilder.build());
    }

    public void setArmor() {
        Player player = this.getPlayer();

        player.getInventory().setHelmet(new ItemBuilder(Material.LEATHER_HELMET).setUnbreakable(true).setArmorColor(team.getArmorColor()).build());
        player.getInventory().setChestplate(new ItemBuilder(Material.LEATHER_CHESTPLATE).setUnbreakable(true).setArmorColor(team.getArmorColor()).build());

        player.getInventory().setLeggings(new ItemBuilder(Material.LEATHER_LEGGINGS).setUnbreakable(true).setArmorColor(team.getArmorColor()).build());
        player.getInventory().setBoots(new ItemBuilder(Material.LEATHER_BOOTS).setUnbreakable(true).setArmorColor(team.getArmorColor()).build());

        if (this.hasChainArmorUpgrade()) {
            player.getInventory().setLeggings(new ItemBuilder(Material.CHAINMAIL_LEGGINGS).setUnbreakable(true).build());
            player.getInventory().setBoots(new ItemBuilder(Material.CHAINMAIL_BOOTS).setUnbreakable(true).build());
        }

        if (this.hasIronArmorUpgrade()) {
            player.getInventory().setLeggings(new ItemBuilder(Material.IRON_LEGGINGS).setUnbreakable(true).build());
            player.getInventory().setBoots(new ItemBuilder(Material.IRON_BOOTS).setUnbreakable(true).build());
        }

        if (this.hasDiamondArmorUpgrade()) {
            player.getInventory().setLeggings(new ItemBuilder(Material.DIAMOND_LEGGINGS).setUnbreakable(true).build());
            player.getInventory().setBoots(new ItemBuilder(Material.DIAMOND_BOOTS).setUnbreakable(true).build());
        }

        if (team.getReinforcedArmorTier() != ReinforcedArmorTier.NONE) {
            team.applyProtection();
        }
    }

    public void giveGameItems() {
        this.getPlayer().getInventory().setItem(0, new ItemBuilder(Material.WOOD_SWORD).setUnbreakable(true).build());

        this.setArmor();
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

    public boolean hasChainArmorUpgrade() {
        return chainArmorUpgrade;
    }

    public void setChainArmorUpgrade(boolean chainArmorUpgrade) {
        this.chainArmorUpgrade = chainArmorUpgrade;
    }

    public boolean isFinalKilled() {
        return finalKilled;
    }

    public void setFinalKilled(boolean finalKilled) {
        this.finalKilled = finalKilled;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }
}

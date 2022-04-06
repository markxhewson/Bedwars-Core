package xyz.lotho.me.bedwars.managers.team;

import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.lotho.me.bedwars.Bedwars;
import xyz.lotho.me.bedwars.managers.game.Game;
import xyz.lotho.me.bedwars.managers.player.GamePlayer;
import xyz.lotho.me.bedwars.managers.teamupgrades.ReinforcedArmorTier;
import xyz.lotho.me.bedwars.util.Chat;

import java.util.ArrayList;

public class Team {

    private final Bedwars instance;
    private final Game game;

    private final String teamName;
    private final ChatColor teamColor;
    private final Color armorColor;
    private final int metaID;

    private boolean sharpenedSwords = false;
    private ReinforcedArmorTier reinforcedArmorTier = ReinforcedArmorTier.NONE;

    private Location spawnLocation;
    private final ArrayList<GamePlayer> teamMembers = new ArrayList<>();

    private final int maxTeamSize = 2;
    private boolean bedBroken = false;

    public Team(Bedwars instance, Game game, String teamName, ChatColor teamColor, Color armorColor, int metaID) {
        this.instance = instance;
        this.game = game;
        this.teamName = teamName;
        this.teamColor = teamColor;
        this.armorColor = armorColor;
        this.metaID = metaID;
    }

    public void loadTeam() {
        this.getTeamMembers().forEach(GamePlayer::spawn);
    }

    public ArrayList<GamePlayer> getAliveMembers() {
        ArrayList<GamePlayer> aliveMembers = new ArrayList<>();

        this.getTeamMembers().forEach(gamePlayer -> {
            if (!gamePlayer.isFinalKilled()) aliveMembers.add(gamePlayer);
        });

        return aliveMembers;
    }

    public void breakBed(GamePlayer breaker) {
        if (this.isBedBroken()) return;

        this.setBedBroken(true);
        Player bedBreaker = this.instance.getServer().getPlayer(breaker.getUuid());
        Team breakerTeam = this.game.getGamePlayerManager().getPlayerTeam(breaker.getUuid());

        this.game.getWorld().playSound(bedBreaker.getLocation(), Sound.ENDERDRAGON_GROWL, 1, 1);
        this.game.broadcast("\n&f&lBED DESTRUCTION > " + this.getTeamColor() + this.getTeamName() + " Bed &7was incinerated by " + breakerTeam.getTeamColor() + bedBreaker.getName() + "\n ");

        this.getTeamMembers().forEach(gamePlayer -> {
            gamePlayer.sendTitle(gamePlayer.getPlayer(), "&cBED DESTROYED!", "&fYou will no longer respawn!", 50);
        });
    }

    public void applySharpness() {
        this.teamMembers.forEach(gamePlayer -> {
            for (ItemStack itemStack : gamePlayer.getPlayer().getInventory().getContents()) {

                if (itemStack != null && itemStack.getType() != Material.AIR && itemStack.hasItemMeta() && itemStack.getType().name().contains("SWORD")) {
                    ItemMeta itemMeta = itemStack.getItemMeta();
                    itemMeta.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
                    itemStack.setItemMeta(itemMeta);
                }
            }
            gamePlayer.getPlayer().updateInventory();
        });
    }

    public void applyProtection() {
        this.teamMembers.forEach(gamePlayer -> {
            for (ItemStack itemStack : gamePlayer.getPlayer().getInventory().getArmorContents()) {
                ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, this.getReinforcedArmorTier().getLevel(), true);
                itemStack.setItemMeta(itemMeta);
            }
            gamePlayer.getPlayer().updateInventory();
        });
    }

    public void broadcast(String message) {
        this.getTeamMembers().forEach((gamePlayer) -> {
            Player player = gamePlayer.getPlayer();
            if (player == null) return;

            player.sendMessage(Chat.color(message));
        });
    }

    public boolean isBedBroken() {
        return this.bedBroken;
    }

    public void setBedBroken(boolean bedBroken) {
        this.bedBroken = bedBroken;
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
        this.game.getWorld().getBlockAt(spawnLocation).setType(Material.AIR);
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

    public boolean hasSharpenedSwords() {
        return sharpenedSwords;
    }

    public void setSharpenedSwords(boolean sharpenedSwords) {
        this.sharpenedSwords = sharpenedSwords;
    }

    public ReinforcedArmorTier getReinforcedArmorTier() {
        return reinforcedArmorTier;
    }

    public void setReinforcedArmorTier(ReinforcedArmorTier reinforcedArmorTier) {
        this.reinforcedArmorTier = reinforcedArmorTier;
    }

    public int getMetaID() {
        return metaID;
    }
}

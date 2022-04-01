package xyz.lotho.me.bedwars.ui;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import xyz.lotho.me.bedwars.Bedwars;
import xyz.lotho.me.bedwars.managers.game.Game;
import xyz.lotho.me.bedwars.managers.player.GamePlayer;
import xyz.lotho.me.bedwars.managers.teams.Team;
import xyz.lotho.me.bedwars.ui.util.Menu;
import xyz.lotho.me.bedwars.util.Chat;
import xyz.lotho.me.bedwars.util.ItemBuilder;

import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;

public class PickTeamMenu extends Menu {

    private final Bedwars instance;
    private final Game game;

    public PickTeamMenu(Bedwars instance, Game game) {
        this.instance = instance;
        this.game = game;
    }

    @Override
    public String getMenuName() {
        return "Pick your Team!";
    }

    @Override
    public int getSlots() {
        return 27;
    }

    @Override
    public void setItems() {
        AtomicInteger startingIndex = new AtomicInteger(10);

        this.game.getTeamManager().getTeamsMap().forEach((teamName, team) -> {
            this.getInventory().setItem(startingIndex.getAndIncrement(),
                    new ItemBuilder(new ItemStack(Material.ANVIL))
                            .setDisplayName(team.getTeamColor() + teamName)
                            .setLore(Collections.singletonList(Chat.color("&7Players: " + team.getTeamMembers().size() + "/1")))
                            .getItem()
            );
        });
    }

    @Override
    public void handleClick(InventoryClickEvent event) throws Exception {
        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();

        String teamName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName());

        Team team = game.getTeamManager().getTeam(teamName);

        if (team.getTeamMembers().size() >= 1) {
            player.sendMessage(Chat.color("&cThis team is full!"));
            return;
        }

        if (game.getGamePlayerManager().getPlayerTeam(player.getUniqueId()) != null) {
            GamePlayer gamePlayer = game.getGamePlayerManager().getPlayer(player.getUniqueId());
            Team currentTeam = game.getGamePlayerManager().getPlayerTeam(player.getUniqueId());

            currentTeam.removeMember(gamePlayer);
        }

        team.addMember(game.getGamePlayerManager().getPlayer(player.getUniqueId()));
        this.open(player);
    }
}

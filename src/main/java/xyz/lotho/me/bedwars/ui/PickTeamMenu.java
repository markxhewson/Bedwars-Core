package xyz.lotho.me.bedwars.ui;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import xyz.lotho.me.bedwars.Bedwars;
import xyz.lotho.me.bedwars.managers.game.Game;
import xyz.lotho.me.bedwars.managers.player.GamePlayer;
import xyz.lotho.me.bedwars.managers.team.Team;
import xyz.lotho.me.bedwars.ui.util.Menu;
import xyz.lotho.me.bedwars.util.Chat;
import xyz.lotho.me.bedwars.util.ItemBuilder;

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
        return 18;
    }

    @Override
    public void setItems() {
        AtomicInteger counter = new AtomicInteger(0);
        this.game.getTeamManager().getTeamsMap().forEach((teamName, team) -> {
            ItemBuilder itemBuilder = new ItemBuilder(Material.ANVIL);
            itemBuilder.setDisplayName(team.getTeamColor() + team.getTeamName());
            itemBuilder.setLore("&7Players: " + team.getTeamMembers().size() + "/" + team.getMaxTeamSize());

            for (GamePlayer gamePlayer : team.getTeamMembers()) {
                Player teamMember = this.instance.getServer().getPlayer(gamePlayer.getUuid());
                itemBuilder.addLore("&6- &7" + teamMember.getName());
            }

            this.getInventory().setItem(counter.getAndIncrement(),
                    itemBuilder.build()
            );
        });
    }

    @Override
    public void handleClick(InventoryClickEvent event) throws Exception {
        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();

        String selectedTeamName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName());
        Team selectedTeam = game.getTeamManager().getTeam(selectedTeamName);

        if (selectedTeam.getTeamMembers().size() >= selectedTeam.getMaxTeamSize()) {
            player.sendMessage(Chat.color("&cThis team is full!"));
            return;
        }

        if (game.getGamePlayerManager().getPlayerTeam(player.getUniqueId()) != null) {
            GamePlayer gamePlayer = game.getGamePlayerManager().getPlayer(player.getUniqueId());
            Team currentTeam = game.getGamePlayerManager().getPlayerTeam(player.getUniqueId());

            currentTeam.removeMember(gamePlayer);
        }

        selectedTeam.addMember(game.getGamePlayerManager().getPlayer(player.getUniqueId()));
        player.sendMessage(Chat.color("&aYou have joined " + selectedTeam.getTeamColor() + selectedTeam.getTeamName() + " &ateam!"));

        this.game.getPickTeamMenu().open(player);
    }
}

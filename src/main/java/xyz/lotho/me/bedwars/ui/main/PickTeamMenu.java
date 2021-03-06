package xyz.lotho.me.bedwars.ui.main;

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
            ItemBuilder itemBuilder = new ItemBuilder(Material.WOOL);

            itemBuilder.setDurability((short) team.getMetaID());
            itemBuilder.setDisplayName(team.getTeamColor() + team.getTeamName());
            itemBuilder.setLore("&7Click to join the " + team.getTeamColor() + team.getTeamName() + " Team&7!", "", "&7Current players (" + team.getTeamMembers().size() + "/" + team.getMaxTeamSize() + "): ");

            if (team.getTeamMembers().isEmpty()) {
                itemBuilder.addLore("&cN/A");
            }

            for (GamePlayer gamePlayer : team.getTeamMembers()) {
                Player teamMember = this.instance.getServer().getPlayer(gamePlayer.getUuid());
                itemBuilder.addLore("&7" + teamMember.getName());
            }

            itemBuilder.addLore("");
            itemBuilder.addLore("&eClick to join!");

            this.getInventory().setItem(
                    counter.getAndIncrement(),
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

        if (selectedTeam == null) {
            return;
        }

        if (selectedTeam.getTeamMembers().size() >= selectedTeam.getMaxTeamSize()) {
            player.sendMessage(Chat.color("&cThis team is full!"));
            return;
        }

        GamePlayer gamePlayer = game.getGamePlayerManager().getPlayer(player.getUniqueId());

        if (gamePlayer.getTeam() != null) {
            gamePlayer.getTeam().removeMember(gamePlayer);
        }

        selectedTeam.addMember(gamePlayer);
        gamePlayer.setTeam(selectedTeam);

        player.sendMessage(Chat.color("&aYou joined the " + selectedTeam.getTeamColor() + selectedTeam.getTeamName() + " &ateam!"));
        this.game.getPickTeamMenu().open(player);
    }
}

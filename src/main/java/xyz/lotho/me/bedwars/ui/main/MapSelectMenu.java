package xyz.lotho.me.bedwars.ui.main;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import xyz.lotho.me.bedwars.Bedwars;
import xyz.lotho.me.bedwars.managers.game.Game;
import xyz.lotho.me.bedwars.ui.util.Menu;
import xyz.lotho.me.bedwars.util.Chat;
import xyz.lotho.me.bedwars.util.ItemBuilder;

import java.util.ArrayList;
import java.util.UUID;

public class MapSelectMenu extends Menu {

    private final Bedwars instance;

    public MapSelectMenu(Bedwars instance) {
        this.instance = instance;
    }

    @Override
    public String getMenuName() {
        return "Select Map";
    }

    @Override
    public int getSlots() {
        return 9;
    }

    @Override
    public void setItems() {
        final ArrayList<String> maps = this.instance.getMapManager().getMaps();
        final Inventory inventory = this.getInventory();

        maps.forEach(mapName -> {
            inventory.addItem(
                    new ItemBuilder(Material.BED).setDisplayName("&a" + mapName).setLore("&7Click to play this map!").build()
            );
        });
    }

    @Override
    public void handleClick(InventoryClickEvent event) throws Exception {
        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();

        String mapName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName());

        player.closeInventory();

        Game game = new Game(this.instance, UUID.randomUUID(), this.instance.getGameWorld(), mapName, this.instance.getLastGame().add(5000, 0, 0));
        this.instance.getGameManager().addGame(game);

        player.sendMessage(Chat.color("&aStarting game..\n&8" + game.getGameUUID()));
    }
}

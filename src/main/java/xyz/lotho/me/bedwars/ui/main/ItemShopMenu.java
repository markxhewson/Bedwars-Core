package xyz.lotho.me.bedwars.ui.main;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import xyz.lotho.me.bedwars.Bedwars;
import xyz.lotho.me.bedwars.managers.game.Game;
import xyz.lotho.me.bedwars.managers.player.GamePlayer;
import xyz.lotho.me.bedwars.ui.categories.*;
import xyz.lotho.me.bedwars.ui.util.Menu;
import xyz.lotho.me.bedwars.util.ItemBuilder;

public class ItemShopMenu extends Menu {

    private final Bedwars instance;
    private final GamePlayer gamePlayer;
    private final Game game;

    public ItemShopMenu(Bedwars instance, GamePlayer gamePlayer, Game game) {
        this.instance = instance;
        this.gamePlayer = gamePlayer;
        this.game = game;
    }

    @Override
    public String getMenuName() {
        return "Item Shop";
    }

    @Override
    public int getSlots() {
        return 54;
    }

    @Override
    public void setItems() {
        Inventory inventory = this.getInventory();

        inventory.setItem(1, new ItemBuilder(Material.HARD_CLAY).setDisplayName("&aBlocks").setLore("&eClick to view!").build());
        inventory.setItem(2, new ItemBuilder(Material.GOLD_SWORD).setDisplayName("&aMelee").setLore("&eClick to view!").build());
        inventory.setItem(3, new ItemBuilder(Material.CHAINMAIL_BOOTS).setDisplayName("&aArmor").setLore("&eClick to view!").build());
        inventory.setItem(4, new ItemBuilder(Material.STONE_PICKAXE).setDisplayName("&aTools").setLore("&eClick to view!").build());
        inventory.setItem(5, new ItemBuilder(Material.BOW).setDisplayName("&aRanged").setLore("&eClick to view!").build());
        inventory.setItem(6, new ItemBuilder(Material.BREWING_STAND_ITEM).setDisplayName("&aPotions").setLore("&eClick to view!").build());
        inventory.setItem(7, new ItemBuilder(Material.TNT).setDisplayName("&aUtility").setLore("&eClick to view!").build());
    }

    @Override
    public void handleClick(InventoryClickEvent e) throws Exception {
        ItemStack itemStack = e.getCurrentItem();
        Player player = (Player) e.getWhoClicked();

        switch (itemStack.getType()) {
            case HARD_CLAY:
                new BlocksShopMenu(this.instance, gamePlayer, game).open(player);
                break;
            case GOLD_SWORD:
                new MeleeShopMenu(this.instance, gamePlayer, game).open(player);
                break;
            case CHAINMAIL_BOOTS:
                new ArmorShopMenu(this.instance, gamePlayer, game).open(player);
                break;
            case STONE_PICKAXE:
                new ToolsShopMenu(this.instance, gamePlayer, game).open(player);
                break;
            case BOW:
                new RangedShopMenu(this.instance, gamePlayer, game).open(player);
                break;
            case BREWING_STAND_ITEM:
                break;
            case TNT:
                new UtilityShopMenu(this.instance, gamePlayer, game).open(player);
                break;
        }
    }
}

package xyz.lotho.me.bedwars.ui.categories;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import xyz.lotho.me.bedwars.Bedwars;
import xyz.lotho.me.bedwars.managers.game.Game;
import xyz.lotho.me.bedwars.managers.player.GamePlayer;
import xyz.lotho.me.bedwars.ui.util.Menu;
import xyz.lotho.me.bedwars.util.Chat;
import xyz.lotho.me.bedwars.util.ItemBuilder;

public class UtilityShopMenu extends Menu {

    private final Bedwars instance;
    private final GamePlayer gamePlayer;
    private final Game game;

    public UtilityShopMenu(Bedwars instance, GamePlayer gamePlayer, Game game) {
        this.instance = instance;
        this.gamePlayer = gamePlayer;
        this.game = game;
    }

    @Override
    public String getMenuName() {
        return "Utility Shop";
    }

    @Override
    public int getSlots() {
        return 54;
    }

    @Override
    public void setItems() {
        Inventory inventory = this.getInventory();

        int ironCount = gamePlayer.getItemCount(Material.IRON_INGOT);
        int goldCount = gamePlayer.getItemCount(Material.GOLD_INGOT);
        int emeraldCount = gamePlayer.getItemCount(Material.EMERALD);

        // nav menu
        inventory.setItem(1, new ItemBuilder(Material.HARD_CLAY).setDisplayName("&aBlocks").setLore("&eClick to view!").build());
        inventory.setItem(2, new ItemBuilder(Material.GOLD_SWORD).setDisplayName("&aMelee").setLore("&eClick to view!").build());
        inventory.setItem(3, new ItemBuilder(Material.CHAINMAIL_BOOTS).setDisplayName("&aArmor").setLore("&eClick to view!").build());
        inventory.setItem(4, new ItemBuilder(Material.STONE_PICKAXE).setDisplayName("&aTools").setLore("&eClick to view!").build());
        inventory.setItem(5, new ItemBuilder(Material.BOW).setDisplayName("&aRanged").setLore("&eClick to view!").build());
        inventory.setItem(6, new ItemBuilder(Material.BREWING_STAND_ITEM).setDisplayName("&aPotions").setLore("&eClick to view!").build());
        inventory.setItem(7, new ItemBuilder(Material.TNT).setDisplayName("&aUtility").setLore("&eClick to view!").build());

        // category items
        inventory.setItem(19, new ItemBuilder(Material.GOLDEN_APPLE).setDisplayName(goldCount < 3 ? "&cGolden Apple" : "&aGolden Apple").setLore("&7Cost: &63 Gold").build());
        inventory.setItem(20, new ItemBuilder(Material.TNT).setDisplayName(goldCount < 4 ? "&cTNT" : "&aTNT").setLore("&7Cost: &64 Gold").build());
        inventory.setItem(21, new ItemBuilder(Material.ENDER_PEARL).setDisplayName(emeraldCount < 4 ? "&cEnderpearl" : "&aEnderpearl").setLore("&7Cost: &24 Emeralds").build());
    }

    @Override
    public void handleClick(InventoryClickEvent e) throws Exception {
        ItemStack itemStack = e.getCurrentItem();
        Player player = (Player) e.getWhoClicked();

        int ironCount = gamePlayer.getItemCount(Material.IRON_INGOT);
        int goldCount = gamePlayer.getItemCount(Material.GOLD_INGOT);
        int emeraldCount = gamePlayer.getItemCount(Material.EMERALD);

        switch (itemStack.getType()) {
            case GOLDEN_APPLE:
                if (goldCount < 3) {
                    player.sendMessage(Chat.color("&cYou cannot afford this!"));
                    return;
                }

                gamePlayer.removeItem(Material.GOLD_INGOT, 3);
                gamePlayer.giveItem(new ItemBuilder(Material.GOLDEN_APPLE));
                break;

            case ENDER_PEARL:
                if (emeraldCount < 4) {
                    player.sendMessage(Chat.color("&cYou cannot afford this!"));
                    return;
                }

                gamePlayer.removeItem(Material.EMERALD, 4);
                gamePlayer.giveItem(new ItemBuilder(Material.ENDER_PEARL));
                break;

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
                if (e.getSlot() == 7) new UtilityShopMenu(this.instance, gamePlayer, game).open(player);
                else {
                    if (goldCount < 4) {
                        player.sendMessage(Chat.color("&cYou cannot afford this!"));
                        return;
                    }

                    gamePlayer.removeItem(Material.GOLD_INGOT, 4);
                    gamePlayer.giveItem(new ItemBuilder(Material.TNT));
                }
                break;
        }
    }
}

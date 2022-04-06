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

public class ArmorShopMenu extends Menu {

    private final Bedwars instance;
    private final GamePlayer gamePlayer;
    private final Game game;

    public ArmorShopMenu(Bedwars instance, GamePlayer gamePlayer, Game game) {
        this.instance = instance;
        this.gamePlayer = gamePlayer;
        this.game = game;
    }

    @Override
    public String getMenuName() {
        return "Armor Shop";
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
        inventory.setItem(19, new ItemBuilder(Material.CHAINMAIL_BOOTS).setDisplayName(ironCount < 30 ? "&cPermanent Chainmail Armor" : "&aPermanent Chainmail Armor").setLore("&7Cost: &f30 Iron").build());
        inventory.setItem(20, new ItemBuilder(Material.IRON_BOOTS).setDisplayName(goldCount < 12 ? "&cPermanent Iron Armor" : "&aPermanent Iron Armor").setLore("&7Cost: &612 Gold").build());
        inventory.setItem(21, new ItemBuilder(Material.DIAMOND_BOOTS).setDisplayName(emeraldCount < 6 ? "&cPermanent Diamond Armor" : "&aPermanent Diamond Armor").setLore("&7Cost: &26 Emeralds").build());
    }

    @Override
    public void handleClick(InventoryClickEvent e) throws Exception {
        ItemStack itemStack = e.getCurrentItem();
        Player player = (Player) e.getWhoClicked();

        int ironCount = gamePlayer.getItemCount(Material.IRON_INGOT);
        int goldCount = gamePlayer.getItemCount(Material.GOLD_INGOT);
        int emeraldCount = gamePlayer.getItemCount(Material.EMERALD);

        switch (itemStack.getType()) {
            case IRON_BOOTS:
                if (goldCount < 12) {
                    player.sendMessage(Chat.color("&cYou cannot afford this!"));
                    return;
                }
                gamePlayer.setIronArmorUpgrade(true);
                gamePlayer.removeItem(Material.GOLD_INGOT, 12);
                gamePlayer.setArmor();
                break;

            case DIAMOND_BOOTS:
                if (emeraldCount < 6) {
                    player.sendMessage(Chat.color("&cYou cannot afford this!"));
                    return;
                }
                gamePlayer.setDiamondArmorUpgrade(true);
                gamePlayer.removeItem(Material.EMERALD, 6);
                gamePlayer.setArmor();
                break;

            case HARD_CLAY:
                new BlocksShopMenu(this.instance, gamePlayer, game).open(player);
                break;
            case GOLD_SWORD:
                new MeleeShopMenu(this.instance, gamePlayer, game).open(player);
            case CHAINMAIL_BOOTS:
                if (e.getSlot() == 4) new ArmorShopMenu(this.instance, gamePlayer, game).open(player);
                else {
                    if (ironCount < 30) {
                        player.sendMessage(Chat.color("&cYou cannot afford this!"));
                        return;
                    }
                    gamePlayer.setChainArmorUpgrade(true);
                    gamePlayer.removeItem(Material.IRON_INGOT, 30);
                    gamePlayer.setArmor();
                    break;
                }
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

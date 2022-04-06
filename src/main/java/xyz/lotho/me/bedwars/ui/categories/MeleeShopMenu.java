package xyz.lotho.me.bedwars.ui.categories;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
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

public class MeleeShopMenu extends Menu {

    private final Bedwars instance;
    private final GamePlayer gamePlayer;
    private final Game game;

    public MeleeShopMenu(Bedwars instance, GamePlayer gamePlayer, Game game) {
        this.instance = instance;
        this.gamePlayer = gamePlayer;
        this.game = game;
    }

    @Override
    public String getMenuName() {
        return "Melee Shop";
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
        inventory.setItem(19, new ItemBuilder(Material.STONE_SWORD).setDisplayName(ironCount < 10 ? "&cStone Sword" : "&aStone Sword").setLore("&7Cost: &f10 Iron").build());
        inventory.setItem(20, new ItemBuilder(Material.IRON_SWORD).setDisplayName(goldCount < 7 ? "&cIron Sword" : "&aIron Sword").setLore("&7Cost: &67 Gold").build());
        inventory.setItem(21, new ItemBuilder(Material.DIAMOND_SWORD).setDisplayName(emeraldCount < 3 ? "&cDiamond Sword" : "&aDiamond Sword").setLore("&7Cost: &23 Emeralds").build());
        inventory.setItem(22, new ItemBuilder(Material.STICK).addUnsafeEnchant(Enchantment.KNOCKBACK, 1).setDisplayName(goldCount < 5 ? "&cKnockback Stick" : "&aKnockback Stick").setLore("&7Cost: &65 Gold").build());
    }

    @Override
    public void handleClick(InventoryClickEvent e) throws Exception {
        ItemStack itemStack = e.getCurrentItem();
        Player player = (Player) e.getWhoClicked();

        int ironCount = gamePlayer.getItemCount(Material.IRON_INGOT);
        int goldCount = gamePlayer.getItemCount(Material.GOLD_INGOT);
        int emeraldCount = gamePlayer.getItemCount(Material.EMERALD);

        switch (itemStack.getType()) {
            case STONE_SWORD:
                if (ironCount < 10) {
                    player.sendMessage(Chat.color("&cYou cannot afford this!"));
                    return;
                }

                gamePlayer.removeItem(Material.IRON_INGOT, 10);
                gamePlayer.giveItem(new ItemBuilder(Material.STONE_SWORD).setUnbreakable(true));
                if (gamePlayer.getTeam().hasSharpenedSwords()) gamePlayer.getTeam().applySharpness();
                break;

            case IRON_SWORD:
                if (goldCount < 7) {
                    player.sendMessage(Chat.color("&cYou cannot afford this!"));
                    return;
                }

                gamePlayer.removeItem(Material.GOLD_INGOT, 7);
                gamePlayer.giveItem(new ItemBuilder(Material.IRON_SWORD).setUnbreakable(true));
                if (gamePlayer.getTeam().hasSharpenedSwords()) gamePlayer.getTeam().applySharpness();
                break;

            case DIAMOND_SWORD:
                if (emeraldCount < 3) {
                    player.sendMessage(Chat.color("&cYou cannot afford this!"));
                    return;
                }

                gamePlayer.removeItem(Material.EMERALD, 3);
                gamePlayer.giveItem(new ItemBuilder(Material.DIAMOND_SWORD).setUnbreakable(true));
                if (gamePlayer.getTeam().hasSharpenedSwords()) gamePlayer.getTeam().applySharpness();
                break;

            case STICK:
                if (goldCount < 10) {
                    player.sendMessage(Chat.color("&cYou cannot afford this!"));
                    return;
                }

                gamePlayer.removeItem(Material.GOLD_INGOT, 5);
                gamePlayer.giveItem(new ItemBuilder(Material.STICK).addUnsafeEnchant(Enchantment.KNOCKBACK, 1).setUnbreakable(true));
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
                new UtilityShopMenu(this.instance, gamePlayer, game).open(player);
                break;
        }
    }
}

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

public class BlocksShopMenu extends Menu {

    private final Bedwars instance;
    private final GamePlayer gamePlayer;
    private final Game game;

    public BlocksShopMenu(Bedwars instance, GamePlayer gamePlayer, Game game) {
        this.instance = instance;
        this.gamePlayer = gamePlayer;
        this.game = game;
    }

    @Override
    public String getMenuName() {
        return "Blocks Shop";
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
        inventory.setItem(19, new ItemBuilder(Material.WOOL).setDisplayName(ironCount < 4 ? "&cWool" : "&aWool").setDurability((short) gamePlayer.getTeam().getMetaID()).setAmount(16).setLore("&7Cost: &f4 Iron").build());
        inventory.setItem(20, new ItemBuilder(Material.HARD_CLAY).setDisplayName(ironCount < 12 ? "&cHardened Clay" : "&aHardened Clay").setAmount(16).setLore("&7Cost: &f12 Iron").build());
        inventory.setItem(21, new ItemBuilder(Material.STAINED_GLASS).setDisplayName(ironCount < 12 ? "&cBlast-Proof Glass" : "&aBlast-Proof Glass").setAmount(4).setLore("&7Cost: &f12 Iron").build());
        inventory.setItem(22, new ItemBuilder(Material.ENDER_STONE).setDisplayName(ironCount < 24 ? "&cEnd Stone" : "&aEnd Stone").setAmount(12).setLore("&7Cost: &f24 Iron").build());
        inventory.setItem(23, new ItemBuilder(Material.LADDER).setDisplayName(ironCount < 4 ? "&cLadder" : "&aLadder").setAmount(16).setLore("&7Cost: &f4 Iron").build());
        inventory.setItem(24, new ItemBuilder(Material.WOOD).setDisplayName(goldCount < 4 ? "&cOak Wood Planks" : "&aOak Wood Planks").setAmount(16).setLore("&7Cost: &64 Gold").build());
        inventory.setItem(25, new ItemBuilder(Material.OBSIDIAN).setDisplayName(emeraldCount < 4 ? "&cObsidian" : "&aObsidian").setAmount(4).setLore("&7Cost: &24 Emeralds").build());
    }

    @Override
    public void handleClick(InventoryClickEvent e) throws Exception {
        ItemStack itemStack = e.getCurrentItem();
        Player player = (Player) e.getWhoClicked();

        int ironCount = gamePlayer.getItemCount(Material.IRON_INGOT);
        int goldCount = gamePlayer.getItemCount(Material.GOLD_INGOT);
        int emeraldCount = gamePlayer.getItemCount(Material.EMERALD);

        switch (itemStack.getType()) {
            case WOOL:
                if (ironCount < 4) {
                    player.sendMessage(Chat.color("&cYou cannot afford this!"));
                    return;
                }

                gamePlayer.removeItem(Material.IRON_INGOT, 4);
                gamePlayer.giveItem(new ItemBuilder(Material.WOOL).setDurability((short) gamePlayer.getTeam().getMetaID()).setAmount(16));
                break;

            case STAINED_GLASS_PANE:
                if (ironCount < 12) {
                    player.sendMessage(Chat.color("&cYou cannot afford this!"));
                    return;
                }

                gamePlayer.removeItem(Material.IRON_INGOT, 4);
                gamePlayer.giveItem(new ItemBuilder(Material.STAINED_GLASS).setAmount(4));
                break;

            case ENDER_STONE:
                if (ironCount < 24) {
                    player.sendMessage(Chat.color("&cYou cannot afford this!"));
                    return;
                }

                gamePlayer.removeItem(Material.IRON_INGOT, 24);
                gamePlayer.giveItem(new ItemBuilder(Material.ENDER_STONE).setAmount(12));
                break;

            case LADDER:
                if (ironCount < 4) {
                    player.sendMessage(Chat.color("&cYou cannot afford this!"));
                    return;
                }

                gamePlayer.removeItem(Material.IRON_INGOT, 4);
                gamePlayer.giveItem(new ItemBuilder(Material.LADDER).setAmount(16));
                break;

            case WOOD:
                if (goldCount < 4) {
                    player.sendMessage(Chat.color("&cYou cannot afford this!"));
                    return;
                }

                gamePlayer.removeItem(Material.GOLD_INGOT, 4);
                gamePlayer.giveItem(new ItemBuilder(Material.WOOD).setAmount(16));
                break;

            case OBSIDIAN:
                if (emeraldCount < 12) {
                    player.sendMessage(Chat.color("&cYou cannot afford this!"));
                    return;
                }

                gamePlayer.removeItem(Material.EMERALD, 4);
                gamePlayer.giveItem(new ItemBuilder(Material.OBSIDIAN).setAmount(4));
                break;

            case HARD_CLAY:
                if (e.getSlot() == 1) new BlocksShopMenu(this.instance, gamePlayer, game).open(player);
                else {
                    if (ironCount < 12) {
                        player.sendMessage(Chat.color("&cYou cannot afford this!"));
                        return;
                    }

                    gamePlayer.removeItem(Material.IRON_INGOT, 12);
                    gamePlayer.giveItem(new ItemBuilder(Material.HARD_CLAY).setAmount(16));
                }
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

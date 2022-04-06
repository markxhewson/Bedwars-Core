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

public class RangedShopMenu extends Menu {

    private final Bedwars instance;
    private final GamePlayer gamePlayer;
    private final Game game;

    public RangedShopMenu(Bedwars instance, GamePlayer gamePlayer, Game game) {
        this.instance = instance;
        this.gamePlayer = gamePlayer;
        this.game = game;
    }

    @Override
    public String getMenuName() {
        return "Ranged Shop";
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
        inventory.setItem(19, new ItemBuilder(Material.ARROW).setDisplayName(goldCount < 2 ? "&cArrow" : "&aArrow").setAmount(8).setLore("&7Cost: &62 Gold").build());
        inventory.setItem(20, new ItemBuilder(Material.BOW).setDisplayName(goldCount < 12 ? "&cBow" : "&aBow").setLore("&7Cost: &612 Gold").build());
        inventory.setItem(21, new ItemBuilder(Material.BOW).setDisplayName(goldCount < 24 ? "&cBow (Power I)" : "&aBow (Power I)").setLore("&7Cost: &624 Gold").build());
        inventory.setItem(22, new ItemBuilder(Material.BOW).setDisplayName(emeraldCount < 6 ? "&cBow (Power I, Punch I)" : "&aBow (Power I, Punch I)").setLore("&7Cost: &26 Emeralds").build());
    }

    @Override
    public void handleClick(InventoryClickEvent e) throws Exception {
        ItemStack itemStack = e.getCurrentItem();
        Player player = (Player) e.getWhoClicked();

        int ironCount = gamePlayer.getItemCount(Material.IRON_INGOT);
        int goldCount = gamePlayer.getItemCount(Material.GOLD_INGOT);
        int emeraldCount = gamePlayer.getItemCount(Material.EMERALD);

        switch (itemStack.getType()) {
            case ARROW:
                if (goldCount < 2) {
                    player.sendMessage(Chat.color("&cYou cannot afford this!"));
                    return;
                }

                gamePlayer.removeItem(Material.GOLD_INGOT, 2);
                gamePlayer.giveItem(new ItemBuilder(Material.ARROW).setAmount(8));
                break;

            case BOW:
                if (e.getSlot() == 5) {
                    new RangedShopMenu(this.instance, gamePlayer, game).open(player);
                    return;
                }
                if (e.getSlot() == 20) {
                    if (goldCount < 12) {
                        player.sendMessage(Chat.color("&cYou cannot afford this!"));
                        return;
                    }

                    gamePlayer.removeItem(Material.GOLD_INGOT, 12);
                    gamePlayer.giveItem(new ItemBuilder(Material.BOW));
                }
                else if (e.getSlot() == 21) {
                    if (goldCount < 24) {
                        player.sendMessage(Chat.color("&cYou cannot afford this!"));
                        return;
                    }

                    gamePlayer.removeItem(Material.GOLD_INGOT, 24);
                    gamePlayer.giveItem(new ItemBuilder(Material.BOW).addUnsafeEnchant(Enchantment.ARROW_DAMAGE, 1).setUnbreakable(true));
                }
                else if (e.getSlot() == 22) {
                    if (emeraldCount < 6) {
                        player.sendMessage(Chat.color("&cYou cannot afford this!"));
                        return;
                    }

                    gamePlayer.removeItem(Material.EMERALD, 6);
                    gamePlayer.giveItem(new ItemBuilder(Material.BOW)
                            .addUnsafeEnchant(Enchantment.ARROW_DAMAGE, 1)
                            .addUnsafeEnchant(Enchantment.ARROW_KNOCKBACK, 1)
                            .setUnbreakable(true));
                }
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
            case BREWING_STAND_ITEM:
                break;
            case TNT:
                new UtilityShopMenu(this.instance, gamePlayer, game).open(player);
                break;
        }
    }
}

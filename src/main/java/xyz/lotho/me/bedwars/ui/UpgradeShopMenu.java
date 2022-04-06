package xyz.lotho.me.bedwars.ui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import xyz.lotho.me.bedwars.Bedwars;
import xyz.lotho.me.bedwars.managers.game.Game;
import xyz.lotho.me.bedwars.managers.player.GamePlayer;
import xyz.lotho.me.bedwars.managers.team.Team;
import xyz.lotho.me.bedwars.managers.teamupgrades.ReinforcedArmorTier;
import xyz.lotho.me.bedwars.ui.util.Menu;
import xyz.lotho.me.bedwars.util.Chat;
import xyz.lotho.me.bedwars.util.ItemBuilder;

public class UpgradeShopMenu extends Menu {

    private final Bedwars instance;
    private final GamePlayer gamePlayer;
    private final Game game;
    private final Team team;

    public UpgradeShopMenu(Bedwars instance, GamePlayer gamePlayer, Game game, Team team) {
        this.instance = instance;
        this.gamePlayer = gamePlayer;
        this.game = game;
        this.team = team;
    }

    @Override
    public String getMenuName() {
        return "Upgrades Shop";
    }

    @Override
    public int getSlots() {
        return 36;
    }

    @Override
    public void setItems() {
        Inventory inventory = this.getInventory();

        int diamondCount = gamePlayer.getItemCount(Material.DIAMOND);

        inventory.setItem(10, new ItemBuilder(Material.IRON_SWORD)
                .setDisplayName("&cSharpened Swords")
                .setLore("&7Your team permanently gains",
                        "&7Sharpness I on all swords and", "&7axes",
                        "",
                        "&7Cost: &b4 Diamonds",
                        "",
                        team.hasSharpenedSwords() ? "&cAlready purchased!" : diamondCount < 4 ? "&cYou don't have enough Diamonds!" : "&aClick to purchase!")
                .build()
        );
        inventory.setItem(11, new ItemBuilder(Material.IRON_CHESTPLATE)
                .setDisplayName("&cReinforced Armor")
                .setLore("&7Your team permanently gains",
                        "&7Protection on all armor pieces!",
                        "",
                        "&7Tier 1: Protection I. &b4 Diamonds",
                        "&7Tier 2: Protection II. &b8 Diamonds",
                        "&7Tier 3: Protection III. &b12 Diamonds",
                        "&7Tier 4: Protection IV. &b16 Diamonds",
                        "",
                        team.getReinforcedArmorTier() == ReinforcedArmorTier.IV ? "&cMax tier achieved!" : (diamondCount < team.getReinforcedArmorTier().getDiamondsRequired() + 4 ? "&cYou don't have enough Diamonds!" : "&aClick to purchase!"))
                .build());

        this.fillRemainingSlots();
    }

    @Override
    public void handleClick(InventoryClickEvent e) throws Exception {
        ItemStack currentItem = e.getCurrentItem();
        Player player = (Player) e.getWhoClicked();

        int diamondCount = gamePlayer.getItemCount(Material.DIAMOND);

        switch (currentItem.getType()) {
            case IRON_SWORD:
                if (team.hasSharpenedSwords()) player.sendMessage(Chat.color("&cYou have already purchased this upgrade!"));
                else {
                    if (diamondCount < 4) {
                        player.sendMessage(Chat.color("&cYou do not have enough diamonds for this upgrade!"));
                        return;
                    }
                    gamePlayer.removeItem(Material.DIAMOND, 4);
                    team.broadcast("&6" + player.getName() + " &ahas purchased &6Sharpened Swords&a!");
                    team.setSharpenedSwords(true);
                    team.applySharpness();
                }

                break;

            case IRON_CHESTPLATE:
                ReinforcedArmorTier currentTier = team.getReinforcedArmorTier();

                if (currentTier == ReinforcedArmorTier.IV) {
                    player.sendMessage(Chat.color("&cYou already have the max tier of this upgrade!"));
                    return;
                }

                if (currentTier == ReinforcedArmorTier.NONE) {
                    if (diamondCount >= ReinforcedArmorTier.I.getDiamondsRequired()) team.setReinforcedArmorTier(ReinforcedArmorTier.I);
                    else {
                        player.sendMessage(Chat.color("&cYou do not have enough diamonds for this upgrade!"));
                        return;
                    }
                }
                else if (currentTier == ReinforcedArmorTier.I) {
                    if (diamondCount >= ReinforcedArmorTier.II.getDiamondsRequired()) team.setReinforcedArmorTier(ReinforcedArmorTier.II);
                    else {
                        player.sendMessage(Chat.color("&cYou do not have enough diamonds for this upgrade!"));
                        return;
                    }
                }
                else if (currentTier == ReinforcedArmorTier.II) {
                    if (diamondCount >= ReinforcedArmorTier.III.getDiamondsRequired()) team.setReinforcedArmorTier(ReinforcedArmorTier.III);
                    else {
                        player.sendMessage(Chat.color("&cYou do not have enough diamonds for this upgrade!"));
                        return;
                    }
                }
                else if (currentTier == ReinforcedArmorTier.III) {
                    if (diamondCount >= ReinforcedArmorTier.IV.getDiamondsRequired()) team.setReinforcedArmorTier(ReinforcedArmorTier.IV);
                    else {
                        player.sendMessage(Chat.color("&cYou do not have enough diamonds for this upgrade!"));
                        return;
                    }
                }

                team.broadcast("&6" + player.getName() + " &ahas purchased &6" + team.getReinforcedArmorTier().getFormattedName() + "&a!");
                gamePlayer.removeItem(Material.DIAMOND, team.getReinforcedArmorTier().getDiamondsRequired());

                team.applyProtection();
        }
    }
}

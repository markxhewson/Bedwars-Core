package xyz.lotho.me.bedwars.ui.util;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public abstract class Menu implements InventoryHolder {

    protected Inventory inventory = null;
    protected ItemStack FILLER_GLASS = new ItemStack(Material.STAINED_GLASS_PANE, 1);


    public abstract String getMenuName();

    public abstract int getSlots();

    public abstract void handleClick(InventoryClickEvent e) throws Exception;

    public abstract void setItems();

    public void open(Player player) {
        this.inventory = Bukkit.createInventory(this, getSlots(), getMenuName());

        this.setItems();
        this.fillRemainingSlots();

        player.openInventory(this.getInventory());
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public void fillRemainingSlots(){
        for (int i = 0; i < getSlots(); i++) {
            if (inventory.getItem(i) == null){
                inventory.setItem(i, FILLER_GLASS);
            }
        }
    }
}

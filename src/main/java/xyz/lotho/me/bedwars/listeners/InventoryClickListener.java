package xyz.lotho.me.bedwars.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;
import xyz.lotho.me.bedwars.Bedwars;
import xyz.lotho.me.bedwars.ui.util.Menu;

public class InventoryClickListener implements Listener {

    private Bedwars instance;

    public InventoryClickListener(Bedwars instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) throws Exception {
        InventoryHolder holder = event.getInventory().getHolder();

        if (!(holder instanceof Menu)) return;

        event.setCancelled(true);
        if (event.getCurrentItem() == null) return;

        Menu menu = (Menu) holder;
        menu.handleClick(event);
    }
}

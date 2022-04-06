package xyz.lotho.me.bedwars.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryHolder;
import xyz.lotho.me.bedwars.Bedwars;
import xyz.lotho.me.bedwars.managers.game.Game;
import xyz.lotho.me.bedwars.ui.util.Menu;

public class InventoryClickListener implements Listener {

    private final Bedwars instance;

    public InventoryClickListener(Bedwars instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) throws Exception {
        Game game = this.instance.getGameManager().findPlayerGame(event.getWhoClicked().getUniqueId());

        if (event.getSlotType() == InventoryType.SlotType.ARMOR && game != null) {
            event.setCancelled(true);
        }

        InventoryHolder holder = event.getInventory().getHolder();

        if (!(holder instanceof Menu)) return;
        if (event.getCurrentItem() == null) return;
        if (event.getCurrentItem().getType() == Material.AIR) return;
        if (event.getSlotType() == InventoryType.SlotType.OUTSIDE) return;

        event.setCancelled(true);

        Menu menu = (Menu) holder;
        menu.handleClick(event);
    }
}

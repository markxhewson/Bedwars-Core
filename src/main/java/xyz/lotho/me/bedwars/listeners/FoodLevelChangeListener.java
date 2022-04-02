package xyz.lotho.me.bedwars.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import xyz.lotho.me.bedwars.Bedwars;

public class FoodLevelChangeListener implements Listener {

    private final Bedwars instance;

    public FoodLevelChangeListener(Bedwars instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onFoodChange(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }
}

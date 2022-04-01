package xyz.lotho.me.bedwars.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import xyz.lotho.me.bedwars.Bedwars;
import xyz.lotho.me.bedwars.managers.game.Game;
import xyz.lotho.me.bedwars.ui.PickTeamMenu;
import xyz.lotho.me.bedwars.util.Chat;

public class PlayerInteractListener implements Listener {

    private final Bedwars instance;

    public PlayerInteractListener(Bedwars instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR) return;

        Player player = event.getPlayer();
        Game game = this.instance.getGameManager().findPlayerGame(player.getUniqueId());

        if (game == null) return;
        if (game.isStarted()) return;

        player.sendMessage(Chat.color("&aGame found! &7Loading team menu.."));
        new PickTeamMenu(this.instance, game).open(player);

    }
}

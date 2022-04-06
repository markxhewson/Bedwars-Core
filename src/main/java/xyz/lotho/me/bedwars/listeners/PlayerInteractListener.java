package xyz.lotho.me.bedwars.listeners;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import xyz.lotho.me.bedwars.Bedwars;
import xyz.lotho.me.bedwars.managers.game.Game;
import xyz.lotho.me.bedwars.managers.player.GamePlayer;
import xyz.lotho.me.bedwars.ui.main.ItemShopMenu;
import xyz.lotho.me.bedwars.ui.main.UpgradeShopMenu;
import xyz.lotho.me.bedwars.util.Chat;

public class PlayerInteractListener implements Listener {

    private final Bedwars instance;

    public PlayerInteractListener(Bedwars instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onEntityInteractAtEntity(PlayerInteractAtEntityEvent event) {
        if (event.getRightClicked().getType() == EntityType.VILLAGER) {
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onEntityInteract(PlayerInteractEntityEvent event) {
        if (!(event.getRightClicked() instanceof Villager)) return;

        event.setCancelled(true);

        Player player = event.getPlayer();
        Game game = this.instance.getGameManager().findGameByPlayer(player.getUniqueId());
        if (game == null) return;

        GamePlayer gamePlayer = game.getGamePlayerManager().getPlayer(player.getUniqueId());
        if (gamePlayer == null) return;

        Villager villager = (Villager) event.getRightClicked();

        if (villager.getProfession() == Villager.Profession.BLACKSMITH) { // item shop
            player.sendMessage(Chat.color("&aYou clicked item shop!"));
            new ItemShopMenu(this.instance, gamePlayer, game).open(player);
        }
        else if (villager.getProfession() == Villager.Profession.LIBRARIAN) { // upgrades shop
            player.sendMessage(Chat.color("&aYou clicked upgrades shop!"));
            new UpgradeShopMenu(this.instance, gamePlayer, game, gamePlayer.getTeam()).open(player);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR) return;

        Player player = event.getPlayer();
        Game game = this.instance.getGameManager().findGameByPlayer(player.getUniqueId());

        if (game == null) return;
        if (game.isStarted()) return;

        player.sendMessage(Chat.color("&aGame found! &7Loading team menu.."));
        game.getPickTeamMenu().open(player);
    }
}

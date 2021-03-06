package xyz.lotho.me.bedwars.listeners;

import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import xyz.lotho.me.bedwars.Bedwars;
import xyz.lotho.me.bedwars.managers.game.Game;
import xyz.lotho.me.bedwars.managers.player.GamePlayer;

public class GameDeathListener implements Listener {

    private final Bedwars instance;

    public GameDeathListener(Bedwars instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (player.getLocation().getY() <= 60) {
            Game game = this.instance.getGameManager().findGameByPlayer(player.getUniqueId());
            if (game == null) return;

            GamePlayer gamePlayer = game.getGamePlayerManager().getPlayer(player.getUniqueId());

            if (gamePlayer.getTeam().isBedBroken()) gamePlayer.kill(true, null);
            else gamePlayer.kill(false, null);
        }
    }

    @EventHandler
    public void fallDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Villager) {
            event.setCancelled(true);
        }

        if (!(event.getDamager() instanceof Player)) return;
        if (!(event.getEntity() instanceof Player)) return;

        final Player damager = (Player) event.getDamager();
        final Player damaged = (Player) event.getEntity();
        final double damage = event.getFinalDamage();

        Game game = this.instance.getGameManager().findGameByPlayer(damaged.getUniqueId());
        if (game == null) return;

        GamePlayer gamePlayer = game.getGamePlayerManager().getPlayer(damager.getUniqueId());
        GamePlayer otherGamePlayer = game.getGamePlayerManager().getPlayer(damaged.getUniqueId());

        if (gamePlayer.getTeam().getTeamName().equals(otherGamePlayer.getTeam().getTeamName())) {
            event.setCancelled(true);
            return;
        }

        if ((damaged.getHealth() - damage) <= 0) {
            event.setCancelled(true);

            GamePlayer killed = game.getGamePlayerManager().getPlayer(damaged.getUniqueId());
            GamePlayer killer = game.getGamePlayerManager().getPlayer(damager.getUniqueId());

            if (killed.getTeam().isBedBroken()) killed.kill(true, killer);
            else killed.kill(false, killer);
        }
    }
}

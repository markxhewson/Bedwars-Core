package xyz.lotho.me.bedwars.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import xyz.lotho.me.bedwars.Bedwars;
import xyz.lotho.me.bedwars.managers.game.Game;
import xyz.lotho.me.bedwars.managers.player.GamePlayer;
import xyz.lotho.me.bedwars.managers.team.Team;
import xyz.lotho.me.bedwars.util.Chat;

public class GameDeathListener implements Listener {

    private final Bedwars instance;

    public GameDeathListener(Bedwars instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (player.getLocation().getY() <= 60) {
            Game game = this.instance.getGameManager().findPlayerGame(player.getUniqueId());
            if (game == null) return;

            GamePlayer gamePlayer = game.getGamePlayerManager().getPlayer(player.getUniqueId());
            gamePlayer.kill();
        }
    }

    @EventHandler
    public void fallDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;

        if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;
        if (!(event.getEntity() instanceof Player)) return;

        final Player damager = (Player) event.getDamager();
        final Player damaged = (Player) event.getEntity();
        final double damage = event.getFinalDamage();

        if ((damaged.getHealth() - damage) <= 0) {
            event.setCancelled(true);

            Game game = this.instance.getGameManager().findPlayerGame(damaged.getUniqueId());
            if (game == null) return;

            Team killerTeam = game.getGamePlayerManager().getPlayerTeam(damager.getUniqueId());

            GamePlayer damagedPlayer = game.getGamePlayerManager().getPlayer(damaged.getUniqueId());
            damagedPlayer.killByPlayer(damager, killerTeam);
        }
    }
}

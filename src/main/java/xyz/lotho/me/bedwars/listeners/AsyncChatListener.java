package xyz.lotho.me.bedwars.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import xyz.lotho.me.bedwars.Bedwars;
import xyz.lotho.me.bedwars.managers.game.Game;
import xyz.lotho.me.bedwars.managers.team.Team;
import xyz.lotho.me.bedwars.util.Chat;

public class AsyncChatListener implements Listener {

    private final Bedwars instance;

    public AsyncChatListener(Bedwars instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        Game game = this.instance.getGameManager().findGameByPlayer(player.getUniqueId());
        if (game != null) {
            Team team = game.getGamePlayerManager().getPlayerTeam(player.getUniqueId());
            if (player.isOp()) event.setFormat(Chat.color("&7[1✫]" + team.getTeamColor() + "[" + team.getTeamName() + "] &c[ADMIN] " + "%s&7" + ": %s"));
            else event.setFormat(Chat.color("&7[1✫]" + team.getTeamColor() + "[" + team.getTeamName() + "] &7" + "%s" + ": %s"));
        } else {
            if (player.isOp()) event.setFormat(Chat.color("&7[1✫] &c[ADMIN] " + "%s&7" + ": %s"));
            else event.setFormat(Chat.color("&7[1✫]" + "%s" + ": %s"));
        }
    }
}

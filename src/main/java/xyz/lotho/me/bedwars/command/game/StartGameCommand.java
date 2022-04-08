package xyz.lotho.me.bedwars.command.game;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.lotho.me.bedwars.Bedwars;
import xyz.lotho.me.bedwars.managers.game.Game;
import xyz.lotho.me.bedwars.ui.main.MapSelectMenu;
import xyz.lotho.me.bedwars.util.Chat;

import java.util.UUID;

public class StartGameCommand implements CommandExecutor {

    private final Bedwars instance;

    public StartGameCommand(Bedwars instance) {
        this.instance = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return false;

        Game game = this.instance.getGameManager().findGameByPlayer(((Player) sender).getUniqueId());
        if (game != null) {
            sender.sendMessage(Chat.color("&cYou cannot use this command while in-game."));
            return false;
        }

        if (this.instance.getQueueManager().getQueuedPlayers().size() < 1) {
            sender.sendMessage(Chat.color("&cYou are unable to start a game with less than 2 people in queue."));
            return false;
        }

        new MapSelectMenu(this.instance).open(((Player) sender).getPlayer());
        return true;
    }
}
